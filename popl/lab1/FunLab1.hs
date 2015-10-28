module FunLab1(main) where

import Parsing
import FunSyntax
import FunParser
import Environment
import Memory

-- MONAD

type M a = Mem -> (String, a, Mem)

result :: a -> M a
result x mem = ("", x, mem)

($>) :: M a -> (a -> M b) -> M b
(xm $> f) mem =
  let (s1, x, mem') = xm mem in
  let (s2, y, mem'') = (f x) mem' in
  (s1++s2, y, mem'')

output :: String -> M ()
output s mem = (s, (), mem)

new :: M Location
new mem = let (a, mem') = fresh mem in ("", a, mem')

get :: Location -> M Value
get a mem = ("", contents mem a, mem)

put :: Location -> Value -> M ()
put a v mem = ("", (), update mem a v)


-- SEMANTIC DOMAINS

data Value =
    IntVal Integer
  | BoolVal Bool
  | Addr Location
  | Nil | Cons Value Value
  | Function ([Value] -> M Value)

type Env = Environment Value
type Mem = Memory Value


-- EVALUATOR

eval :: Expr -> Env -> M Value
eval (Number n) env = result (IntVal n)
eval (Variable x) env = result (find env x)
eval (Apply f es) env =
  eval f env $> (\fv ->
    evalargs es env $> (\args ->
      apply fv args))
eval (If e1 e2 e3) env =
  eval e1 env $> (\b ->
    case b of
      BoolVal True -> eval e2 env
      BoolVal False -> eval e3 env
      _ -> error "boolean required in conditional")
eval (Lambda xs e1) env =
  result (abstract xs e1 env)
eval (Let d e1) env =
  elab d env $> (\env' -> eval e1 env')
eval (Assign e1 e2) env =
  eval e1 env $> (\v1 ->
    case v1 of
      Addr a ->
        eval e2 env $> (\v2 ->
          put a v2 $> (\() -> result v2))
      _ -> error "assigning to a non-address")
eval (Sequence e1 e2) env =
  eval e1 env $> (\v -> eval e2 env)
eval (While e1 e2) env = u
  where
    u = eval e1 env $> (\v1 ->
      case v1 of
	BoolVal True -> eval e2 env $> (\v2 -> u)
	BoolVal False -> result Nil
	_ -> error "boolean required in while loop")
	
evalargs :: [Expr] -> Env -> M [Value]
evalargs [] env = result []
evalargs (e:es) env =
  eval e env $> (\v -> 
    evalargs es env $> (\vs -> 
      result (v:vs)))

abstract :: [Ident] -> Expr -> Env -> Value
abstract xs e env =
  Function (\args -> eval e (defargs env xs args))

apply :: Value -> [Value] -> M Value
apply (Function f) args = f args
apply _ args = error "applying a non-function"

elab :: Defn -> Env -> M Env
elab (Val x e) env = 
  eval e env $> (\v -> result (define env x v))
elab (Rec x e) env =
  case e of
    Lambda fps body ->
      result env' where env' = define env x (abstract fps body env')
    _ ->
      error "RHS of letrec must be a lambda"


-- INITIAL ENVIRONMENT

init_env :: Env
init_env =
  make_env [constant "nil" Nil, 
    constant "true" (BoolVal True), constant "false" (BoolVal False),
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
    pureprim "list" (\ xs -> foldr Cons Nil xs),
    primitive "print" (\ [v] -> output (show v) $> (\ () -> result v)),
    primitive "new" (\ [] -> new $> (\a -> result (Addr a))),
    primitive "!" (\ [Addr a] -> get a)]
  where
    constant x v = (x, v)
    primitive x f = (x, Function (primwrap x f))
    pureprim x f = (x, Function (primwrap x (\args -> result (f args))))


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
  show (Addr a) = "<address " ++ show a ++ ">"
  show Nil = "[]"
  show (Cons h t) = "[" ++ show h ++ shtail t ++ "]"
    where 
      shtail Nil = ""
      shtail (Cons h t) = ", " ++ show h ++ shtail t
      shtail x = " . " ++ show x
  show (Function _) = "<function>"


-- MAIN PROGRAM

type GloState = (Env, Mem)

obey :: Phrase -> GloState -> (String, GloState)
obey (Calculate exp) (env, mem) =
  let (out, v, mem') = eval exp env mem in
  (out ++ print_value v, (env, mem'))
obey (Define def) (env, mem) =
  let x = def_lhs def in
  let (out, env', mem') = elab def env mem in
  (out ++ print_defn env' x, (env', mem'))

main = dialog funParser obey (init_env, init_mem)
