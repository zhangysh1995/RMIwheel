import org.apache.activemq.advisory.DestinationSource;
import org.apache.activemq.command.ActiveMQTopic;

import javax.jms.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class Client {
	static Scanner input = new Scanner(System.in);
	static ConnFactory conn = new ConnFactory();
    static MessageProducer producer;
	static private ArrayList<Consumer> consumers = new ArrayList<Consumer>(5);
	private Session session;
	private Executor executor = Executors.newFixedThreadPool(10);
	String user;
	private int clientNum = 0;

    public Client(){
	    try {
            Producer producer1 = new Producer(conn.createConnection(), "Topic 1", "Producer 1");
            producer1.start();
            Producer producer2 = new Producer(conn.createConnection(), "Topic 2", "Producer 2");
            producer2.start();

            producer = conn.createConnection().createSession(false, Session.AUTO_ACKNOWLEDGE).createProducer(null);
        } catch (JMSException e) {
	        System.out.println("Exception when intializing producers");
	        e.printStackTrace();
        }
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
		user = input.nextLine();
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

		operations(user);
	}

	public void operations(String user){
		String dest;
		String text;

		while (true) {
			try {
				DestinationSource ds = conn.createConnection().getDestinationSource();
				Set<ActiveMQTopic> topics = ds.getTopics();

				for (ActiveMQTopic topic : topics) {
					System.out.println(topic.getTopicName());
				}
			}catch(JMSException e) {
				e.printStackTrace();
			}

			System.out.println("\nChoose one operation: 1.Subscribe 2.Publish 3.Receive 4.New Topic 5.Exit");
            int choice = input.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Enter topic: ");
                    dest = input.next();
//                    System.out.println("Enter your name: ");
//                    user = input.next();
                    try {
						subscribe(conn.createConnection(), dest, user);
					} catch (JMSException e) {
                    	e.printStackTrace();
					}
                    break;
                case 2:
                    System.out.println("Enter topic: ");
                    dest = input.next();
                    System.out.println("Enter message: ");
                    text = input.next();
                    try {
						publish(conn.createConnection(), dest, text);
					} catch (JMSException e) {
                    	e.printStackTrace();
					}
                    break;
                case 3:
//                    System.out.println("Enter your name: ");
//                    name = input.next();
						if (consumers.isEmpty()) System.out.println("You don't have any subsriptions yet.");
						else for (Consumer consumer : consumers) consumer.receive();
                    break;
                case 4:
                    System.out.println("Enter topic: ");
                    dest = input.next();
//                    System.out.println("Enter your name: ");
//                    user = input.next();
                    try {
                        Producer producer = new Producer(conn.createConnection(), dest, user);
                        producer.start();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    System.exit(0);
                default:
                    break;
            }
		}
	}

	private void subscribe(Connection conn, String dest, String name) {
		try {
			conn.setClientID(name+(clientNum++));
			session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Consumer consumer = new Consumer(conn, name);
			consumer.start(dest);
			consumers.add(consumer);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	private void publish(Connection conn, String dest, String text) {
	    try {
			Producer producer = new Producer(conn, dest, dest);
			producer.start();
			producer.send(text);
        } catch (JMSException e) {
	        e.printStackTrace();
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