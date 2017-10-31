import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.JOptionPane;
/**
 * Trivial client for the date server.
 */

public class MultithreadedClient {
    /*
     * Runs the client as an application.  
     * First it will connect to the Server socket (IP: , PORT: 5370)
    */
    private static InetAddress host;
    private static final int PORT = 5370;
    private static Socket link;
    private static BufferedReader in;
    private static PrintWriter out;

    
    public static void main(String[] args) throws IOException {

        int counter = 1;    //it containts the PO local clock value
        int loop_var = 1;   //loop control variable

        /*
        * Displays 2 dialog boxes asking for the probablity for Send and Receive Event. From
        * these two inputs it will calculate the probablity for internal
        * event.
        */
        int probab_send = Integer.parseInt(JOptionPane.showInputDialog("Enter probablity for SEND: "));
        int probab_received = Integer.parseInt(JOptionPane.showInputDialog("Enter probablity for RECEIVE: "));
        int probab_internal = 100 - (probab_received + probab_send);
 
        if((probab_received + probab_send + probab_internal) != 100) {
            JOptionPane.showMessageDialog(null, "Invalid probability!!");
            return;
        }

        int choice[] = new int[100];
 
        for(int i = 0; i < probab_send; i++) {
            choice[i] = 1;
        }
        for(int i = probab_send; i < probab_send + probab_received; i++) {
            choice[i] = 2;
        }
        for(int i = probab_send + probab_received; i < 100; i++) {
            choice[i] = 3;
        }

        try {
            host = InetAddress.getLocalHost();
            link = new Socket(host, PORT);

            in = new BufferedReader(new InputStreamReader(link.getInputStream()));
            out = new PrintWriter(link.getOutputStream(), true);

            String response = "";
            String message = "";
            int flagS = 0, flagR = 1;

            //System.out.println("Client Clock (B): " + counter);
            out.println(Transfer.encrypt(counter)+"\n");
            do {
                Random rand = new Random();
                int i = rand.nextInt(100);
                int c = choice[i];

                if (c == 1) { //Send
                    counter++;
                    //System.out.println("Send");
                    response = in.readLine();
                    response = response.trim();
                    if((response.equals("")) ){
                        //System.out.println("Client Clock (B): " + counter);
                        System.out.println(+ counter);
                        out.println(Transfer.encrypt(counter)+"\n");
                    }
                    else {
                        message = response;
                        //System.out.println("Send: Response = "+ message);
                        flagS = 1;
                    }
                }

                else if (c == 2) { //Receive
                    counter++;
                    //System.out.println("Receive");
                    if (flagS == 1) {
                        //System.out.println("Receive: Response = "+ message); 
                        int offset = Transfer.decrypt(Integer.parseInt(message));
                        //System.out.println("Offset (S) = " + offset);
                        int temp_counter = offset + counter + 1;
                        if (temp_counter >= 0)
                            counter = temp_counter;
                        //System.out.println("Client Clock (A): " + counter + "\n\n");
                        System.out.println(+ counter);

                        flagS = 0;
                        flagR = 1;
                        response = "";
                    }
                }

                else { //Inner communication
                    //System.out.println("Internal communication");
                    try {
                        counter++;
                        //thread to sleep for 1000 milliseconds
                        Thread.sleep(100);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
                loop_var++;
            } while (loop_var <= 5000);
            //System.out.println("loop var = " +loop_var);
            counter = -1;
            out.println(Transfer.encrypt(counter));
        } catch (UnknownHostException uhEx) {
            System.out.println("\nHost ID not found!\n");
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        } finally {
            try {
                if (link != null) {
                    //System.out.println("loop var = " +loop_var);
                    System.out.println("Closing down connection...");
                    link.close();
                }
            } catch (IOException ioEx) {
                ioEx.printStackTrace();
            }
        }
    }
}

class Transfer {
    public static int encrypt(int item) {
        return item + 10;
    }

    public static int decrypt(int item) {
        return item - 10;
    }
}