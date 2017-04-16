
public class ClientForBulk implements Runnable{
	private String host;
	private int port;
	private String user;
	private String pin;

	public ClientForBulk(String host, int port, String user, String pin){
		this.host = host;
		this.port = port;
		this.user = user;
		this.pin = pin;
	}

	public void run() {
		try {
			SimpleRegistry registry = LocateSimpleRegistry.getRegistry(host, port);
			UserOperation stub = (UserOperation)registry.lookup("userOperation");
			boolean result = stub.register(user, pin);

		} catch (Exception e) {
			System.out.println("Exception in Client.");
			e.printStackTrace();
		}
	}
}