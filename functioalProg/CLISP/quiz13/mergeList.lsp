(defun lengthOfList(List1)
        (cond
                ((not (listp List1)) (print "Not a list"))
                ((null List1)0)
                (t (+ 1(lengthOfList(cdr List1))))
        )
)

(defun mergeList (List1 List2)
  	      (cond
		((not (= (lengthOfList List1) (lengthOfList List2)))(princ "lists not equal"))
		((null List1)nil)
		(t (cons (car List1)(cons (car List2) (mergeList (cdr List1) (cdr List2))))))
)

