--===================
--= 	 lab1       =
--===================

-- foldl f e (x:xs) = foldl f (f e x) xs
rec foldl(f,e,xs) = 
	if xs = nil then e else foldl(f,f(e,head(xs)),tail(xs));;

-- foldr f e (x:xs) = f x (foldr f e xs)
rec foldr(f,e,xs) = 
	if xs = nil then e else f(head(xs),foldr(f,e,tail(xs)));;

-- append xs ys = xs ++ ys
val append(xs,ys) = foldr((lambda(a,as) a:as),ys,xs);;
	
-- concat concatenates the elements of a list of lists
val concat(xss) = foldl(append,list(),xss);;

-- for testing
val xxx = list(list(1,2),list(),list(3,4));;
val yyy = list(1, list(2,3), list(4,list(5),6));;

-- flatten produces a list with no nesting
-- rec flatten(x) =