package com.custom;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HMAC {
	public static String HMACMD5(String key, String message){

		Mac mac = null;
	    
	    try {
	    	mac = Mac.getInstance("HmacMD5");
	    	SecretKeySpec secret = new SecretKeySpec(key.getBytes(), "HmacMD5");
			mac.init(secret);
			byte[] bytes = mac.doFinal(message.getBytes());
			
            StringBuffer hash = new StringBuffer();

            for (int i=0; i<bytes.length; i++) {
                String hex = Integer.toHexString(0xFF &  bytes[i]);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }
            
		    return hash.toString();
		} catch (Exception e) {}

        return null;
	}
}
