import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * Created by KellyZhang on 2017/4/21.
 */
public class OperationProxy implements InvocationHandler {
    Socket s;
    PrintWriter outWriter;
    ObjectOutputStream out;
    ObjectInputStream in;
    Object result;

    String host = "127.0.0.1";
    int port = 1099;

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        s = new Socket(host, port);

        outWriter = new PrintWriter(s.getOutputStream(), true);
        outWriter.println("invoke");

//        System.out.println("method invocation request was sent to server");
        try {
            out = new ObjectOutputStream(s.getOutputStream());
            in = new ObjectInputStream(s.getInputStream());
            out.writeObject(args);
            out.writeObject(method.getName());
//            System.out.println("Arguments are passed to server ...");

            result = in.readObject();
            s.close();
            in.close();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println("Result is: " + result);
        return result;
    }
}
