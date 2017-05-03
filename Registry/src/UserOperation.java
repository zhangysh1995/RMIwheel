import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.Remote;
import java.util.Enumeration;

/**
 * Created by KellyZhang on 2017/3/18.
 */
public interface UserOperation extends Remote{
    public boolean login(String user, String pin) throws RemoteException;
    public boolean register(String user, String pin) throws RemoteException;
    public void subscribe(String dest, String name);
    public Enumeration<String> getTopics();
    public void newTopic(String dest, String name);
    public void publish(String dest, String text);
    public Consumer receive(String name);
}
