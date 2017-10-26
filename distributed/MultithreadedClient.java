import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.JOptionPane;

public class MultithreadedClient {
    private static InetAddress host;
    private static final int PORT = 1247;
    private static Socket link;
    private static BufferedReader in;
    private static PrintWriter out;
    //private static BufferedReader keyboard;

    public static void main(String[] args) throws IOException {

        int counter = 0;
        int loop_var = 1;

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

            //keyboard = new BufferedReader(new InputStreamReader(System.in));

            String response;            
            int flagS = 0, flagR = 1;

            do {
                Random rand = new Random();
                int i = rand.nextInt(100);
                int c = choice[i];

                if (c == 1) { //Send
                    counter++;
                    //System.out.println("Send");
                    if (flagR == 1){
                        System.out.println("Client Clock (B): " + counter + "\n");
                        out.println(Transfer.encrypt(counter));
                        flagS = 1;
                        flagR = 0;
                    }
                }

                else if (c == 2) { //Receive
                    counter++;
                    System.out.println("Receive");
                    if (flagS == 1){
                        response = "" + Transfer.decrypt(Integer.parseInt(in.readLine()));
                        //if (response != null) {
                        int offset = Integer.parseInt(response);
                        System.out.println("offset receive from Server = " + offset + "\n");
                        counter = offset + counter + 1;
                        System.out.println("Client Clock (A): " + counter + "\n");
                        
                        flagS = 0;
                        flagR = 1;
                    }
                }

                else { //Inner communication
                    System.out.println("Internal communication");
                    try {
                        counter++;
                        //thread to sleep for 1000 milliseconds
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
                loop_var++;
            } while (loop_var <= 50);
            //System.out.println("loop var = " +loop_var);
            counter = -1;
            out.println(Transfer.encrypt(counter) + "\n");
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