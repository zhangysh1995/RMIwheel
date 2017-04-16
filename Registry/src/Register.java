public class Register{
	static Xcryption xcryption = new Xcryption();

	public boolean run(String user, String pin){
		boolean result = true;

		try {
			String resultPin = xcryption.encrypt(pin);
			DBconnection conn = new DBconnection();
			conn.connect();
			if ( !conn.updateUser(user, resultPin)) result = false;
			// database operations to save new user

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
    }
}