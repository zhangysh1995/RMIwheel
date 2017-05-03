import java.io.Serializable;
import javax.jms.JMSException;

/**
 * Created by KellyZhang on 2017/3/18.
 */
public class Operation implements UserOperation, Serializable {
    private static ConnFactory cf = new ConnFactory();

    private Login login = new Login();
    private Register register = new Register();

    public boolean login(String user, String pin){

        return login.run(user, pin);
    }

    public boolean register(String user, String pin){

        return register.run(user, pin);
    }

    public void subscribe(String dest, String name) {
        try {
            Consumer consumer = new Consumer(cf.createConnection(), dest, name);
        } catch (JMSException e){
            e.printStackTrace();
        }
    }

    public void publish(String dest, String name) {
        try {
            Producer producer = new Producer(cf.createConnection(), dest, name);
        } catch (JMSException e){
            e.printStackTrace();
        }
    }

}
