* Inside this folder it contains Three folders:
   1. Client
   2. Server
   3. logFiles	

* Each Server and Client folder contains the source file along with the "makefile".
   - To compile the file type $ make
   - To run the file type $ make run send="40" rec="40"
   - To clean type $ make clean

* With "make run" command for both Client and Server side you need to pass the probablity values for 
  SEND and RECEIVE event as an argument. Sample command is given below:
  
	$  make run send="40" rec="40"


* The server IP is set to "10.234.136.55" and PORT allocate to "5370"; Now if you try to run
   the source code in your localhost PC then you need to change the Client side code. 
   The line 66 and 67 will be then - 
   -------------------------------------
   71. host = InetAddress.getLocalHost();
   72. link = new Socket(host, PORT);
   -------------------------------------

* Client will also communicate through PORT "5370".

* Server will continue to listen for client requests until it is terminated by CTRL+C.

* logFiles folder contains the logical clock value for each POs and MO for 10000 iterrations.
  Three different cases are covered: 1. without synchronization, 
				     2. equal probablities of SEND and RECEIVe event with synchronization,
				     3. low probablities RECEIVE event with synchronization .