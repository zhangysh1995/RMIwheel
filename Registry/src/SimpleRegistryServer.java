import java.lang.reflect.*;
import java.rmi.Remote;
import java.util.*;
import java.net.*;
import java.io.*;

// This is a simple registry server.
// The system does not do any error checking or bound checking.
// It uses the ROR as specified in the coursework sheet.

// protocol: 
//   (1) lookup  --> returns ROR.
//   (2) rebind --> binds ROR.
//   (3) whoareyou --> I am simple registry etc.
// it is used through SimpleRegistry and LocateSimpleRegistry.

public class SimpleRegistryServer {

	public static void main(String args[]) throws IOException {
		// I do no checking. A user supplies one argument,
		// which is a port name for the registry
		// at the host in which it is running.
		 int port = Integer.parseInt(args[0]);

		// create a socket.
		ServerSocket serverSoc = new ServerSocket(port);
		System.out.println("server socket created.\n");

		Operation operation = new Operation();
		// create new local registry
		SimpleRegistry registry = LocateSimpleRegistry.getRegistry();
		// prepare remote reference
		RemoteObjectRef uo = new RemoteObjectRef("127.0.0.1", 1099, 0, "userOperation");
//		// add remote reference to table
		registry.rebind("userOperation", uo);

		while (true) {
			// create new connections.
			Socket newsoc = serverSoc.accept();

			System.out.println("accepted the request.");

			// input/output streams (remember, TCP is bidirectional).
			BufferedReader in = new BufferedReader(new InputStreamReader(newsoc.getInputStream()));
			PrintWriter out = new PrintWriter(newsoc.getOutputStream(), true);

			String command = in.readLine();
			// branch: commands are either lookup or rebind.
			if (command.equals("lookup")) {
				System.out.println("it is lookup request.");

				String serviceName = in.readLine();

				System.out.println("The service name is " + serviceName + ".");

				RemoteObjectRef ror = registry.localLookup(serviceName);

				// output stream to send objects
				ObjectOutputStream oout = new ObjectOutputStream(newsoc.getOutputStream());

				// tests if it is in the table.
				// if it is gets it.
				if ( ror != null) {
					System.out.println("the service found.");

					System.out.println("ROR is " + ror.IP_adr + "," + ror.Port + "," + ror.Obj_Key + ","
							+ ror.Remote_Interface_Name + ".");

					oout.writeObject(ror);
					oout.flush();;
					System.out.println("ROR was sent.\n");
				} else {
					System.out.println("the service not found.\n");
					out.println("not found");
				}

				oout.close();

			} else if (command.equals("rebind")) {
				System.out.println("it is rebind request.");

				// again no error check.
				String serviceName = in.readLine();

				System.out.println("the service name is " + serviceName + ".");

				System.out.println("I got the following ror:");

				String IP_adr = in.readLine();
				int Port = Integer.parseInt(in.readLine());
				int Obj_Key = Integer.parseInt(in.readLine());
				String Remote_Interface_Name = in.readLine();

				System.out.println("IP address: " + IP_adr);
				System.out.println("port num:" + Port);
				System.out.println("object key:" + Obj_Key);
				System.out.println("Interface Name:" + Remote_Interface_Name);

				// make ROR.
				RemoteObjectRef ror = new RemoteObjectRef(IP_adr, Port, Obj_Key, Remote_Interface_Name);

				System.out.println("ROR is put in the table.\n");

				// ack.
				out.println("bound");

			} else if (command.equals("who are you?")) {
				out.println("I am a simple registry.");
				System.out.println("I was asked who I am, so I answered.\n");

			} else if (command.equals("invoke")){
				System.out.println("Invocation was received");
//				String method = in.readLine();
//				String user = in.readLine();
//				String pin = in.readLine();
//				System.out.println("method: " + method + " user: " + user + " pin: " + pin);
//				boolean result;
//				if (method.equals("login"))
//					result = operation.login(user, pin);
//				else
//					result = operation.register(user, pin);

//				System.out.println("Result is: " + result);
//				out.println(result);

				// using proxy to invoke
				try {
					ObjectInputStream oint = new ObjectInputStream(newsoc.getInputStream());
					ObjectOutputStream oot = new ObjectOutputStream(newsoc.getOutputStream());

					// get method parameter types
					Object[] arg = (Object [])oint.readObject();
					Class<?>[] parameterTypes = new Class<?>[arg.length];
					for (int i = 0; i < arg.length ; i++) {
						parameterTypes[i] = arg[i].getClass();
//						System.out.print(arg[i].getClass());
					}

					// get method to be invoked
					Method method = Operation.class.getMethod((String)oint.readObject(), parameterTypes);

					// invoke method on object & write result back to client
					oot.writeObject(method.invoke(operation, arg));

					oint.close();
					oot.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("Required method was invoked, result was sent to client.");
			} else {
				System.out.println("I got an imcomprehensive message.\n");
			}

			// close the socket.
			newsoc.close();
		}
	}
}
