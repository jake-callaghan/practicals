module FunPipe(main) where
import Parsing
import FunSyntax
import FunParser
import Environment

infixl 1 $>

type M a = Cont a -> Answer

type Cont a = Command a -> Answer

data Command a =
    Done a
  | In (Value -> M a)
  | Out Value (M a)

result :: a -> M a
result x k = k (Done x)

($>) :: M a -> (a -> M b) -> M b
(xm $> f) k = 
  xm (\ cmd -> 
    case cmd of
      Done x -> f x k
      In g -> k (In (\ v -> g v $> f))
      Out s xm' -> k (Out s (xm' $> f)))

input :: M Value
input k = k (In (\ v -> result v))

output :: Value -> M ()
output v k = k (Out v (result ()))

pipe :: M a -> M a -> M a
pipe xm ym k =
  ym (\ cmd -> 
    case cmd of
      Done x -> k (Done x)
      In g -> pipe2 xm g k
      Out s ym' -> k (Out s (pipe xm ym')))

pipe2 :: M a -> (Value -> M a) -> M a
pipe2 xm g k = 
  xm (\ cmd ->
    case cmd of
      Done x -> k (Done x)
      In h -> k (In (\ v -> pipe2 (h v) g))
      Out s xm' -> pipe xm' (g s) k)

data Value =
    IntVal Integer			-- Integers
  | BoolVal Bool			-- Booleans
  | Nil 				-- Empty list
  | Cons Value Value			-- Non-empty lists
  | Function ([Value] -> M Value)

type Env = Environment Value

eval :: Expr -> Env -> M Value

eval (Number n) env = result (IntVal n)

eval (Variable x) env = result (find env x)

eval (Apply f es) env =
  eval f env $> (\ fv ->
    evalargs es env $> (\ args ->
      apply fv args))

eval (Lambda xs e1) env =
  result (abstract xs e1 env)

eval (If e1 e2 e3) env =
  eval e1 env $> (\ b ->
    case b of
      BoolVal True -> eval e2 env
      BoolVal False -> eval e3 env
      _ -> error "boolean required in conditional")

eval (Let d e1) env =
  elab d env $> (\ env' -> eval e1 env')
eval (Sequence e1 e2) env = eval e1 env $> (\ v -> eval e2 env)
eval (Pipe e1 e2) env = pipe (eval e1 env) (eval e2 env)

abstract :: [Ident] -> Expr -> Env -> Value
abstract xs e env =
  Function (\ args -> eval e (defargs env xs args))

apply :: Value -> [Value] -> M Value
apply (Function f) args = f args
apply _ args = error "applying a non-function"
elab :: Defn -> Env -> M Env
elab (Val x e) env = 
  eval e env $> (\ v -> result (define env x v))
elab (Rec x (Lambda xs e1)) env =
  result env' where env' = define env x (abstract xs e1 env')
elab (Rec x _) env =
  error "RHS of letrec must be a lambda"
evalargs :: [Expr] -> Env -> M [Value]
evalargs [] env = result []
evalargs (e:es) env =
  eval e env $> (\ v -> evalargs es env $> (\ vs -> result (v:vs)))

init_env :: Env
init_env =
  make_env [constant "nil" Nil, 
            constant "true" (BoolVal True), 
            constant "false" (BoolVal False),
    pureprim "+" (\ [IntVal a, IntVal b] -> IntVal (a + b)),
    pureprim "-" (\ [IntVal a, IntVal b] -> IntVal (a - b)),
    pureprim "*" (\ [IntVal a, IntVal b] -> IntVal (a * b)),
    pureprim "div" (\ [IntVal a, IntVal b] -> 
      if b == 0 then error "Dividing by zero" else IntVal (a `div` b)),
    pureprim "mod" (\ [IntVal a, IntVal b] ->
      if b == 0 then error "Dividing by zero" else IntVal (a `mod` b)),
    pureprim "~" (\ [IntVal a] -> IntVal (- a)),
    pureprim "<" (\ [IntVal a, IntVal b] -> BoolVal (a < b)),
    pureprim "<=" (\ [IntVal a, IntVal b] -> BoolVal (a <= b)),
    pureprim ">" (\ [IntVal a, IntVal b] -> BoolVal (a > b)),
    pureprim ">=" (\ [IntVal a, IntVal b] -> BoolVal (a >= b)),
    pureprim "=" (\ [a, b] -> BoolVal (a == b)),
    pureprim "<>" (\ [a, b] -> BoolVal (a /= b)),
    pureprim "integer" (\ [a] ->
      case a of IntVal _ -> BoolVal True; _ -> BoolVal False),
    pureprim "head" (\ [Cons h t] -> h),
    pureprim "tail" (\ [Cons h t] -> t),
    pureprim ":" (\ [a, b] -> Cons a b),
    primitive "input" (\ [] -> input),
    primitive "output" (\ [v] -> output v $> (\ () -> result Nil))]
  where constant x v = (x, v)
        primitive x f = (x, Function (primwrap x f))
        pureprim x f = primitive x (result . f) 

instance Eq Value where
  IntVal a == IntVal b = a == b
  BoolVal a == BoolVal b = a == b
  Nil == Nil = True
  Cons h1 t1 == Cons h2 t2 = (h1 == h2) && (t1 == t2)
  Function _ == Function _ = error "can't compare functions"
  _ == _ = False

instance Show Value where
  show (IntVal n) = show n
  show (BoolVal b) = if b then "true" else "false"
  show Nil = "[]"
  show (Cons h t) = "[" ++ show h ++ shtail t ++ "]"
    where 
      shtail Nil = ""
      shtail (Cons h t) = ", " ++ show h ++ shtail t
      shtail x = " . " ++ show x
  show (Function _) = "<function>"

type GloState = Env

type Answer = IO GloState

obeym :: Phrase -> GloState -> IO GloState

obeym (Calculate exp) env =
  eval exp env 
    (topcont (\ v -> 
      do putStrLn (print_value v); return env))

obeym (Define def) env =
  let x = def_lhs def in
  elab def env 
    (topcont (\ env' -> 
      do putStrLn (print_defn env' x); return env'))

topcont :: (a -> IO GloState) -> Cont a
topcont k = kk
  where
    kk (In f) = do n <- readLn; f (IntVal n) kk
    kk (Out v xm) = do printStrLn (show v); xm kk
    kk (Done v) = k v

main = dialogm funParser obeym init_env
