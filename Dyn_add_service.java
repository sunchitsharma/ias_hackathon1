import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Scanner;

public class Dyn_add_service {

    public static void main(String[] args) throws MalformedURLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        while (true) {
            System.out.println("Enter the name of the service to load:");
            Scanner sc = new Scanner(System.in);
            String jar_name = sc.nextLine();
            File file = new File(jar_name + ".jar");
            if (!file.exists()) {
                System.out.println("Service not found!");
                continue;
            }
            String jarDetails = jar_name + "~~";
            URL url = file.toURI().toURL();
            URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(classLoader, url);
            Class c = Class.forName(jar_name + "." + jar_name);
            Method[] m = c.getDeclaredMethods();
            for (Method m1 : m) {
                String rettype = (m1.getReturnType().toString().equals("class java.lang.String")) ? "String" : m1.getReturnType().toString();
                String mname = m1.getName();
                int numpara = m1.getParameterCount();
                Parameter[] params = m1.getParameters();
                jarDetails = jarDetails.concat(rettype + "~~" + mname + "~~" + numpara + "~~");
                for (Parameter param : params) {
                    String pty = (param.getType().toString().equals("class java.lang.String")) ? "String" : param.getType().toString();
                    jarDetails = jarDetails.concat(pty + "##");
                }
                jarDetails = jarDetails.concat("~~");
            }
            System.out.println(jarDetails);
            String serverName = "localhost";
            int port = Integer.parseInt("4999");
            try {
                System.out.println("Connecting to " + serverName + " on port " + port);
                Socket client = new Socket(serverName, port);
                System.out.println("Just connected to " + client.getRemoteSocketAddress());
                OutputStream outToServer = client.getOutputStream();
                DataOutputStream out = new DataOutputStream(outToServer);
                out.writeUTF(jarDetails);
                InputStream inFromServer = client.getInputStream();
                DataInputStream in = new DataInputStream(inFromServer);
                System.out.println("Sent...\n");
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

