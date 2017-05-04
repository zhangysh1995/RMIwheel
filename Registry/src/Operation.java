import java.io.Serializable;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import javax.jms.JMSException;

/**
 * Created by KellyZhang on 2017/3/18.
 */
public class Operation implements UserOperation, Serializable {
    private static ConnFactory cf = new ConnFactory();
    private ConcurrentHashMap<String, Producer> topics = new ConcurrentHashMap<String, Producer>();
    private ConcurrentHashMap<String, Consumer> subscribers = new ConcurrentHashMap<String, Consumer>();

    private Login login = new Login();
    private Register register = new Register();

    public boolean login(String user, String pin){

        return login.run(user, pin);
    }

    public boolean register(String user, String pin){

        return register.run(user, pin);
    }

    public void subscribe(String dest, String name) {
        try {
            Consumer consumer = new Consumer(cf.createConnection(), dest, name);
            subscribers.put(name, consumer);
            consumer.start();
        } catch (JMSException e){
            e.printStackTrace();
        }
    }

    public Enumeration<String> getTopics() {
//        if(!topics.isEmpty()){
//            Enumeration<String> topic = null;
//            return topic;
//        }
//        else
        return topics.keys();
    }

    public void newTopic(String dest, String name) {
        try {
            Producer producer = new Producer(cf.createConnection(), dest, name);
            topics.put(name, producer);
            producer.start();
        } catch (JMSException e){
            e.printStackTrace();
        }
    }

    public void publish(String dest, String text) {
        Producer producer = topics.get(dest);
        try {
            producer.send(text);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public Consumer receive(String name) {
        Consumer consumer = subscribers.get(name);
        return consumer;
    }

}
