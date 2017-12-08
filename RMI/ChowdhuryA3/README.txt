- "SourceCode" folder contains total 4 files, are:
		1. ClientOperation.java  
		2. ServerOperation.java 
		3. RMIInterface.c  
		4. Makefile

The following steps must be involved in order for a RMI program to work properly:
1. Compile all java files by using the following commmand:
	$make
2. start the rmiregistry on the pc that contains "10.234.136.55" ip address. As our server binds with this IP.
	$make rmi
3. On the same machine ("10.234.136.55") open another console to start the RMI Server.
	$make server
4. Run the client program on other pc excluding the server pc by using the following command:
	$make client
5. To clean the all class file the command is:
	$make clean

	
- The loop will continue until the client doesn’t want to terminate. 
  In this application to quit client should provide 6 or any other integer number excluding 1 to 5. 
  The server will listen always, will never terminate. To force terminate server please press clt+c.