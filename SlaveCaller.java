import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class SlaveCaller extends Thread {
    public int slavePort;
    String str;
    Object[] parameters;
    Socket clientSocket;
    public SlaveCaller(int i, String str, Object[] parameters, Socket clientSocket) {
        this.slavePort = i;
        this.str = str;
        this.parameters = parameters;
        this.clientSocket = clientSocket;
    }

    public void run(){
        try {
            Socket socket = new Socket(Main.slave2ip.get(slavePort-5002), slavePort);
            DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
            dout.writeUTF(str);
            ObjectOutputStream objout = new ObjectOutputStream(socket.getOutputStream());
            objout.writeObject(parameters);
            Object response;
            ObjectOutputStream doutClient = new ObjectOutputStream(clientSocket.getOutputStream
                    ());
            ObjectInputStream objin = new ObjectInputStream(socket.getInputStream());
            response = objin.readObject();


            String type = str.split("~~")[1];
            switch(type)
            {
                case "int":
                    doutClient.writeObject((int)response);
                    break;
                case "float":
                    doutClient.writeObject((float)response);
                    break;
                case "char":
                    char c;
                    c = response.toString().charAt(0);
                    doutClient.writeObject(c);
                    break;
                case "double" :
                    doutClient.writeObject((double)response);
                    break;
                default:
                    doutClient.writeObject((String)response);;
                    break;
            }

            dout.flush();
//            dout.close();
            objout.flush();
//            objout.close();
//            socket.close();
//            doutClient.close();
        }catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }

    }


}
