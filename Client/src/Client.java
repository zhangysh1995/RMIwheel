import org.apache.activemq.advisory.DestinationSource;
import org.apache.activemq.command.ActiveMQQueue;

import javax.jms.*;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class Client {
	static Scanner input = new Scanner(System.in);
	static ConnFactory conn = new ConnFactory();
    static MessageProducer producer;
	static private MessageConsumer consumer;
	private Session session;
	private Executor executor = Executors.newFixedThreadPool(10);
	String user;

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

		operations(stub);
	}

	public void operations(UserOperation stub){
		String dest;
		String name;
		String text;

		while (true) {
			try {
				DestinationSource ds = conn.createConnection().getDestinationSource();
				Set<ActiveMQQueue> queues = ds.getQueues();

				for (ActiveMQQueue queue : queues) {
					System.out.println(queue.getQueueName());
				}
			}catch(JMSException e) {
				e.printStackTrace();
			}

			System.out.println("Choose one operation: 1.Subscribe 2.Publish 3.Receive 4.New Topic 5.Exit");
            int choice = input.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Enter topic: ");
                    dest = input.next();
                    System.out.println("Enter your name: ");
                    name = input.next();
                    try {
						subscribe(conn.createConnection(), dest, name);
					} catch (JMSException e) {
                    	e.printStackTrace();
					}
                    break;
                case 2:
                    System.out.println("Enter topic: ");
                    dest = input.next();
                    System.out.println("Enter message: ");
                    text = input.next();
                    publish(dest, text);
                    break;
                case 3:
//                    System.out.println("Enter your name: ");
//                    name = input.next();
                    try {
						if (consumer == null) System.out.println("You don't have any subsriptions yet.");
						else consumer.receive();
					} catch (JMSException e) {
                    	e.printStackTrace();
					}
                    break;
                case 4:
                    System.out.println("Enter topic: ");
                    dest = input.next();
                    System.out.println("Enter your name: ");
                    name = input.next();
                    try {
                        Producer producer = new Producer(conn.createConnection(), dest, name);
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
			conn.setClientID(name);
			conn.start();
			session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			consumer = session.createDurableSubscriber(session.createTopic(dest), name);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	private void publish(String dest, String text) {
	    try {
            session = conn.createConnection().createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(dest);
            //Added as a producer
            javax.jms.MessageProducer producer = session.createProducer(queue);
            // Create and send the message
            TextMessage msg = session.createTextMessage();
            msg.setText(text);
            producer.send(msg);
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