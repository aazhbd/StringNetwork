(defun makelist (List) 
	(cons List nil)
)

(defun appendList (List1 List2)
    (cond
        ((null List1) List2)
        (t (cons (car List1) (appendList(cdr List1)List2)))
	)
)

(defun deleteOne (List1 List2 Result)
    (cond
        ((null List1) Result)
        ((=(car List1) List2)(deleteOne(cdr List1)List2 Result))
        ((not(=(car List1) List2)) (deleteOne (cdr List1) List2 (appendList Result (makelist(car List1)))))
    )
)
(defun deleteAll(Input1 Input2)
        (cond
                ((or (not (listp Input1)) (not (listp Input2))) (format t "Not a list"))
                ((null Input2) Input1)
                (t (deleteAll (deleteOne Input1 (car Input2) nil) (cdr Input2)))
        )
)
