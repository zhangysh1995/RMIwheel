
import java.io.Serializable;
import java.util.*;

// This is simple. ROR needs a new object key for each remote object (or its skeleton). 
// This can be done easily, for example by using a counter.
// We also assume a remote object implements only one interface, which is a remote interface.

public class RORtbl {
    // create a table of keys (service names) and ROR.
    Hashtable<String, RemoteObjectRef> table;

    // make a new table. 
    public RORtbl() {
        table = new Hashtable<String, RemoteObjectRef>();
    }

    public void addObj(String serviceName, RemoteObjectRef o) {
        // put new pair into hash table
        table.put(serviceName, o);
    }

    public void rebindObj(String serviceName, RemoteObjectRef o) {
        // remove old pair in hash table
        table.remove(o);
        addObj(serviceName, o);
    }

    // given ror, find the corresponding object.
    public RemoteObjectRef findObj(String servicename) {
        if ( table.containsKey(servicename)) {
            return table.get(servicename);
        } else return null;
    }
}
