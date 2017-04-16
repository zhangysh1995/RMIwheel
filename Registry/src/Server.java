public class Server{

	
	public Server(){
		super();
	}

	public static void main(String[] args){
//		if (System.getSecurityManager() == null)
//			System.setSecurityManager(new SecurityManager());

		try {
			String name = "userOperation";

			SimpleRegistry registry = LocateSimpleRegistry.getRegistry();
			// prepare remote reference
			Operation operation = new Operation();
			RemoteObjectRef uo = new RemoteObjectRef("127.0.0.1", 1099, 0, "OperationStub");
			// add remote reference to table
			registry.rebind("userOperation", uo);

			System.out.println("Server is running.\nServer is waiting for requests...");

		} catch (Exception e) {
			System.out.println("Exception in Server");
			e.printStackTrace();
		}
		
	}
}
