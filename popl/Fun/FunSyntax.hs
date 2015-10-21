module FunSyntax where

import Data.Char(isAlpha)
import Data.List(intersperse)

data Phrase =                 -- Top-level phrase $p$
    Calculate Expr            -- \pg $e$;;
  | Define Defn               -- \pg $d$;;
  deriving Show

data Expr =                   -- Expressions $e$
    Number Integer            -- $n$
  | Variable Ident            -- $x$
  | Apply Expr [Expr]         -- \pg $e_0$($e_1$, \dots, $e_n$)
  | If Expr Expr Expr         -- \pg if $e_1$ then $e_2$ else $e_3$
  | Lambda [Ident] Expr       -- \pg lambda ($x_1$, \dots, $x_n$) $e$
  | Let Defn Expr             -- \pg let $d$ in $e$
  -- Extra forms not in the basic language:
  -- \dots~for imperative programming
  | Assign Expr Expr          -- \pg $e_1$ := $e_2$
  | Sequence Expr Expr        -- \pg $e_1$; $e_2$
  | While Expr Expr           -- \pg while $e_1$ do $e_2$
  -- \dots~for exceptions and non-determinism
  | OrElse Expr Expr          -- \pg $e_1$ orelse $e_2$
  -- \dots~for \longref{Exercises}{e:staramper1} to \ref{e:staramper2}
  | Contents Expr             -- \pg *$e_1$
  | Address Expr              -- \pg \&$e_1$
  | Subscript Ident Expr      -- \pg $e_1$[$e_2$]
  -- \dots~for \longref{Exercise}{ex:pipe}
  | Pipe Expr Expr		-- \pg $e_1$ >> $e_2$
  -- \dots~for records
  | Empty                     -- \pg \{ \}
  | Extend Expr Ident Expr    -- \pg \{ $e_1$ | $x$ => $e_2$ \}
  | Select Expr Ident         -- \pg $e_1$ . $x$
  -- \dots~for |Fun| in |Fun|
  | Literal String            -- \pg "$s$"
  deriving Show

data Defn =                   -- Definitions $d$
    Val Ident Expr            -- \pg val $x$ = $e$
  | Rec Ident Expr            -- \pg rec $x$ = $e$
  -- Extensions:
  -- \dots~for imperative programming
  | Array Ident Expr          -- \pg array $x$[$e$]
  -- \dots~for records
  | Open Expr                 -- \pg open $e$
  deriving Show

type Ident = String

def_lhs (Val x e) = x
def_lhs (Rec x e) = x
def_lhs (Array x e) = x

-- |pretty| -- pretty-print an expression with ellipses
pretty :: Expr -> String
pretty exp = pp 8 2 exp ""
  where pp :: Int -> Int -> Expr -> ShowS
        
        pp p d (Number n) = showString (show n)
        
        pp p d (Variable x) = showName x
        
        pp p d (Literal s) =
          showString "\"" . showString s . showString "\""
        
        pp p d (Select e1 x) =
          pp 0 (d-1) e1 . showString "." . showName x
        
        pp p 0 _ = showString "..."
        
        pp p d (Let def body) =
          showParen (p < 8)
            (showString "let " . pp_def d def 
               . showString " in " . pp 8 (d-1) body)
        
        pp p d (Lambda fps body) =
          showParen (p < 8)
            (showString "lambda (" . pp_list showName fps . showString ") "
               . pp 8 (d-1) body)
        
        pp p d (Sequence e1 e2) =
          showParen (p < 8)
            (pp 7 d e1 . showString "; " . pp 7 (d-1) e2)
        
        pp p d (If e1 e2 e3) =
          showParen (p < 7)
            (showString "if " . pp 7 (d-1) e1 . showString " then " 
               . pp 7 (d-1) e2 . showString " else " . pp 7 (d-1) e3)
        
        pp p d (While e1 e2) =
          showParen (p < 7)
            (showString "while " . pp 7 (d-1) e1 
               . showString " do " . pp 7 (d-1) e2)
        
        pp p d (OrElse e1 e2) =
          showParen (p < 6)
            (pp 5 (d-1) e1 . showString " orelse " . pp 6 (d-1) e2)
        
        pp p d (Assign e1 e2) =
          showParen (p < 4)
            (pp 3 (d-1) e1 . showString " := " . pp 4 (d-1) e2)
        
        pp p d (Apply f aps) = 
          showParen (p < 2)
            (pp 2 d f . showString "(" . pp_list (pp 8 (d-1)) aps . showString ")")
        
        pp p d (Contents e1) =
          showParen (p < 1) (showString "*" . pp 1 (d-1) e1)
        
        pp p d (Address e1) =
          showParen (p < 1) (showString "&" . pp 1 (d-1) e1)
        
        pp p d (Subscript x e2) =
          showParen (p < 2)
            (showName x . showString "[" . pp 8 (d-1) e2 . showString "]")
        
        pp p d Empty = showString "{ }"
        
        pp p d (Extend e1 x e2) =
          let (root, elems) = split e1 [(x, e2)] in
          showString "{ " 
             . (case root of Empty -> id; _ -> pp 8 (d-1) root . showString " | ")
             . pp_list (\ (y, e') -> 
                showName y . showString " => " . pp 8 (d-1) e') elems
             . showString " }"
          where
            split (Extend e1 x e2) elems = split e1 ((x, e2):elems)
            split e elems = (e, elems)
        
        showName x =
          if isAlpha (head x) then showString x
          else showString "(" . showString x . showString ")"
        
        pp_def :: Int -> Defn -> ShowS
        
        pp_def d (Val x e) =
          showString "val " . showName x . showString " = " . pp 8 (d-1) e
        
        pp_def d (Rec x e) =
          showString "rec " . showName x . showString " = " . pp 8 (d-1) e
        
        pp_def d (Array x e) =
          showString "array " . showName x . showString "["
             . pp 8 (d-1) e . showString "]"
        
        pp_def d (Open e) =
          showString "open " . pp 8 (d-1) e
        
        pp_list :: (a -> ShowS) -> [a] -> ShowS
        pp_list p = foldr (.) id . intersperse (showString ", ") . map p
