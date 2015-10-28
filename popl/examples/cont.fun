val index(x, xs) =
  callcc(lambda (k)
    let rec search(ys) =
      if ys = nil then throw(k, -1)
      else if head(ys) = x then 0
      else search(tail(ys))+1 in
    search(xs));;

val top = new();;
val breakpoint = new();;

val break(n) =
  callcc(lambda (k) breakpoint := k; throw(!top, n));;

val resume(x) = throw(!breakpoint, x);;

callcc(lambda (k) top := k);;
