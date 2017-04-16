/**
 * Created by KellyZhang on 2017/3/20.
 */
import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.concurrent.Semaphore;

public class DBconnection {
    static Connection connection;
    static Semaphore sf = new Semaphore(50, true);

    public boolean connect() {

        try {
            sf.acquire();
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/rmi", "root", "rmitest");
        } catch (SQLException e) {
            System.out.println("Exception when connecting database");
            printException(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sf.release();
        return true;
    }

    public boolean updateUser(String user, String pin) {
        try {
            Statement stt = connection.createStatement();
            stt.execute("INSERT INTO User VALUES('" + user + "', '" + pin + "')");
            stt.close();
            connection.close();
        } catch (SQLException e) {

            System.out.println("Exception when adding new user");
            printException(e);
        }

        return true;
    }

    public boolean login(String user, String pin) {
        boolean result = true;

        try {
            Statement stt = connection.createStatement();
            ResultSet rs = stt.executeQuery("SELECT * FROM User WHERE username='" + user + "' AND pin ='" + pin + "'");
            if (rs.next()) result = true;
            else result = false;

            //System.out.println("result = " + result + " in connection");
            rs.close();
            stt.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Exception when finding user");
            printException(e);
        }

        return result;
    }

    public void printException(SQLException e) {
        System.out.println("SQLException: " + e.getMessage());
        System.out.println("SQLState: " + e.getSQLState());
        System.out.println("VendorError: " + e.getErrorCode());

        e.printStackTrace();
    }
}