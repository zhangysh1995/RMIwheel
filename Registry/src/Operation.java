import java.io.Serializable;

/**
 * Created by KellyZhang on 2017/3/18.
 */
public class Operation implements UserOperation, Serializable {

    private Login login = new Login();
    private Register register = new Register();

    public boolean login(String user, String pin){

        return login.run(user, pin);
    }

    public boolean register(String user, String pin){

        return register.run(user, pin);
    }

}
