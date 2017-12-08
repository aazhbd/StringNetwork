In this folder it contains 4 fiels, are:
	1. lengthOfList.lsp ; problem 1 source file
	2. secondLastElement.lsp ; problem 2 source file
	3. mergeList.lsp ; problem 3 source file
	4. README.txt

Intially you need to provide the following command to execute clisp file:
$clisp
Then - 
	- Command that need to execute problem 1 with trace information: 
		>(load "lengthOfList.lsp")
		>(trace lengthOfList)
		>(lengthOfList '(1 2 3 4 5 6))

	- Command that need to execute problem 2 with trace information: 
		>(load "secondLastElement.lsp")
		>(trace secondLastElement)
		>(secondLastElement '(1 2 (3 4 5) 6 7))

	- Command that need to execute problem 3 with trace information: 
		>(load "mergeList.lsp")
		>(trace mergeList)
		>(mergeList '(1 3 5 7 9) '(2 4 6 8 10))

To exit need to press "clt+D"