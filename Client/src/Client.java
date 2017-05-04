import java.rmi.RemoteException;
import java.lang.Integer;
import java.util.Enumeration;
import java.util.Scanner;
import static javax.swing.JFrame.EXIT_ON_CLOSE;


public class Client {
	static Scanner input = new Scanner(System.in);

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
			} else {
				System.out.println("Internal Error.");
				System.exit(-1);
			}
		}

		else if (type == operation.login) {
			if (result) {
				System.out.println("\nLogin successfully!");
			} else {
				System.out.println("WRONG username or password");
				System.exit(0);
			}
		}

		operations(stub);
	}

	public void operations(UserOperation stub){
		String dest;
		String name;
		String text;

		Enumeration<String> topics = stub.getTopics();
		if (topics!=null) {
			System.out.println(java.util.Arrays.asList(topics));
		}

		while (true) {
			System.out.println("Choose one operation: 1.Subscribe 2.Publish 3.Receive 4.New Topic 5.Exit");
			int choice = input.nextInt();
			switch (choice) {
				case 1:
					System.out.println("Enter topic: ");
					dest = input.nextLine();
					System.out.println("Enter your name: ");
					name = input.nextLine();
					stub.subscribe(dest, name);
					break;
				case 2:
					System.out.println("Enter topic: ");
					dest = input.nextLine();
					System.out.println("Enter message: ");
					text = input.nextLine();
					stub.publish(dest, text);
					break;
				case 3:
					System.out.println("Enter your name: ");
					name = input.nextLine();
					Consumer consumer = stub.receive(name);
					consumer.receive();
					break;
				case 4:
					System.out.println("Enter topic: ");
					dest = input.nextLine();
					System.out.println("Enter your name: ");
					name = input.nextLine();
					stub.newTopic(dest, name);
					break;
				case 5:
					System.exit(0);
				default:
					break;
			}
		}
	}
	// start client
	private void run(UserOperation stub){
		System.out.println("===== Welcome to e-shop! Please choose your operation: =====");
		System.out.println("1. Login\n2. Register");
		System.out.println("Your choice: ");


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