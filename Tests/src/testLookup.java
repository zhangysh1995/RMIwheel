import java.io.*;

// we test simple registry by looking up a service.

public class testLookup
{

    public static void main(String args[])    
	throws IOException
    {
	if (args.length < 3) {
		System.out.println("Usage: xxx host port service");
		System.exit(0);
	}
	String host = args[0];
	int port = Integer.parseInt(args[1]);

	// these is the service name.
	String ServiceName = args[2];

	System.out.println("We lookup "+ServiceName);

	// locate.
	// first socket connection with registry
	SimpleRegistry sr = LocateSimpleRegistry.getRegistry(host, port);

	System.out.println("located."+sr+"/n");
	
	if (sr != null)
	    {
		// lookup.
		// second socket connection with registry

		// this program need to be revised , so the sent ror could be received
		// this is using the local rortbl which is emppty by now, so there is no service found
			RemoteObjectRef ror = (RemoteObjectRef)sr.lookup(ServiceName);
		
		if (ror != null)
		    {
			System.out.println("IP address is "+ror.IP_adr);
			System.out.println("Port num is "+ror.Port);
			System.out.println("Object key is "+ror.Obj_Key);
			System.out.println("Interface name is "+ror.Remote_Interface_Name);
		    }
		else
		    {
			System.out.println("The service is bound to no remote object.");
		    }
	    }
	else		
	    {
		System.out.println("no registry found.");
	    }

    }
}
