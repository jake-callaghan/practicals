module Fungol(main) where
import Parsing
import Environment
import Memory
import FunSyntax
import FunParser


-- MONAD

infixl 1 $>

type Cont a = a -> Mem -> Answer
type M a = Mem -> Cont() -> Cont a -> Answer

result x mem kx ks = ks x mem

(xm $> f) mem kx ks = xm mem kx (\x mem' -> f x mem' kx ks)

get :: Location -> M Value
get a mem kx ks = ks (contents mem a) mem

put :: Location -> Value -> M ()
put a v mem kx ks = ks () (update mem a v)

new :: M Location
new mem kx ks = let (a, mem') = fresh mem in ks a mem'

bind :: Value -> M Location
bind v = new $> (\ a -> put a v $> (\ () -> result a))

exit :: M a
exit mem kx ks = kx () mem

orelse :: M a -> M a -> M a
orelse xm ym mem kx ks = xm mem (\x mem' -> ym mem' kx ks) ks

callxc :: (Cont () -> M a) -> M a
callxc f mem kx ks = f kx mem kx ks

withxc :: Cont () -> M a -> M a
withxc kx xm mem kx2 = xm mem kx ks 


-- SEMANTIC DOMAINS

data Value =
    IntVal Integer 
  | BoolVal Bool
  | Nil | Cons Value Value

data Def =
    Const Value
  | Ref Location
  | Proc ([Value] -> M Value)

type Env = Environment Def
type Mem = Memory Value


-- EVALUATOR

eval :: Expr -> Env -> M Value

eval (Number n) env = result (IntVal n)

eval (Variable x) env = 
  case find env x of
    Const v -> result v
    Ref a -> get a
    _ -> error "variable does not have a value"

eval (Apply (Variable x) es) env =
  mapm (\ e -> eval e env) es $> (\ args ->
    apply (find env x) args)

eval (If e1 e2 e3) env =
  eval e1 env $> (\b ->
    case b of
      BoolVal True -> eval e2 env
      BoolVal False -> eval e3 env
      _ -> error "boolean required in conditional")

eval (Lambda xs e1) env =
  error "no lambda expressions"

eval (Let d e1) env =
  elab d env $> (\env' -> eval e1 env')

eval (Assign (Variable x) e2) env =
  case find env x of
    Ref a ->
      eval e2 env $> (\ v2 -> put a v2 $> (\ () -> result v2))
    _ -> error "assigning to a non-variable"

eval (Sequence e1 e2) env =
  eval e1 env $> (\v -> eval e2 env)

eval (While e1 e2) env = u
  where
    u = eval e1 env $> (\v1 ->
      case v1 of
	BoolVal True -> eval e2 env $> (\v2 -> u)
	BoolVal False -> result Nil
	_ -> error "boolean required in while loop")

eval (Loop e1) env = orelse u (result Nil)
  where 
    u = eval e env $> (\v -> u))

eval Exit env = exit

eval e env =
  error ("can't evaluate " ++ pretty e)
	
mapm :: (a -> M b) -> [a] -> M [b]
mapm f [] = result []
mapm f (x:xs) =
  f x $> (\y -> mapm f xs $> (\ys -> result (y:ys)))

-- abstract now statically binds an exit continuation
abstract :: [Ident] -> Expr -> Env -> Cont() -> Def
abstract xs e env kx =
  Proc (\ args -> 
    withxc kx (mapm bind args $> (\ as ->
      eval e (defargs env xs (map Ref as)))))

apply :: Def -> [Value] -> M Value
apply (Proc f) args = f args
apply _ argms = error "applying a non-procedure"

elab :: Defn -> Env -> M Env

elab (Val x e) env mem kx ks = 
  (eval e env $> (\ v -> 
    bind v $> (\ a -> result (define env x (Ref a))))) mem kx ks

-- updated to use the new abstract
elab (Rec x (Lambda xs e1)) env mem kx ks =
  let env' = define env x (abstract xs e1 env' kx) in result env' mem kx ks

elab (Rec x _) env mem kx ks =
  error "RHS of letrec must be a lambda"


-- INITIAL ENVIRONMENT

init_env :: Env
init_env =
  make_env [("nil", Const Nil), 
    ("true", Const (BoolVal True)), ("false", Const (BoolVal False)),
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
    pureprim ":" (\ [a, b] -> Cons a b)]
  where
    constant x v = (x, Const v)
    pureprim x f = (x, Proc (primwrap x (result . f)))


-- AUXILIARY FUNCTIONS ON VALUES

instance Eq Value where
  IntVal a == IntVal b = a == b
  BoolVal a == BoolVal b = a == b
  Nil == Nil = True
  Cons h1 t1 == Cons h2 t2 = h1 == h2 && t1 == t2
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

instance Show Def where
  show (Const v) = show v
  show (Ref a) = "<ref " ++ show a ++ ">"
  show (Proc _) = "<proc>"


-- MAIN PROGRAM

type GloState = (Env, Mem)
type Answer = (String, GloState)

obey :: Phrase -> GloState -> (String, GloState)
obey (Calculate exp) (env, mem) =
  let (v, mem') = eval exp env mem in
  (print_value v, (env, mem'))
  eval exp env mem kx ks
    where kx = (\ () mem' -> ("exit in main !", (env,mem')))
          ks = (\ v mem' -> (print_value v, (env,mem')))

obey (Define def) (env, mem) =
  let x = def_lhs def in
  elab def env mem kx ks 
    where kx = (\ () mem' -> ("exit in definition!", (env,mem')))
          ks = (\ env' mem' -> (print_defn env' x, (env',mem')))

main = dialog funParser obey (init_env, init_mem)
