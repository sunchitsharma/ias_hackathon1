import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class MainServer {
    static Queue<Socket> pendingQueuesSocket = new LinkedList<>();
    static Queue<String> pendingQueuesStr = new LinkedList<>();
    static Queue<Object[]> pendingQueuesParameters = new LinkedList<>();
    static HashMap<String, String> methodToJarFile = new HashMap<>();
}
