import java.lang.String;
import java.net.Socket;
import java.util.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;


public class Main {

    public static final Map<Integer, String> slave2ip;
    static
    {
        slave2ip = new HashMap<Integer, String>();
        slave2ip.put(0, "localhost");
        slave2ip.put(1, "localhost");
        slave2ip.put(2, "localhost");
    }

    static ArrayList<Integer> freeLists = new ArrayList<>(Arrays.asList(1,1,1));
    // freeLists.add(1);
    // freeLists.add(1);
    // freeLists.add(1);

    static Queue<Integer> pendingQueues = new LinkedList<>();
    public static int checkFreeSlaves(){
        for(int slaves:freeLists){
            if(slaves==1){
                return freeLists.indexOf(slaves);
            }
        }
        return -1;
    }

    public static boolean checkRequestQueue(){
        if(MainServer.pendingQueuesStr.size()>0){
            return true;
        }
        return false;
    }

    public static String validation(String input) {
        String ans=MainServer.methodToJarFile.get(input);
        if(ans != null)
        {
            return ans;
        }
        else
        {
            return "False";
        }
    }



    // Vector to store active clients
    static Vector<ClientHandler> array_of_clients = new Vector<>();

    // counter for clients
    static int i = 0;



    public static void main(String args[])
            throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException,
            NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {

        DAS object = new DAS();
        object.start();

        // server is listening on port 1234
        ServerSocket serverSocket = new ServerSocket(5000);

        Socket socket;

        // running infinite loop for getting
        // client request
        while (true)
        {
            // Accept the incoming request
            socket = serverSocket.accept();

            System.out.println("New client request received : " + socket);

            // obtain input and output streams
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            System.out.println("Creating a new handler for this client...");

            // Create a new handler object for handling this request.
            ClientHandler mtch = new ClientHandler(socket,"client " + i, dis, dos);

            // Create a new Thread with this object.
            Thread t = new Thread(mtch);

            System.out.println("Adding this client to active client list");

            // add this client to active clients list
            array_of_clients.add(mtch);

            // start the thread.
            t.start();

            // increment i for new client.
            // i is used for naming only, and can be replaced
            // by any naming scheme
            i++;

        }
    }

}

class ClientHandler implements Runnable
{


    final DataInputStream dis;
    final DataOutputStream dos;
    //Socket socket;
    boolean isloggedin;
    private Socket s;

    // constructor
    public ClientHandler(Socket s, String name,DataInputStream dis, DataOutputStream dos) {
        this.dis = dis;
        this.dos = dos;

        this.s = s;
        this.isloggedin=true;
    }

    @Override
    public void run() {

        String received;
        while (true) {
            try {
                // receive the string
                received = dis.readUTF();
                System.out.println(received+"jdfjgf");


                //dos.writeUTF("from server   "+received);


                if (received.equals("logout")) {
                    this.isloggedin = false;
//                    this.s.close();
                    break;
                } else {


                    String str = received;

                    String[] splitted = str.split("#");

                    int len = splitted.length, i = 0;
                    int noOfParam = Integer.parseInt(splitted[3]);
                    i = 0;
                    //param is the object array of input value
                    Object[] param = new Object[noOfParam];
                    int index = 5;


//
//                    while(i<noOfParam)
//                    {
//                        param[i++]=splitted[index];
//                        index+=2;
//                    }


                    int paramindex = 4;
                    while (i < noOfParam) {
                        // param[i++]=splitted[index];


                        //System.out.println(splitted[paramindex]);
                        switch (splitted[paramindex]) {
                            case "int":
                                param[i] = Integer.parseInt(splitted[index]);
                                break;
                            case "float":
                                param[i] = Float.parseFloat(splitted[index]);
                                break;
                            case "char":
                                char c[] = new char[1];
                                c = (splitted[index]).toCharArray();
                                param[i] = c[0];
                                break;
                            case "double":
                                param[i] = Double.parseDouble(splitted[index]);
                                break;
                            default:
                                // param[i]=br.readLine();
                                break;
                        }


                        System.out.println(param[i]);
                        //System.out.println(param[i]);
                        i++;


                        index += 2;
                        paramindex += 2;

                    }


                    i = 0;
                    int temp = noOfParam;
                    //def represents the rest of the data of input
                    String def = splitted[1] + "~~" + splitted[2] + "~~" + splitted[3] + "~~";
                    int first = 4;
                    for (int y = 0; y < noOfParam; y++) {
                        def = def + splitted[first] + "##";
                        first += 2;
                    }
                    def += "~~";


                    System.out.println("formatted input from client ");
                    System.out.println(def);


                    for (i = 0; i < noOfParam; i++) {
                        System.out.println(param[i]);
                    }


                    String clientText = def;
                    Object[] parameters = param;
                    Socket clientSocket = this.s;
                    String jarName = Main.validation(clientText);
                    Object returnValue = null;
                    if (jarName.equals("False")) {
                        String output = "Function Not Found";
                        returnValue = output;
                    } else {
                        MainServer.pendingQueuesStr.add(jarName + "~~" + clientText);
                        MainServer.pendingQueuesParameters.add(parameters);
                        MainServer.pendingQueuesSocket.add(clientSocket);
                        CheckQueue check = new CheckQueue();
                        check.start();
                    }
                    System.out.println(returnValue);


                }


            } catch (IOException e) {
//                System.out.print("Exeption caught");
                System.out.println(e);
                e.printStackTrace();
            }
        }
//        try
//        {
//            // closing resources
////            this.dis.close();
////            this.dos.close();
//
//        }catch(IOException e){
//            e.printStackTrace();
//        }
    }
}
