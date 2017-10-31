import java.io.*;

import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.*;

/*
 * A TCP server (MO) that runs on port 5370;
 * When a client connects it receives the PO's local CLK value
 * and calculate the average of the all PO's and MO's local value.
 * Finally broadcast the corresponding offset to all POs that 
 * require to adjust their local clk value to sync with global clk.
 */


public class MultithreadedServer {
    Vector<ClientHandler> clients = new Vector<ClientHandler>();
    Vector<String> users = new Vector<String>();
    private static ServerSocket servSocket;
    private static final int PORT = 5370;
    int[] choice;

    public MultithreadedServer(int[] ch) throws IOException {
        this.choice = ch;
        servSocket = new ServerSocket(PORT);
        HashMap<String, Integer> values = new HashMap<String, Integer>();
        HashMap<String, ClockValues> data = new HashMap<String, ClockValues>();

        /*Entities that are going to be use each client theread are Initialized here*/
        values.put("SeverValue", 0);
        data.put("Thread-0", new ClockValues(0, 0, 0));
        data.put("Thread-1", new ClockValues(0, 0, 0));
        data.put("Thread-2", new ClockValues(0, 0, 0));
        data.put("Thread-3", new ClockValues(0, 0, 0));
   
        while (true) {
            /*New client accepted*/
            Socket client = servSocket.accept();
            ClientHandler handler;
            handler = new ClientHandler(client, values, data);
            clients.add(handler);
        }
    }

    public static void main(String[] args) throws IOException {
        /*
        * Pass two command line parameters for the SEND and RECEIVE events
        */
        int probab_send = 0, probab_received = 0;

        try {
            probab_send = Integer.parseInt(args[0]);
            probab_received = Integer.parseInt(args[1]);    
        }
        catch(Exception e) {
            System.out.println("Enter command line parameters for probability values.");
        }

        int probab_internal = 100 - (probab_received + probab_send);

        if ((probab_received + probab_send + probab_internal) != 100) {
            System.out.println("Invalid probability!!");
            return;
        }

        /*
        * Generating Choice array that contains 1, 2, and 3 only where;
        * 1 indicate SEND event
        * 2 indicate RECEIVE event
        * 3 indicate Internal Communication Event
        */
        
        int choice[] = new int[100];

        for (int i = 0; i < probab_send; i++) {
            choice[i] = 1;
        }
        for (int i = probab_send; i < probab_send + probab_received; i++) {
            choice[i] = 2;
        }
        for (int i = probab_send + probab_received; i < 100; i++) {
            choice[i] = 3;
        }
        MultithreadedServer ms = new MultithreadedServer(choice);
    }

    class ClientHandler extends Thread {
        private Socket client;
        private BufferedReader in;
        private PrintWriter out;
        String name, message, response;
        HashMap<String, Integer> values;
        HashMap<String, ClockValues> data;

