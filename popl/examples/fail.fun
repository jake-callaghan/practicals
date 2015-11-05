val index(x, xs) =
  let rec search(ys) =
    if ys = nil then fail()
    else if head(ys) = x then 0
    else search(tail(ys)) + 1 in
  search(xs) orelse -1;;
