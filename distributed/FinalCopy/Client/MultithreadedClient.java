import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.JOptionPane;

/*
 * Client (PO) side code for a system that maintains 
 * Clock consistency, associated drifts, inter-process communication 
 * and the end-to-end argument 
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
        /*
        * Generating Choice array that contains 1, 2, and 3 only where;
        * 1 indicate SEND event
        * 2 indicate RECEIVE event
        * 3 indicate Internal Communication Event
        */
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
            /* 
            * Create connection to MO and 
            * initialize input and output streams
            */
            host = InetAddress.getLocalHost();
            link = new Socket(host, PORT);
            in = new BufferedReader(new InputStreamReader(link.getInputStream()));
            out = new PrintWriter(link.getOutputStream(), true);

            String response = "";
            String message = "";
            
            int flagS = 0, flagR = 1;   // these flags are used to sync SEND and RECIEVE events within a PO

            /* Initial local clk value SEND to MO after encryption*/
            out.println(Transfer.encrypt(counter)+"\n");    
            do {
                /*
                * Random selection within 3 events take place here!
                */
                Random rand = new Random();
                int i = rand.nextInt(100);
                int c = choice[i];

                /* SEND Event*/
                if (c == 1) { 
                    counter++;
                    /*
                    * Read from input stream buffer using readLine() until it gets 
                    * a new line (\n) character.
                    * Now if there is any input waiting for recieve then SEND
                    * operation will not take place here. 
                    * Otherwise PO's current local value will SEND to MO.
                    */
                    response = in.readLine();
                    response = response.trim();
                    if((response.equals("")) ){
                        System.out.println(+ counter);
                        out.println(Transfer.encrypt(counter)+"\n");
                    }
                    else {
                        message = response;
                        flagS = 1;
                    }
                }
                
                /* RECEIVE Event*/
                else if (c == 2) {
                    counter++;
                    if (flagS == 1) {
                        /* Offset value decrypted here */
                        int offset = Transfer.decrypt(Integer.parseInt(message));

                        /*
                        * Byzantine failure is introduced here!!
                        * If randomly pick number is 33 then set offset value to 0;
                        * Again if the number is 66 then add additional 100 with the received offset value
                        */
                        if (i == 33){
                            offset = 0;
                        }
                        if (i == 66){
                            offset = offset + 100;
                        }
                        
                        /* Received offset value from MO adjust here to sync the PO's local CLK*/
                        int temp_counter = offset + counter + 1; 
                        if (temp_counter >= 0){
                            counter = temp_counter;
                        }
                        System.out.println(+ counter);

                        flagS = 0;
                        flagR = 1;
                        response = "";
                    }
                }

                /* Internal Communication Event*/
                else {
                    try {
                        counter++;
                        /*thread to sleep for 100 milliseconds*/
                        Thread.sleep(100);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
                loop_var++;
            } while (loop_var <= 10000);    //loop will continue for 10,000 times
            
            /* 
            *Once PO done its execution then it will set the local CLK value to -1
            * and SEND it to the MO as a notofication that its terminating.
            */
            counter = -1;
            out.println(Transfer.encrypt(counter));
        } catch (UnknownHostException uhEx) {
            System.out.println("\nHost ID not found!\n");
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        } finally {
            try {
                if (link != null) {
                    System.out.println("Closing down connection...");
                    link.close();
                }
            } catch (IOException ioEx) {
                ioEx.printStackTrace();
            }
        }
    }
}

/* Encypt and Decrypt mechanism Implemented here */
class Transfer {
    public static int encrypt(int item) {
        return item + 10;
    }

    public static int decrypt(int item) {
        return item - 10;
    }
}