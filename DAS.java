import java.net.*;
import java.io.*;
import java.util.*;

class DAS extends Thread
{
    public void run()
    {
        try
        {
            // Displaying the thread that is running
            //System.out.println ("Thread " +Thread.currentThread().getId() +" is running");
            ServerSocket server =  new ServerSocket(4999);
            while(true)
            {
                Socket socket = server.accept();
                DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                String h=in.readUTF();
                //JAR~RETFNAMENOPARA~STR##INT##STR##
                String inputStr[]=h.split("~~");
                String jarname=inputStr[0];
                int i=1;
                while(true)
                {
                    if(inputStr.length<=i)
                        break;
                    String newEntry=inputStr[i]+"~~"+inputStr[i+1]+"~~"+inputStr[i+2]+"~~";
                    String Parameters[]=inputStr[i+3].split("##");

                    for(int k=0;k<Parameters.length;k++)
                    {
                        newEntry+=Parameters[k]+"##";
                    }
                    newEntry+="~~";
                    MainServer.methodToJarFile.put(newEntry,jarname);
                    i+=4;
                }
                System.out.println(MainServer.methodToJarFile);
            }
        }
        catch (Exception e)
        {
            // Throwing an exception
            System.out.println ("Exception is caught");
        }
    }
}
