package com.custom;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;
import android.util.Log;

public class Security {
	public static String hmac_md5(String key, String message){

		Mac mac = null;
	    
	    try {
	    	SecretKeySpec secret = new SecretKeySpec((key).getBytes(), "HmacMD5");
	    	mac = Mac.getInstance("HmacMD5");
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
            return Base64.encodeToString(hash.toString().getBytes("UTF-8"), Base64.DEFAULT);
		} catch (Exception e) {}

        return null;
	}
	public static String encrypt(String key, String message){
		String result = "";

		try {
			
			message = new String(message.getBytes("UTF-8"),"ISO-8859-1");
			int key_length = key.length();
			int string_length = message.length();
			
			for(int i=0; i<string_length; i++) {
				int offset = (int)Math.floor(key_length/3)*2;
		        offset = (int)i%key_length-offset;
		        offset = offset<0?(int)(offset+key_length):offset;

		        char k = key.charAt(offset);
		        char c = message.charAt(i);
				result+=""+(char)(c+k);
			}
			result = Base64.encodeToString(result.getBytes("UTF-8"), Base64.DEFAULT);
			
			result = ((int)result.charAt(result.length()-1))==10 ? result.substring(0, (result.length()-1)) : result;
		} catch (Exception e) {e.printStackTrace();
			Log.d("dx",e.toString());
		}	
		return result;
	}
	public static String decrypt(String key, String message){
		String result = "";

		try {
			message = new String(Base64.decode(message.getBytes("UTF-8"), Base64.DEFAULT));
		
			int key_length = key.length();
			int string_length = message.length();
			
			for(int i=0; i<string_length; i++) {
				int offset = (int)Math.floor(key_length/3)*2;
		        offset = (int)i%key_length-offset;
		        offset = offset<0?(int)(offset+key_length):offset;

		        char k = key.charAt(offset);
		        char c = message.charAt(i);
				result+=""+(char)(c-k);
			}
			result = new String(result.getBytes("ISO-8859-1"),"UTF-8");
		} catch (Exception e) {e.printStackTrace();
		}	
		return result;
	}
}
