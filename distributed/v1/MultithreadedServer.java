import java.io.*;

import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.*;

import javax.swing.JOptionPane;

public class MultithreadedServer {
    Vector<ClientHandler> clients = new Vector<ClientHandler>();
    Vector<String> users = new Vector<String>();
    private static ServerSocket servSocket;
    private static final int PORT = 1247;
    int[] choice;

    public MultithreadedServer(int[] ch) throws IOException {
        this.choice = ch;
        servSocket = new ServerSocket(PORT);
        HashMap<String, Integer> values = new HashMap<String, Integer>();
        HashMap<String, ClockValues> data = new HashMap<String, ClockValues>();
        values.put("SeverValue", 0);

        data.put("Thread-0", new ClockValues(0, 0));
        data.put("Thread-1", new ClockValues(0, 0));
        data.put("Thread-2", new ClockValues(0, 0));
        data.put("Thread-3", new ClockValues(0, 0));

        while (true) {

            Socket client = servSocket.accept();
            System.out.println("\n********************************New client accepted.*****************************\n");
            client.setSoTimeout(50000);

            ClientHandler handler;
            handler = new ClientHandler(client, values, data);
            clients.add(handler);
        }
    }

    public static void main(String[] args) throws IOException {
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
            //this.data.put("5", new ClockValues(0, 0));

            client = socket;
            try {
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out = new PrintWriter(client.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            start();
        }

        public void sendMessage(String msg) {
            out.println(Transfer.encrypt(Integer.parseInt(msg)));
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
            int offset = 0, preClock = 0;
            try {
                String received;
                int flagR = 0, flagI = 0, flagS = 1;
                do {
                    Random rand = new Random();
                    int i = rand.nextInt(100);
                    int ch = choice[i];

                    //for(i = 0; i < 100; i++) System.out.print(choice[i] + " ");

                    if (ch == 2) { //Receive
                        /*START: server clock increment by 1 */
                        t_clk = Integer.parseInt("" + this.values.get("SeverValue")) + 1;
                        this.values.put("SeverValue", t_clk);
                        /*END: server clock increment by 1 */

                        System.out.println("Receive"+this.getName() + "\n");
                        if (flagS == 1) {
                            line = "" + Transfer.decrypt(Integer.parseInt(in.readLine()));
                            n = Integer.parseInt(line);
                            if (n == -1) {
                                break;
                            }

                            System.out.println("Received " + line + " from Connection " + this.getName() + ".");

                            //int current_offset = this.data.get(this.getName()).getOffset();                            
                            this.data.put(this.getName(), new ClockValues(n, 0));
                            flagR = 1;
                            flagS = 0;
                        }
                    }

                    else if (ch == 1) { //Send
                        /*START: server clock increment by 1 */
                        t_clk = Integer.parseInt("" + this.values.get("SeverValue")) + 1;
                        this.values.put("SeverValue", t_clk);
                        /*END: server clock increment by 1 */

                        System.out.println("Send: " +this.getName() + "\n");

                        if (flagI == 1) {
                            //System.out.println("Internal Send");
                            for (ClientHandler c : clients) {
                                //preClock = this.data.get(c.getName()).getClock();
                                offset_s = this.data.get(c.getName()).getOffset();
                                c.sendMessage("" +offset_s);
                            }
                            flagI = 0;
                            flagS = 1;
                        }
                    }

                    else { //Internal communication
                        System.out.println("Internal communication" +this.getName() + "\n");

                        if (flagR == 1) {
                            hSize = 4;// total num of POs
                            sum = Integer.parseInt("" + this.values.get("SeverValue"));
                            System.out.println("Server Clock (B): " + this.values.get("SeverValue"));

                            for (Map.Entry m : this.data.entrySet()) {
                                ClockValues tmpclock = (ClockValues) m.getValue();
                                System.out.println("Thread Name : " + m.getKey() + " Clock : " + tmpclock.getClock());
                                sum += Integer.parseInt("" + tmpclock.getClock());
                            }
                            avg = sum / (hSize + 1);
                            this.values.put("SeverValue", avg);
                            
                            System.out.println("Server Clock (A): " + this.values.get("SeverValue"));

                            for (ClientHandler c : clients) {
                                preClock = this.data.get(c.getName()).getClock();
                                offset = avg - preClock;
                                //System.out.println("Server send to " +c.getName() + avg + " - " +preClock + "=" +offset+ "\n");
                                int current_clk = this.data.get(c.getName()).getClock();
                                this.data.put(c.getName(), new ClockValues(current_clk, offset));
                            }

                            flagI = 1;
                            flagR = 0;
                        } else {
                            try {
                                t_clk = Integer.parseInt("" + this.values.get("SeverValue")) + 1;
                                this.values.put("SeverValue", t_clk);
                                //thread to sleep for 1000 milliseconds
                                Thread.sleep(1000);
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                        }
                    }
                } while (true);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (client != null) {
                        System.out.println("********************Closing down connection...***********************");
                        client.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

class ClockValues {
    public int clock;
    public int offset;

    public ClockValues(int clock, int offset) {
        this.clock = clock;
        this.offset = offset;
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
        return "" + this.clock + " " + this.offset;
    }
}
