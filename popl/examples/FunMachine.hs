module FunMachine where
import Parsing
import FunSyntax
import FunParser
import Environment

data Value =
    IntVal Integer			-- Integers
  | BoolVal Bool			-- Booleans
  | Nil 				-- Empty list
  | Cons Value Value			-- Non-empty lists
  | Closure [Ident] Expr Env		-- Function closures
  | Primitive Prim			-- Primitives

data Prim = 
    Plus | Minus | Times | Div | Mod | Uminus | Less | Greater
  | Leq | Geq | Equal | Neq | Integer | Head | Tail | Consop
  deriving Show

type Env = Environment Value

data ValCont =
    IfTest Expr Expr Env ValCont

  | Apply1 [Expr] Env ValCont

  | Args1 [Expr] Env ArgCont

  | Bind Ident Env EnvCont

  | PrintVal Env

data ArgCont =
    Apply2 Value ValCont
  | Args2 Value ArgCont

data EnvCont =
    Let1 Expr ValCont

  | PrintDef Ident

eval :: Expr -> Env -> ValCont -> Answer
eval (Number n) env k = valcont k (IntVal n)
eval (Variable x) env k = valcont k (find env x)

eval (If e1 e2 e3) env k = eval e1 env (IfTest e2 e3 env k)

eval (Apply f es) env k = eval f env (Apply1 es env k)

eval (Lambda xs e1) env k = valcont k (abstract xs e1 env)

eval (Let d e1) env k = elab d env (Let1 e1 k)
eval e env k = error ("can't evaluate " ++ pretty e)

apply :: Value -> [Value] -> ValCont -> Answer
apply (Closure xs e env) args k = eval e (defargs env xs args) k
apply (Primitive p) args k = primapply p args (valcont k)
apply _ args k = error "applying a non-function"

evalargs :: [Expr] -> Env -> ArgCont -> Answer
evalargs [] env k = argcont k []
evalargs (e:es) env k = eval e env (Args1 es env k)

abstract :: [Ident] -> Expr -> Env -> Value
abstract xs e env = Closure xs e env

elab :: Defn -> Env -> EnvCont -> Answer
elab (Val x e) env k = eval e env (Bind x env k)
elab (Rec x (Lambda xs e1)) env k =
  envcont k env' where env' = define env x (abstract xs e1 env')
elab (Rec x _) env k =
  error "RHS of letrec must be a lambda"

valcont :: ValCont -> Value -> Answer
valcont (IfTest e2 e3 env k) b =
    case b of
      BoolVal True -> eval e2 env k
      BoolVal False -> eval e3 env k
      _ -> error "boolean required in conditional"

valcont (Apply1 es env k) fv = evalargs es env (Apply2 fv k)

valcont (Args1 es env k) v = evalargs es env (Args2 v k)

valcont (Bind x env k) v = envcont k (define env x v)

valcont (PrintVal env) v = (print_value v, env)

argcont :: ArgCont -> [Value] -> Answer
argcont (Apply2 fv k) args = apply fv args k
argcont (Args2 v k) vs = argcont k (v:vs)

envcont :: EnvCont -> Env -> Answer
envcont (Let1 e1 k) env' = eval e1 env' k

envcont (PrintDef x) env' = (print_defn env' x, env')

primapply :: Prim -> [Value] -> (Value -> Answer) -> Answer
primapply Plus [IntVal a, IntVal b] k = k (IntVal (a + b))
primapply Minus [IntVal a, IntVal b] k = k (IntVal (a - b))
primapply Times [IntVal a, IntVal b] k = k (IntVal (a * b))
primapply Div [IntVal a, IntVal b] k = 
  if b == 0 then error "dividing by zero" 
  else k (IntVal (a `div` b))
primapply Mod [IntVal a, IntVal b] k =
  if b == 0 then error "dividing by zero" 
  else k (IntVal (a `mod` b))
primapply Uminus [IntVal a] k = k (IntVal (- a))
primapply Less [IntVal a, IntVal b] k = k (BoolVal (a < b))
primapply Leq [IntVal a, IntVal b] k = k (BoolVal (a <= b))
primapply Greater [IntVal a, IntVal b] k = k (BoolVal (a > b))
primapply Geq [IntVal a, IntVal b] k = k (BoolVal (a >= b))
primapply Equal [a, b] k = k (BoolVal (a == b))
primapply Neq [a, b] k = k (BoolVal (a /= b))
primapply Integer [a] k =
  case a of 
    IntVal _ -> k (BoolVal True)
    _ -> k (BoolVal False)
primapply Head [Cons h t] k = k h
primapply Tail [Cons h t] k = k t
primapply Consop [a, b] k = k (Cons a b)
primapply x args k = 
  error ("bad arguments to primitive " ++ show x ++ ": " 
						++ showlist args)

init_env :: Env
init_env =
  make_env [constant "nil" Nil, 
            constant "true" (BoolVal True), 
            constant "false" (BoolVal False),
    primitive "+" Plus, primitive "-" Minus, primitive "*" Times,
    primitive "div" Div, primitive "mod" Mod, primitive "~" Uminus,
    primitive "<" Less, primitive ">" Greater, primitive ">=" Geq, 
    primitive "<=" Leq, primitive "=" Equal, primitive "<>" Neq,  
    primitive "integer" Integer, primitive "head" Head, 
    primitive "tail" Tail, primitive ":" Consop]
  where
    constant x v = (x, v)
    primitive x p = (x, Primitive p)

instance Eq Value where
  IntVal a == IntVal b = a == b
  BoolVal a == BoolVal b = a == b
  Nil == Nil = True
  Cons h1 t1 == Cons h2 t2 = (h1 == h2) && (t1 == t2)
  f == g | is_function f && is_function g = 
    error "can't compare functions"
  _ == _ = False

is_function :: Value -> Bool
is_function (Closure _ _ _ ) = True
is_function (Primitive _) = True
is_function _ = False

instance Show Value where
  show (IntVal n) = show n
  show (BoolVal b) = if b then "true" else "false"
  show Nil = "[]"
  show (Cons h t) = "[" ++ show h ++ shtail t ++ "]"
    where 
      shtail Nil = ""
      shtail (Cons h t) = ", " ++ show h ++ shtail t
      shtail x = " . " ++ show x
  show (Closure _ _ _) = "<function>"
  show (Primitive x) = "<primitive " ++ show x ++ ">"

type Answer = (String, Env)

obey :: Phrase -> Env -> (String, Env)
obey (Calculate exp) env =
  eval exp env (PrintVal env)
obey (Define def) env =
  let x = def_lhs def in
  elab def env (PrintDef x)

main = dialog funParser obey init_env
