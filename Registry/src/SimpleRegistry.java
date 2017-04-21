import java.rmi.Remote;
import java.util.*;
import java.net.*;
import java.io.*;

public class SimpleRegistry {
	// registry holds its port and host, and connects to it each time.
	String Host;
	int Port;

	// remote reference table
	static RORtbl table;

	// default contructor
	public SimpleRegistry() {
		table = new RORtbl();
		Host = "127.0.0.1";
		Port = 1099;
	}

	// harder constructor
	public SimpleRegistry(String IPAdr, int PortNum) {
		table = new RORtbl();
		Host = IPAdr;
		Port = PortNum;
	}

	public RemoteObjectRef localLookup(String serviceName) throws IOException {
		RemoteObjectRef ror = table.findObj(serviceName);
		return ror;
	}

	// returns the ROR (if found) or null (if else)
	public Object lookup(String serviceName) throws IOException {
		Socket soc = new Socket(Host, Port);

		// get TCP streams and wrap them.
		PrintWriter out = new PrintWriter(soc.getOutputStream(), true);

		// it is locate request, with a service name.
		out.println("lookup");
		out.println(serviceName);

		RemoteObjectRef ror = null;
		// find the object in rortbl
		try {
			// get object input stream
			ObjectInputStream oint = new ObjectInputStream(soc.getInputStream());
			ror  =  (RemoteObjectRef) oint.readObject();
			oint.close();
			soc.close();

		} catch (ClassNotFoundException e) {
			System.out.println("Exception when lookup remote object!!!");
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		}

		// return ROR.
		return ror.getObejct();
	}

	public void rebind(String serviceName, RemoteObjectRef ror) throws IOException {
		table.rebindObj(serviceName, ror);
	}
}
