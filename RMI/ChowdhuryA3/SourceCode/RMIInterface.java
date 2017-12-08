import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIInterface extends Remote {
    public String workingDir() throws RemoteException;
    public int[] sortArray(int[] a) throws RemoteException;
    public String echoMsg(String msg) throws RemoteException;
    public String fileCheck(String fileName) throws RemoteException;
    public int[] matMul(int[] matA, int[] matB, int rA, int cA, int cB) throws RemoteException;
}