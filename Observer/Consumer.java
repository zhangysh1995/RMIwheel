import java.util.concurrent.Executor;
import java.util.concurrent.Executors;  
  
import javax.jms.Connection;
import javax.jms.JMSException;  
import javax.jms.Message;  
import javax.jms.MessageConsumer;  
import javax.jms.Session;  
import javax.jms.TextMessage;  
  
public class Consumer {
    private String name;  
    private String dest;  
    private Connection conn;  
    private MessageConsumer consumer;  
    private Session session;      
    private Executor executor = Executors.newFixedThreadPool(10);  
      
    public Consumer(Connection conn, String dest, String name){
        this.conn = conn;  
        this.dest = dest;  
        this.name = name;  
    }  
      
    public void start() throws JMSException{  
        //使用Consumer之前，必须调用conn的start方法建立连接。  
        conn.start();  
        conn.setClientID(name);
        session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);        
//        consumer = session.createConsumer(session.createTopic(dest));
        consumer = session.createDurableSubscriber(session.createTopic(dest), name);
    }  
  
    public void receive() {  
        executor.execute(new Runnable() {
            public void run() {  
                while (true) {  
                    try {  
                        Message msg = consumer.receive();  
                        if (msg instanceof TextMessage) {  
                            System.out.println(name + " receive message {" + ((TextMessage)msg).getText() + "}");  
                        } else {  
                            System.out.println("msg: " + msg);  
                        }  
                    } catch (JMSException e) {  
                        e.printStackTrace();  
                    }  
                }  
            }  
        });  
    }  
  
}  
