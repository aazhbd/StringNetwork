import java.io.*;

import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.*;

public class MultithreadedServer {
    Vector<ClientHandler> clients = new Vector<ClientHandler>();
    Vector<String> users = new Vector<String>();
    private static ServerSocket servSocket;
    private static final int PORT = 1247;

    public MultithreadedServer() throws IOException {
        servSocket = new ServerSocket(PORT);
        while (true) {

            Socket client = servSocket.accept();
            System.out.println("\nNew client accepted.\n");
            client.setSoTimeout(50000);

            ClientHandler handler;
            handler = new ClientHandler(client);
            clients.add(handler);
        }
    }

    public static void main(String[] args) throws IOException {
        MultithreadedServer ms = new MultithreadedServer();
    }

    class ClientHandler extends Thread {

        private Socket client;
        private BufferedReader in;
        private PrintWriter out;
        String name, message, response;
        HashMap<String, Integer> values;
        HashMap<String, ClockValues> data;

        public ClientHandler(Socket socket) {
            this.values = new HashMap<String, Integer>();
            this.values.put("SeverValue", 0);

            data = new HashMap<String, ClockValues>();

            this.data.put("Thread-0", new ClockValues(0, 0));
            this.data.put("Thread-1", new ClockValues(0, 0));
            this.data.put("Thread-2", new ClockValues(0, 0));
            this.data.put("Thread-3", new ClockValues(0, 0));
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
                    int i = rand.nextInt(10);
                    int choice[] = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
                    int ch = choice[i];

                    if (ch == 5 || ch == 6) { //Receive
                        /*START: server clock increment by 1 */
                        t_clk = Integer.parseInt("" + this.values.get("SeverValue")) + 1;
                        this.values.put("SeverValue", t_clk);
                        /*END: server clock increment by 1 */

                        System.out.println("Receive");
                        if (flagS == 1) {
                            line = "" + Transfer.decrypt(Integer.parseInt(in.readLine()));
                            n = Integer.parseInt(line);
                            if (n == -1) {
                                break;
                            }

                            System.out.println("Received " + line + " from Connection " + this.getName() + ".");
                            this.data.put(this.getName(), new ClockValues(n, 0));
                            flagR = 1;
                            flagS = 0;
                        }
                    }

                    else if (ch == 1 || ch == 2 || ch == 3 || ch == 4) { //Send
                        /*START: server clock increment by 1 */
                        t_clk = Integer.parseInt("" + this.values.get("SeverValue")) + 1;
                        this.values.put("SeverValue", t_clk);
                        /*END: server clock increment by 1 */

                        System.out.println("Send");

                        if (flagI == 1) {
                            System.out.println("Internal Send");
                            for (ClientHandler c : clients) {
                                preClock = this.data.get(c.getName()).getClock();
                                offset = avg - preClock;
                                System.out.println("offset SEND from Server = " + offset + "\n");
                                c.sendMessage("" + offset);
                                int current_clk = this.data.get(c.getName()).getClock();
                                this.data.put(c.getName(), new ClockValues(current_clk, offset));
                            }
                            flagI = 0;
                            flagS = 1;
                        }
                    }

                    else { //Internal communication
                        System.out.println("Internal communication");

                        if (flagR == 1) {
                            hSize = 4;// total num of POs
                            sum = Integer.parseInt("" + this.values.get("SeverValue"));
                            System.out.println("Server Clock (B): " + this.values.get("SeverValue"));

                            for (Map.Entry m : this.data.entrySet()) {
                                ClockValues tmpclock = (ClockValues) m.getValue();
                                sum += Integer.parseInt("" + tmpclock.getClock());
                            }
                            avg = sum / (hSize + 1);
                            this.values.put("SeverValue", avg);
                            
                            System.out.println("Server Clock (A): " + this.values.get("SeverValue"));
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
                        System.out.println("Closing down connection...");
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
