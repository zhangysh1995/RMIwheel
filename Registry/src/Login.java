public class Login {
    static Xcryption xcryption = new Xcryption();

    public boolean run(String user, String pin){
        boolean result = true;

        try {
            String resultPin = xcryption.encrypt(pin);
            DBconnection conn = new DBconnection();
            conn.connect();
            if ( !conn.login(user, resultPin)) result = false;
            // database operations to save new user

//            System.out.println("result = " + result + " in login");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
