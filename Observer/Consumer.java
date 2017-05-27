import javax.jms.*;
  
public class Consumer {
    private String name;  
    private String dest;  
    private Connection conn;  
    private MessageConsumer consumer;
    private Session session;
      
    public Consumer(Connection conn, String name){
        this.conn = conn;
        this.name = name;
    }  
      
    public void start(String dest) throws JMSException{
        //使用Consumer之前，必须调用conn的start方法建立连接。
        this.dest = dest;
        conn.start();
        session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        consumer = session.createDurableSubscriber(session.createTopic(dest), name);
    }  
  
    public void receive() {
                    try {
                        // shouldn't wait forever
                        Message msg = consumer.receiveNoWait();

                        if (msg instanceof TextMessage) {
                            System.out.println("You receive message {" + ((TextMessage)msg).getText() + "}");
                        } else {
                            System.out.println("No new message for topic: " + dest);
                        }
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
    }
}  
