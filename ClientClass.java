
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientClass {

    public Socket socket = null;
    public DataInputStream input = null;
    public DataOutputStream out = null;
    public String line = "hello world";
    public String className = "class-";

    public ClientClass(String address, int port) {


		/*
			// establish a connection

		*/


        try {
            socket = new Socket(address, port);

            System.out.println("Connected");

            // takes input from terminal
            input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            // sends output to the socket
            out = new DataOutputStream(socket.getOutputStream());
            Scanner sc = new Scanner(System.in);

        } catch (UnknownHostException u) {
            System.out.println(u);
        } catch (IOException i) {
            System.out.println(i);
        }

    }

    public void closeConnection() throws IOException {



		/*
			// close the connection

		*/

        System.out.println("closing connection server side");
//        out.writeUTF("Over");
        try {
            input.close();
            out.close();
            socket.close();
        } catch (IOException i) {
            System.out.println(i);
        }

    }


    public void receiveMessage() throws IOException
    {

        Object line = "hello";
        ObjectInputStream inputObject = new ObjectInputStream(new BufferedInputStream(socket
                .getInputStream()));
        while (!line.equals("Over"))
        {
            try
            {
                System.out.println("after receiving");

                line = inputObject.readObject();

                System.out.println(line);
                line="Over";

            }
            catch(IOException i)
            {
                System.out.println(i);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    public void sendMessage(String response) throws IOException
    {


        out.writeUTF(response);

        System.out.println("send Message");

        //out.writeUTF("Over");
    }




    public static void main(String args[]) throws IOException {

        ClientClass client = new ClientClass("127.0.0.1", 5000);

        Scanner sc = new Scanner(System.in);

        String input = sc.nextLine();

        System.out.println("input "+input);


        client.sendMessage(input);




        client.receiveMessage();

        client.sendMessage("logout");

       // client.closeConnection();


    }

}




/*
#String#m4#2#int#5#int#10#

#int#add#2#float#5.0f#int#10.34f#

*/

