import javax.jms.JMSException;

public class Publisher {

	public static void main(String[] args) throws JMSException {  
	     
	    ConnFactory cf = new ConnFactory();       
	      
	    Producer producer1 = new Producer(cf.createConnection(), "Topic1", "Product1");  
	    Producer producer2 = new Producer(cf.createConnection(), "Topic2", "Product2");  
	      
	    producer1.start();  
	    producer2.start();  
	      
	    for(int i = 0; i < 6; i++){  
	        producer1.send("message " + i);  
	        producer2.send("message " + i);  
	    }  
	      
	}  

}