        public ClientHandler(Socket socket, HashMap<String, Integer> values, HashMap<String, ClockValues> data) {
            this.values = values;
            this.data = data;

            client = socket;
            try {
                /* Initialize input and output streams*/
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out = new PrintWriter(client.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            start();
        }

        public void sendMessage(String msg) {
            int offset = Transfer.encrypt(Integer.parseInt(msg));
            out.println(offset + "\n");
        }

        public void boradcast(String message) {
            for (ClientHandler c : clients) {
                c.sendMessage(message);
            }
        }

        public String getUserName() {
            return name;
        }

        public void run() {
            String line;
            int hSize, sum, avg = 0, t_clk = 0, n;
            int offset = 0, preClock = 0, preOldClock = 0;
            try {
                int flagR = 0, flagS = 1;   // these flags are used to sync SEND and RECIEVE events within a PO
                String message = "";
                String received = "";
                received = in.readLine();   //read  is there any entry waiting in input buffer
                
                do {
                    /*
                    * Random selection within 3 events take place here!
                    */
                    Random rand = new Random();
                    int i = rand.nextInt(100);
                    int ch = choice[i];
    
                    /* SEND Event*/
                    if (ch == 1) {
                        /*Server clock value update here by 1*/
                        t_clk = Integer.parseInt("" + this.values.get("SeverValue")) + 1;
                        this.values.put("SeverValue", t_clk);

    
                        if (flagR == 1){
                            hSize = 4;      //total num of POs
                            sum = Integer.parseInt("" + this.values.get("SeverValue"));
                            System.out.println(+ this.values.get("SeverValue"));

                            /*
                            * Read Input buffer stream
                            * If data is waiting buffer then don't SEND anything
                            * otherwise find the average of the 4 POs and MOs clock value
                            * and Broadcast to the all POs.
                            */
                            message = in.readLine();
                            message = message.trim();
                            if((message.equals("")) ){
                                /*Sum all the PO's clock values including the MO's value */
                                for (Map.Entry m : this.data.entrySet()) {
                                    /*tmpclock contains 3 tupple are: clock, offset, oldValue*/
                                    ClockValues tmpclock = (ClockValues) m.getValue();
                                    sum += Integer.parseInt("" + tmpclock.getClock());
                                }
                                avg = sum / (hSize + 1);
                                /* Update the MO clock value by the average */
                                this.values.put("SeverValue", avg);
                                System.out.println(+ this.values.get("SeverValue"));

                                /* Calculate the offset and Send offset to all POs */
                                for (ClientHandler c : clients) {
                                    preClock = this.data.get(c.getName()).getClock();
                                    preOldClock = this.data.get(c.getName()).getOldValue();
                                    if (c.getName() == this.getName()) {
                                        offset = avg - preClock;
                                    } else {
                                        offset = avg - preOldClock;
                                    }

                                    int offset_s = this.data.get(c.getName()).getOffset();
                                    c.sendMessage("" + offset_s);
                                } 
                            }   //if((message.equals("")))
                            else {
                                received = message;
                                flagS = 1;
                                flagR = 0;
                            }
                        }                        
                    }
                    
                    /* RECEIVE Event*/
                    else if (ch == 2) {
                        /*Server clock value update here by 1*/
                        t_clk = Integer.parseInt("" + this.values.get("SeverValue")) + 1;
                        this.values.put("SeverValue", t_clk);

                        if (flagS == 1) {
                            /* Received clock value decrypted here */
                            line = "" + Transfer.decrypt(Integer.parseInt(received));
                            n = Integer.parseInt(line);
                            
                            /* PO send -1 to MO once it terminate */
                            if (n == -1) {
                                break;
                            }
                            this.data.put(this.getName(), new ClockValues(n, 0, n));
                            
                                flagR = 1;
                                flagS = 0;
                        }
                    }                   

                    /* Internal Communication Event*/
                    else {
                        try {
                            /*Server clock value update here by 1*/
                            t_clk = Integer.parseInt("" + this.values.get("SeverValue")) + 1;
                            this.values.put("SeverValue", t_clk);
                            /*thread to sleep for 100 milliseconds*/  
                            Thread.sleep(100);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                } while (true);
            } catch (

            IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (client != null) {
                        client.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

/*
* It contains current clock value, offset and old clock value of a PA
* Passed as a parameter in haHashMap<String, ClockValues>
*/
class ClockValues {
    public int clock;
    public int offset;
    public int oldValue;

    public ClockValues(int clock, int offset, int oldValue) {
        this.clock = clock;
        this.offset = offset;
        this.oldValue = oldValue;
    }

    public int getOldValue() {
        return this.oldValue;
    }

    public void setOldValue(int oldvalue) {
        this.oldValue = oldvalue;
    }

    public int getClock() {
        return this.clock;
    }

    public void setClock(int clock) {
        this.clock = clock;
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String show() {
        return "" + this.clock + " " + this.offset + " " + this.oldValue;
    }
}
