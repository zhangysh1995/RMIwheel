import java.rmi.RemoteException;
import java.lang.Integer;
import java.util.Scanner;
import static javax.swing.JFrame.EXIT_ON_CLOSE;


public class Client {

	public Client(){
		super();
	}

	// initialize client operation
	enum operation{
		login,
		register
	};

	// send request to server
	public void request(operation type, UserOperation stub){
		Scanner input = new Scanner(System.in);
		System.out.println("Enter your user name: ");
		String user = input.nextLine();
		System.out.println("Enter your password: ");
		String pin = input.nextLine();

		boolean result = true;
		try {
			switch (type){
			case login:
				result = stub.login(user, pin);break;
			case register:
				result = stub.register(user, pin);break;
			default:
				System.out.println("\nException in client request.");
				System.exit(-1);break;
			}
		} catch (Exception e) {
			System.out.println("Exception in Client when sending requests!!!");
			e.printStackTrace();
		}

		if (type == operation.register) {
			if (result) {
				System.out.println("\nRegister successfully, you can now start shopping.");
				System.exit(0);
			} else {
				System.out.println("Internal Error.");
				System.exit(-1);
			}
		}

		if (type == operation.login) {
			if (result) {
				System.out.println("\nLogin successfully!");
				System.exit(0);
			} else {
				System.out.println("WRONG username or password");
				System.exit(0);
			}
		}

	}

	// start client
	private void run(UserOperation stub){
		System.out.println("===== Welcome to e-shop! Please choose your operation: =====");
		System.out.println("1. Login\n2. Register");
		System.out.println("Your choice: ");
		Scanner input = new Scanner(System.in);

		while (true) {
			int choose = input.nextInt();
			if (choose == 1) {
				request(operation.login, stub);
				break;
			}
			else if(choose == 2) {
				request(operation.register, stub);
				break;
			}
			else System.out.println("Please enter CORRECT operation in the list.");
		}

	}

	// initialize stub
	public static void main(String[] args) throws RemoteException{
		if (args.length < 2){
			System.err.print("\nUsage: java Client Host Port\n");
			System.exit(1);
		}

		try {
			SimpleRegistry registry = LocateSimpleRegistry.getRegistry(args[0], Integer.parseInt(args[1]));
			UserOperation stub = (UserOperation)registry.lookup("userOperation");

			if (stub == null) {
				System.out.println("Null pointer to stub");
				System.exit(-1);
			}
			Client client = new Client();
			client.run(stub);
		} catch (Exception e) {
			System.out.println("Exception in Client.");
			e.printStackTrace();
		}
	}
}