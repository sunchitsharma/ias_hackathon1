import java.net.*;
import java.io.*;
import java.util.*;
import java.net.InetAddress;
class Client
{
    public static void main(String args[])throws Exception
    {
        String host="127.0.0.1";
        Socket socket = new Socket(InetAddress.getByName(host), 4999);
        DataInputStream input   = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        DataOutputStream out    = new DataOutputStream(socket.getOutputStream());
        String str2="void~~add~~2~~int##float##~~";
        out.writeUTF(str2);
    }
}