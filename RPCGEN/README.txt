- In "Assignment2" folder it contains total 8 files, are:
		1. Makefile.rpc  - Contains most basic capabilities of make
		2. rpc_clnt.c - Cliennt side Stub
		3. rpc_server.c  - code to generate remote procedures on the Server side
		4. rpc.x - Interface deinition      
		5. rpc_client.c  - code to generate remote procedures on the Client side
		6. rpc.h - Header file       
		7. rpc_svc.c - Server side Stub    
		8. rpc_xdr.c - XDR routines

- Intially you need to create file, in this case "rpc.x" which will have the interface definition.
- Then you have to compile the "rpc.x" file using "$rpcgen -C -a rpc.x" command which will create the all necessary files (such as stubs, headers and others)
- Once the compilation dones you will see there are 7 more files get generated within the same folder (mentioned above).

- Now to implement the remote procedure on client side and server side need to add the functionalities to "rpc_client.c" and "rpc_server.c"
- Once the remote procedure is successfully developed then need to compile using the following command: 
	"$make -f Makefile.rpc" ; will create the execution files for both client and server.

- To run the server the command is "./rpc_server"
- To run the client the command is "./rpc_client server_ip_address" for example if server is running on 10.234.136.55 ip address then the command will be:
	"$./rpc_client 10.234.136.55"

- To terminate from client side user should provide ‘q’ or any other character excluding ‘p’, ‘s’, ‘e’, ‘c’, and ‘m’. 
- As server will never terminate so to do forceful termination hit "clt+c".

NOTE: there is a "backup" folder created where it contains all the files; so that for any chance if the file get overwritten then you will get the original files there.