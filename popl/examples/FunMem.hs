module FunMem(main) where
import Parsing
import FunSyntax
import FunParser
import Environment
import Memory

data Value =
    IntVal Integer			-- Integers
  | BoolVal Bool			-- Booleans
  | Nil 				-- Empty list
  | Cons Value Value			-- Non-empty lists
  | Addr Location
  | Function ([Value] -> Mem -> (Value, Mem))

type Env = Environment Value

type Mem = Memory Value

eval :: Expr -> Env -> Mem -> (Value, Mem)

eval (Number n) env mem = (IntVal n, mem)

eval (Variable x) env mem = (find env x, mem)

eval (If e1 e2 e3) env mem =
  let (b, mem') = eval e1 env mem in
  case b of
    BoolVal True -> eval e2 env mem'
    BoolVal False -> eval e3 env mem'
    _ -> error "boolean required in conditional"

eval (Apply f es) env mem =
  let (fv, mem') = eval f env mem in
  let (args, mem'') = evalargs es env mem' in
  apply fv args mem''

eval (Let d e1) env mem =
  let (env', mem1) = elab d env mem in
  eval e1 env' mem1

eval (Assign e1 e2) env mem =
  let (v1, mem') = eval e1 env mem in
  case v1 of
    Addr a ->
      let (v2, mem'') = eval e2 env mem' in
      (v2, update mem'' a v2)
    _ -> error "assigning to a non-variable"

eval (Sequence e1 e2) env mem =
  let (v1, mem') = eval e1 env mem in eval e2 env mem'

eval (While e1 e2) env mem = f mem
  where
    f mem =
      let (b, mem') = eval e1 env mem in
      case b of
        BoolVal True -> let (v, mem'') = eval e2 env mem' in f mem''
        BoolVal False -> (Nil, mem')
        _ -> error "boolean required in while loop"

eval (Lambda xs body) env mem =
  (abstract xs body env, mem)

eval e env mem =
  error ("can't evaluate " ++ pretty e)

evalargs :: [Expr] -> Env -> Mem -> ([Value], Mem)
evalargs [] env mem = ([], mem)
evalargs (e:es) env mem =
  let (v, mem1) = eval e env mem in
  let (vs, mem2) = evalargs es env mem1 in
  (v:vs, mem2)

elab :: Defn -> Env -> Mem -> (Env, Mem)
elab (Val x e) env mem = 
  let (v, mem1) = eval e env mem in
  (define env x v, mem1)
elab (Rec x (Lambda xs e1)) env mem =
  (env', mem) where env' = define env x (abstract xs e1 env')
elab (Rec x _) env mem =
  error "RHS of letrec must be a lambda"

abstract :: [Ident] -> Expr -> Env -> Value
abstract fps body env =
  Function (\ args -> eval body (defargs env fps args))

apply :: Value -> [Value] -> Mem -> (Value, Mem)
apply (Function f) args mem = f args mem
apply _ args mem = error "applying a non-function"

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
    primitive "new" (\ [] mem ->
      let (a, mem') = fresh mem in (Addr a, mem')),
    primitive "!" (\ [Addr a] mem -> (contents mem a, mem))]
  where 
    constant x v = (x, v)
    primitive x f = (x, Function (primwrap x f))
    pureprim x f = 
      (x, Function (primwrap x (\ args mem -> (f args, mem))))

instance Eq Value where
  IntVal a == IntVal b = a == b
  BoolVal a == BoolVal b = a == b
  Nil == Nil = True
  Cons h1 t1 == Cons h2 t2 = (h1 == h2) && (t1 == t2)
  Function _ == Function _ = error "can't compare functions"
  Addr a == Addr b = a == b
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
  show (Addr a) = "<address " ++ show a ++ ">"

obey :: Phrase -> GloState -> (String, GloState)

type GloState = (Env, Mem)

obey (Calculate exp) (env, mem) =
  let (v, mem') = eval exp env mem in
  (print_value v, (env, mem'))

obey (Define def) (env, mem) =
  let x = def_lhs def in
  let (env', mem') = elab def env mem in
  (print_defn env' x, (env', mem'))

main = dialog funParser obey (init_env, init_mem)
