rec interval(a, b) =    -- [a, a+1, ..., b]
  if a > b then nil else a : interval(a+1, b);;

rec map(f, xs) =
  if xs = nil then nil else f(head(xs)) : map(f, tail(xs));;

val sum(xs) =
  let rec loop(ys, s) =
    if ys = nil then s else loop(tail(ys), s + head(ys)) in
  loop(xs, 0);;

val sqsum(n) =
  sum(map(lambda (i) i * i, interval(1, n)));;
