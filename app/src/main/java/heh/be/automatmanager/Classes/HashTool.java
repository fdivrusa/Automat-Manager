package heh.be.automatmanager.Classes;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by flori on 04-12-17.
 */

public class HashTool {

    //Generate the hash
    public String createHash(String passwordToHash) {

        String generatedPassword = null;

        try {

            //Using Sha-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            String salt = "ZQDFZs/.>/.dléélLFçsdmàçé!JSDQ58V7";
            md.update(salt.getBytes("UTF-8"));
            byte[] bytes = md.digest(passwordToHash.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < bytes.length; i++) {

                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
}
