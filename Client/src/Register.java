/**
 * Created by KellyZhang on 2017/4/16.
 */

import org.apache.commons.lang3.RandomStringUtils;

import javax.swing.*;

public class Register {

    public static void run(JTextArea JTAinfo, String host, int port) {
        for( int i = 0; i < 50; i++) {
            String testString = stringGenerator();
            ClientForBulk cfb = new ClientForBulk(host, port, testString, testString);
            Thread thread = new Thread(cfb);
            JTAinfo.setText(JTAinfo.getText() + "\n" + (i+1) + "/50 test, username & pin is " + testString);
            thread.run();
        }
    }

    private static String stringGenerator() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

}
