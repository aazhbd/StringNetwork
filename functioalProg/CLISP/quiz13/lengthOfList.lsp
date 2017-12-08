(defun lengthOfList(List1)
        (cond
		((not (listp List1)) (print "Not a list"))
		((null List1)0)
                (t (+ 1(lengthOfList(cdr List1))))
	)
)

