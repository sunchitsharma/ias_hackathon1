import java.net.Socket;

public class CheckQueue extends Thread {
    public void run(){
            int x = Main.checkFreeSlaves();
            if (x != -1 && Main.checkRequestQueue()) {

                String str = MainServer.pendingQueuesStr.remove();
                Object[] parameters = MainServer.pendingQueuesParameters.remove();
                Socket socket = MainServer.pendingQueuesSocket.remove();
                SlaveCaller slave = new SlaveCaller(5002 + x, str, parameters, socket);
                Main.freeLists.set(x,0);
                slave.start();
            }
    }





}
