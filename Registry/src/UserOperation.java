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
}
