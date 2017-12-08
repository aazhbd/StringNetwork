In this folder it contains 4 fiels, are:
	1. lengthList.P; problem 1 source file
	2. secondLastElement.P; problem 2 source file
	3. shuffle.P ; problem 3 source file
	4. README.txt

Intially you need to provide the following command to execute prolog file:
$prolog -i
Then - 
	- Command that need to execute problem 1 with trace information: 
		>[lengthList].
		>trace.
		>lengthList([1, 2, 3, 4, 5], N).

	- Command that need to execute problem 2 with trace information: 
		>[secondLastElement].
		>trace.
		>secondLastElement([1, 2, 3, 4, 5], N).

	- Command that need to execute problem 3 with trace information: 
		>[shuffle].
		>trace.
		>shuffle([1, 3, 5] , [2, 4, 6], N).

To exit need to press "clt+D"