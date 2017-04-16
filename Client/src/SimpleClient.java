/**
 * Created by KellyZhang on 2017/4/3.
 */
public class SimpleClient {

    public static void main (String [] args) {
        SimpleRegistry r = LocateSimpleRegistry.getRegistry("127.0.0.1", 1099);

    }
}
