package com.jan8192;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class GenerateCheckSum {
    // This sum was generated with http://onlinemd5.com/
    private static String BASE_SUM = "9389C0125F50029216144C8D770A9BF7";

    private String currentSum;

    public Boolean check(String html) throws NoSuchAlgorithmException {

        var md5 = MessageDigest.getInstance("MD5");
        md5.reset();
        md5.update(html.getBytes());

        var digest = md5.digest();
        var bigInt = new BigInteger(1, digest);
        var hashText = bigInt.toString(16);

        while (hashText.length() < 32) {
            hashText = "0" + hashText;
        }

        this.currentSum = hashText;

        if (currentSum.equals(BASE_SUM)) {
            return true;
        }

        return false;

    }

    public String getCurrentSum() {
        return currentSum;
    }
}