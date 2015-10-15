import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Client extends JFrame
{
   private JTextField enter;
   private JTextArea display;
   ObjectOutputStream output;
   ObjectInputStream input;
   String message = "";

   public Client()
   {
      super("ZakirClient");
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
      display = new JTextArea();
      c.add(new JScrollPane(display), BorderLayout.CENTER);
      c.add(new JLabel(" This is a client program to send and recive strings, Zakir."), BorderLayout.NORTH);
      setSize(600, 300);
      show();
   }

   public void runClient() 
   {
      String ipadd;
      ipadd=JOptionPane.showInputDialog("Enter IP address: ");
      Socket client;
      try{
      	display.setText( "Attempting connection\n" );
      	client = new Socket(InetAddress.getByName(""+ipadd), 5000);
      	display.append("Connected to:"+client.getInetAddress().getHostName());
      	output = new ObjectOutputStream(client.getOutputStream());
      	output.flush();
      	input = new ObjectInputStream(client.getInputStream());
      	display.append("\nGot I/O streams\n");
      	enter.setEnabled( true );
      	
      	do{
      		try{
      			message = (String) input.readObject();
      			display.append( "\n" + message );
      			display.setCaretPosition(display.getText().length());
      		}
      		catch(ClassNotFoundException cnfex){
      			display.append("\nUnknown object type received");
      		}
      	}while(!message.equals("Server Says:)"));
      	
      	display.append("Closing Connection.\n");
      	output.close();
      	input.close();
      	client.close();
      	}
      	catch(EOFException eof){
      		System.out.println("Server terminated connection");
      	}
      	catch(IOException e){
      		e.printStackTrace();
      	}
   }
   
   private void sendData(String s)
   {
   	try{
   		message = s;
   		output.writeObject("Client Says:)"+s);
   		output.flush();
   		display.append("\nClient Says:)"+s);
   	}
   	catch(IOException cnfex){
   		display.append("\nError writing object");
   	}
   }
}

public class AClient extends JApplet
{
	public void init()
	{
		Client c=new Client();
//		String name;
//		name=JOptionPane.showInputDialog("Please Enter your number: ");
//		int na=Integer.parseInt(name);
//		if(na==34)
//		{
			c.runClient();
//		}
//		else
//		{
//			JOptionPane.showMessageDialog(null, "Invalid Name");
//		}
	}
}