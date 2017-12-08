(defun lenOfList(List1)
        (cond
                ((not (listp List1)) (print "Not a list"))
                ((null List1)0)
                (t (+ 1(lenOfList(cdr List1))))
        )
)

(defun lastElement(List1)
	(cond
		((null List1)nil)
		((and (= 1 (lenOfList List1))(not(listp(car List1))))(car List1))
                ((= 1 (lenOfList List1)) (lastElement(car List1)))
		(t(lastElement(cdr List1)))
	)
)

(defun secondLastElement(List1)
	(cond
		((not (listp List1)) (princ "Not a list"))
		((null List1)nil)
		((= 1 (lenOfList List1))(princ "Single element"))
		((and (= 2 (lenOfList List1))(not(listp(car List1))))(car List1))
		((= 2 (lenOfList List1)) (lastElement(car List1)))
                (t(secondLastElement(cdr List1)))
	)
)
