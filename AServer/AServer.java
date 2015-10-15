import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Server extends JFrame{
   private JTextField enter;
   private JTextArea display;
   ObjectOutputStream output;
   ObjectInputStream input;

   public Server()
   {
      super("ZakirServer");

      Container c=getContentPane();

      enter=new JTextField();
      enter.setEnabled(false);
      enter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
               sendData(e.getActionCommand());
            }
      });
      c.add(enter, BorderLayout.SOUTH);
      display=new JTextArea();
      c.add(new JScrollPane(display), BorderLayout.CENTER);
      c.add(new Label("This is a Server program for sending and reciving strings, Zakir."), BorderLayout.NORTH);
      
      setSize(600, 300);
      show();
    }
    
    public void runServer()
    {
    	ServerSocket server;
    	Socket connection;
    	
    	int counter=1;
    	
    	try{
    		server=new ServerSocket(5000, 100);
    		while(true){
    			display.setText("Waiting for connection\n");
    			connection = server.accept();
    			display.append("Connection "+counter+" received from: "+connection.getInetAddress().getHostName());
    			output = new ObjectOutputStream(connection.getOutputStream());
    			output.flush();
    			input = new ObjectInputStream(connection.getInputStream());
    			display.append( "\nConnection Successful\n" );
    			String message = "Server Says:) Connection successful";
    			output.writeObject(message);
    			output.flush();
    			enter.setEnabled(true);
    			
    			do{
    				try{
    					message = (String) input.readObject();
    					display.append( "\n" + message );
    					display.setCaretPosition(display.getText().length());
    				}
    				catch(ClassNotFoundException cnfex){
    					display.append("\nUnknown object type received");
    				}
    			}while(!message.equals("Client is terminating the connection"));
    			
    			display.append( "\nUser terminated connection" );
    			enter.setEnabled( false );
    			output.close();
    			input.close();
    			connection.close();
    			++counter;
    		}
    	}
    	catch(EOFException eof){
    		System.out.println("Client terminated connection");
    	}
    	catch(IOException io){
    		io.printStackTrace();
    	}
    }
    
    private void sendData(String s)
    {
    	try{
    		output.writeObject("Server Says:) "+s);
    		output.flush();
    		display.append("\nServer Says:)"+s);
    	}
    	catch(IOException cnfex){
    		display.append("\nError writing object");
    	}
    }
}

public class AServer extends JApplet
{
	public void init()
	{
		Server se=new Server();
		se.runServer();
	}
}