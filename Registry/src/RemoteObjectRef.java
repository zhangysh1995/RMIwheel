import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.*;
import java.rmi.Remote;

public class RemoteObjectRef implements Serializable{
	String IP_adr;
	int Port;
//	static int Obj_num = 0;
	int Obj_Key;
	String Remote_Interface_Name;

//	Class <?> c;

	// ror constructor
	public RemoteObjectRef(String ip, int port, int obj_key, String riname) {
		IP_adr = ip;
		Port = port;
		Obj_Key = obj_key;
		Remote_Interface_Name = riname;
	}

	// method for object stream to send and reconstrcut object
	private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
		stream.writeObject(IP_adr);
		stream.writeObject(Remote_Interface_Name);
		stream.writeInt(Port);
		stream.writeInt(Obj_Key);
//		stream.writeObject(c);
	}

	private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
		IP_adr = (String) stream.readObject();
		Remote_Interface_Name = (String) stream.readObject();
		Port = stream.readInt();
		Obj_Key = stream.readInt();
//		c = (Class) stream.readObject();
	}

	public Object getObejct() {
		Object o = null;

//		Constructor <?> constructor = null;
//		try {
//			constructor = c.getConstructor();
//		} catch (NoSuchMethodException e) {
//			e.printStackTrace();
//		}
//		try {
//			o = constructor.newInstance();
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			e.printStackTrace();
//		}

		o = new OperationStub();
		return o;
	}
}