* Inside this folder it contains Three floders:
   1. Client
   2. Server
   3. logFiles

* Each Server and Client folder contains the source file along with makefile.
   - To compile the file type > make
   - To run the file type > make run
   - To clean type > make clean

* The server IP is set to 10.234.136.55 and PORT allocate to 5370; Now if you try to run
   the source code in your localhost PC then you need to change the Client side code. 
   The line 66 and 67 will be then - 
   -------------------------------------
   66. host = InetAddress.getLocalHost();
   67. link = new Socket(host, PORT);
   -------------------------------------

* As here the server will be always true so whenevr you want to terminate please use clt+C

* To experiment with different probablities for events the GUI interface is provided for client and 
  server side both.

* The source code and the behavior is functioning properly; however when I am trying to run on remote PC
    then having error is: 
	" Exception in thread "main" java.awt.HeadlessException:
		No X11 DISPLAY variable was set, but this program performed an operation which requires it."
  
   As we have limited acces on these listed PC so I was unable to install proper packages it require; however I believe
   if you try this code on your PC it should work.      