/**
 * Created by KellyZhang on 2017/3/20.
 */

import org.apache.commons.codec.binary.Hex;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Xcryption {
    public String encrypt(String pin){
        String resultPin = "";
        try {
            // encrypte password
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(pin.getBytes(Charset.forName("UTF-8")));
            final byte[] resultByte = m.digest();
            resultPin = new String(Hex.encodeHex(resultByte));
            return resultPin;
        } catch (NoSuchAlgorithmException e) {
            System.err.print("Exceptions when operating md5");
            e.printStackTrace();
        }

        return resultPin;
    }
}
