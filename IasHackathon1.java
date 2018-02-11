/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;

/**
 *
 * @author mit
 */
public class IasHackathon1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MalformedURLException, NoSuchMethodException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, InstantiationException, IOException {
        // TODO code application logic here

        IasHackathon1 iasHackathon1 = new IasHackathon1();


        ServerSocket ss = new ServerSocket(Integer.parseInt(args[0]));
        while (true) {
            Socket s = ss.accept();
            DataInputStream dis = new DataInputStream(s.getInputStream());

            String receivedParameter = (String) dis.readUTF();
            ObjectInputStream objin = new ObjectInputStream(s.getInputStream());
            Object response = objin.readObject();

            Object o[] = (Object[]) response;
            ObjectOutputStream objout = new ObjectOutputStream(s.getOutputStream());
            Object s1=iasHackathon1.decode(receivedParameter, o);
            System.out.println(s1);
            objout.writeObject(s1);
        }
    }

    Object decode(String param, Object[] paramObjects) throws MalformedURLException, NoSuchMethodException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, InstantiationException {

        // JAR FRT FN NoP Type Name
        String AllTheParameters[] = param.split("~~");
        String typeOfParam[] = AllTheParameters[4].split("##");

        int index = -1;
        File file = new File(AllTheParameters[0] + ".jar");
        if (file == null) {
            System.out.println("File Not Found");
        }

        URL url = file.toURI().toURL();
        URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Method m = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        m.setAccessible(true);
        m.invoke(classLoader, url);

        Class c4 = Class.forName(AllTheParameters[0]+ "." + AllTheParameters[0]);

        Method methods[] = c4.getDeclaredMethods();

        for (int i = 0; i < methods.length; i++) {

            //  System.out.println("HI"+methods[i].getReturnType());
            if (methods[i].getReturnType().toString().contains(AllTheParameters[1])) {
                // System.out.println("NAME "+methods[i].getName());
                if (methods[i].getName().toString().equals(AllTheParameters[2])) {
                    //          System.out.println(methods[i].getParameterCount());
                    if (methods[i].getParameterCount() == Integer.parseInt(AllTheParameters[3])) {
                        // System.out.println(methods[i].getParameterCount());
                        Parameter para[] = methods[i].getParameters();
                        int count = 0;
                        for (int j = 0; j < Integer.parseInt(AllTheParameters[3]); j++) {
                            String s = para[j].getType().toString();
                            //                System.out.println("TYPE PARA "+typeOfParam[j]);
                            if (s.equalsIgnoreCase(typeOfParam[j])) {
                                // System.out.println("TYPE PARA "+s);
                                count++;
                            }
                        }
                        if (count == Integer.parseInt(AllTheParameters[3])) {
                            //  Class c=Class.forName(AllTheParameters[0].toLowerCase()+"."+AllTheParameters[0]);
                            Object oclass = c4.newInstance();
                            //System.out.println(oclass);
                            //  methods[i].invoke(oclass, paramObjects);

                            return methods[i].invoke(oclass, paramObjects);
                        }
                    }

                }

            }

        }
        return -1;
    }
    //bool validating()

}
