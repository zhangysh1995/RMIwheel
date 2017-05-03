

import java.io.IOException;  
import java.util.Properties;  
  
import javax.naming.Context;  
import javax.naming.InitialContext;  
import javax.naming.NamingException;  
  
import org.apache.log4j.Logger;  
  
public class JndiFactory {  
      
    private static final Logger LOGGER = Logger.getLogger(JndiFactory.class);  
      
    protected Context context = null;  
      
    public void initalize() throws NamingException  
    {         
        Properties props = new Properties();  
        try{  
            props.load(this.getClass().getResourceAsStream("jndi.properties"));   
        }catch(IOException ex){  
            LOGGER.error("Can't load jndi.properties.", ex);  
        }  
        context = new InitialContext(props);          
    }  
  
    public Context getJndiContext() throws NamingException {  
        if(context == null){  
            initalize();  
        }  
        return context;  
    }     
  
}  
