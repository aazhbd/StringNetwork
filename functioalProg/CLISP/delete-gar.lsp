(defun makelist (x) (cons x nil)) (defun my-append (A1 A2)
	(cond
		((null A1) A2)
		(t (cons (car A1) (my-append (cdr A1) A2)))) ) (defun deleteAll (L E R)
	(cond
		((not (listp L)) (princ "Not a list!"))
		((listp E) (princ "Not an atom!"))
		((null L) R)
		((= (car L) E) (deleteAll (cdr L) E R))
		((not (= (car L) E)) (deleteAll (cdr L) E (my-append R (makelist(car L)))))
	) ) (defun my-delete (L1 L2)
	(cond
		((not (listp L1)) (princ "Not a list!"))
		((not (listp L2)) (princ "Not a list!"))
		((null L2) L1)
		(t (my-delete (deleteAll L1 (car L2) nil) (cdr L2)))
	)
)
