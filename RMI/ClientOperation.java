import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.io.*;

public class ClientOperation {
	private static RMIInterface look_up;

	static String ReadLn (int maxLg)  // utility function to read from stdin
    {
        byte lin[] = new byte [maxLg];
        int lg = 0, car = -1;
        String line = "";

        try
        {
            while (lg < maxLg)
            {
                car = System.in.read();
                if ((car < 0) || (car == '\n')) break;
                lin [lg++] += car;
            }
        }
        catch (IOException e)
        {
            return (null);
        }

        if ((car < 0) && (lg == 0)) return (null);  // eof
        return (new String (lin, 0, lg));
    }

	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {

		look_up = (RMIInterface) Naming.lookup("//10.234.136.55/MyServer");
	
		/*
		*This while loop will only terminate when user will provide any other input rather that 1 - 5
		*otherwise for anyother choice it will send request to the server and server will execute 
		*the corresponding functionalities.
		*/

		while (true) {
			System.out.println(
					"Press\n1 PWD\n2 Sort\n3 File Check\n4 Echo\n5 Matrix Multiplication\n6 or others to QUIT\n");
			
			System.out.print("Enter choice : ");
			int choice = Integer.parseInt(ClientOperation.ReadLn(100));

			/*
			*If this condition is true then it will request server for the working directory
			*and once receive the response will print the information
			*/
			if (choice == 1) {
				long startTime = System.currentTimeMillis();
				String response = look_up.workingDir();
				long stopTime = System.currentTimeMillis();
				System.out.println("SERVER Response: " +response);
				long elapsedTime = (stopTime - startTime);
				System.out.println("Execution Time (ms): " +elapsedTime+ "\n");
			}

			/*
	                *If this condition is true then it will send an unsorted array to server
	                *array input will be given by the user 
			*Server will return the sorted array to client
	                */
			else if (choice == 2) {
				System.out.println("Enter the array size: ");
				int size = Integer.parseInt(ClientOperation.ReadLn(1000));
				
				int[] arr = new int[size];
				int i;
			
				for(i = 0 ; i <size; i++) {
					int choice2 = Integer.parseInt(ClientOperation.ReadLn(100));
					arr[i] = choice2;
				}
				long startTime = System.currentTimeMillis();
				int[] response = look_up.sortArray(arr);
				long stopTime = System.currentTimeMillis();
				System.out.println("SERVER Response: " +Arrays.toString(response));
				long elapsedTime = (stopTime - startTime);
				System.out.println("Execution Time (ms): " +elapsedTime+ "\n");
			}

			/*
	                *If this condition is true then it will pass a file name to server
        	        *File name will be user given input
			*Server will reply based on the file status (exists or not)
	                */
			else if (choice == 3) {
				System.out.print("Enter the File Name: ");
				String fileName = ClientOperation.ReadLn(100);
				long startTime = System.currentTimeMillis();
				String response = look_up.fileCheck(fileName);
				long stopTime = System.currentTimeMillis();			
				System.out.println("SERVER Response: " +response);
				long elapsedTime = (stopTime - startTime);
				System.out.println("Execution Time (ms): " +elapsedTime+ "\n");
			}

			/*
	                *If this condition is true then it will send a message to server
        	        *message will be user given input
			*Server will reply the same message to client again
        	        */
			else if (choice == 4) {
				System.out.print("Enter your mesage here: ");
				String msg = ClientOperation.ReadLn(1000);
				long startTime = System.currentTimeMillis();
				String response = look_up.echoMsg(msg);
				long stopTime = System.currentTimeMillis();
				System.out.println("SERVER Response: " +response);
				long elapsedTime = (stopTime - startTime);
				System.out.println("Execution Time (ms): " +elapsedTime+ "\n");
			}

			/*
	                *If this condition is true then it will pass two matrices to server
	                *Both input matrices value will be user given inputs
			*In return server will reply the multiplicattion value of given matrices
                	*/
			else if (choice == 5) {
				System.out.print("Provide 1st Matrix Row and Column value: ");
				int r1 = Integer.parseInt(ClientOperation.ReadLn(100));
				int c1 = Integer.parseInt(ClientOperation.ReadLn(100));
				int[] matA = new int[r1*c1];

				int i, j=0, k=0;
				for (i = 0; i < r1*c1; i++){
					System.out.print("matA["+j+"]["+k+"]=");
					matA[i] = Integer.parseInt(ClientOperation.ReadLn(100));
					
					if(k == c1 - 1){
						k=0;
						j++;
					}
					else{
						k++;
					}
				}
				
				System.out.print("Provide 2nd Matrix Row and Column value (row2 should be equal to column 1) :");
				int r2 = Integer.parseInt(ClientOperation.ReadLn(100));
				int c2 = Integer.parseInt(ClientOperation.ReadLn(100));			
				int[] matB = new int[r2*c2];
				j=0; k=0;
				for (i = 0; i < r2*c2; i++){
					System.out.print("matB["+j+"]["+k+"]=");
					matB[i] = Integer.parseInt(ClientOperation.ReadLn(100));
					
					if(k == c1 - 1){
						k=0;
						j++;
					}
					else{
						k++;
					}
				}

				long startTime = System.currentTimeMillis();
				int[] response = look_up.matMul(matA, matB, r1, c1, c2);
				long stopTime = System.currentTimeMillis();
				
				System.out.println("SERVER Response: ");
				int[] matC = new int[r1*c2];
				int temp =c2;
				for(i = 0; i< r1 * c2; i++){
					matC[i] = response[i];
					if (i == temp){
						temp = temp + c2;
						System.out.println();
					}	
					System.out.print("\t" +matC[i]);
				}
				long elapsedTime = (stopTime - startTime);
				System.out.println("\nExecution Time (ms): " +elapsedTime+ "\n");
			} 
			
			/*For any other character input by user will terminate the loop*/
			else {
				System.out.println("Closing Connection ...\n");
				break;
			}
		} //while (1)
	}

}