import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;


public class ConnFactory {  
    
    static private ActiveMQConnectionFactory factory;
      
    public ConnFactory(){  
//        try {  
//            Context context = new JndiFactory().getJndiContext();  
//            this.factory = (ConnectionFactory) context.lookup("con1");  
//        } catch (NamingException e) {             
            this.factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
//        }  
    }  
      
    public ActiveMQConnection createConnection() throws JMSException{
        return  (ActiveMQConnection) factory.createConnection();
    }     
}  
