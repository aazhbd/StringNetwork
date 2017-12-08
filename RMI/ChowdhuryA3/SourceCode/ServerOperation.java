import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.io.*;

public class ServerOperation extends UnicastRemoteObject implements RMIInterface {

    private static final long serialVersionUID = 1L;

    protected ServerOperation() throws RemoteException {

        super();

    }

	/*matMul: is a method
	*Input: three int elements and two int arrays
	*Output: interger array
	*Operation: will return the multiplication value of two matrices
	*Matrix multiplication - using 1D array
	*/
    @Override
    public int[] matMul(int[] matA, int[] matB, int rA, int cA, int cB) throws RemoteException {
        int matC[] = new int[rA*cB];
        for (int i = 0; i < rA; i++) {
            for (int j = 0; j < cB; j++) {
                int sum = 0;
                for (int k = 0; k < cA; k++) {
                    sum = sum + matA[i * cA + k] * matB[k * cB + j];
                }
                matC[i * cB + j] = sum;
            }
        }
        return matC;
    }
	
	/*fileCheck: is a method
	*Input: string
	*Output: string
	*Operation: will return the file status to client (File exists/Doesn't exixts)
	*/
    @Override
    public String fileCheck(String fileName) throws RemoteException {
        String status;
        File tmpDir = new File(fileName);
        boolean exists = tmpDir.exists();
        if (exists)
            status = "File Exists";
        else
            status = "File Doesn't Exixts";
        return status;
    }
	
	/*echoMsg: is a method
	*Input: string
	*Output: string
	*Operation: will return the same message that recieved from client
	*/
    @Override
    public String echoMsg(String msg) throws RemoteException {
        return msg;
    }

	/*sortArray: is a method
	*Input: integer array
	*Output: integer array
	*Operation: will return sorted array
	*/
    @Override
    public int[] sortArray(int[] a) throws RemoteException {
        Arrays.sort(a);
        //System.out.printf("Modified arr[] : %s", Arrays.toString(a));
        return a;
    }
	
	/*workingDir: is a method
	*Input: void
	*Output: string
	*Operation: will return the present working directory
	*/
    @Override
    public String workingDir() throws RemoteException {
        String s;
        Process p;
        String[] result = new String[10];
        int inx = 0;
        try {
            p = Runtime.getRuntime().exec("pwd");
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null) {
                result[inx] = s;
                inx++;
            }
            p.waitFor();
            p.destroy();
        } catch (Exception e) {
            //System.out.println(e.getMessage());
        }

        System.out.println(result[0]);
        return result[0];
    }

    public static void main(String[] args) {

        try {

            Naming.rebind("//10.234.136.55/MyServer", new ServerOperation());
            System.err.println("Server ready");

        } catch (Exception e) {

            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }

    }

}