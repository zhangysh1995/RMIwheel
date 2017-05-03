import java.io.*;
import java.net.Socket;

/**
 * Created by KellyZhang on 2017/4/7.
 */
public class OperationStub {
        Socket s;
    PrintWriter out;
    BufferedReader in;

    String host = "127.0.0.1";
    int port = 1099;

    // fake method invocations
    public boolean login(String user, String pin){
        connect(host, port);
        boolean result = invoke("login", user, pin);
        disconnect();
        return result;
    }

    public boolean register(String user, String pin){
        connect(host, port);
        boolean result = invoke("register", user, pin);
        disconnect();
        return result;
    }

    // remote invocation

    private boolean invoke(String method, String user, String pin) {
        out.println("invoke");
        out.println(method);
        out.println(user);
        out.println(pin);

//        System.out.println("method invocation request was sent to server");
        int result = -1;
        try {
            result = in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.println("Result is: " + result);
        if (result == 116) return true;
        else return false;
    }

    // provided with communication modules
    private void connect(String host, int port) {

        try {
            s = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(), true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void disconnect() {

        try {
            s.close();
            in.close();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
