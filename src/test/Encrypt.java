package test;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encrypt {

	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException
	{
		//String content = "test";  
		byte content[] ={(byte)0x39,(byte)0x40,(byte)0x41,(byte)0x00,(byte)0x2D,(byte)0x2D,(byte)0x2D,(byte)0x2D,(byte)0x2D};
		String password = "12345";  		
		//加密  
		System.out.println("加密前：" +  parseByte2HexStr(content));  
		byte[] encryptResult2 = encrypt4(content, password);  
		//解密  
		byte[] decryptResult2 = decrypt2(encryptResult2,password);  
		System.out.println("解密后：" + new String(decryptResult2)); 
		

		/////////////////////////方法3///////////////////////////////////////
		//加密  
//		System.out.println("加密前:" + parseByte2HexStr(content));  
//		byte[] encryptResult2 = encrypt3(content, password);  
//		System.out.println("加密后:"+parseByte2HexStr(encryptResult2));
//		//解密  
//		byte[] decryptResult2 = decrypt(encryptResult2,password);  
//		System.out.println("解密后:" + parseByte2HexStr(decryptResult2)); 
//		
//		byte []b = parseHexStr2Byte(parseByte2HexStr(decryptResult2));
//		System.out.println(parseByte2HexStr(b));
		
		/////////////////////////方法4///////////////////////////////////////
//		String str = "E3651DCB94C4628C1B317F0FC817B69FA4B04C9FB9BE513C44A97535A7ADC0F0D08226AE7DB89C0F90665B458A3E253F0A346D623949EC1994";
//		byte []b2 = parseHexStr2Byte(str);
//		byte[] decryptResult2 = decrypt(b2,password);
//		System.out.println(parseByte2HexStr(b2));
		
	}
	/** 
	 * 加密 
	 * @param content 需要加密的内容 
	 * @param password  加密密码 
	 * @return 
	 */  
	public static byte[] encrypt(String content, String password) {  
	        try {             
	                KeyGenerator kgen = KeyGenerator.getInstance("AES");  
	                kgen.init(128, new SecureRandom(password.getBytes()));  
	                SecretKey secretKey = kgen.generateKey();  
	                byte[] enCodeFormat = secretKey.getEncoded();  
	                SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");  
	                Cipher cipher = Cipher.getInstance("AES");// 创建密码器  
	                byte[] byteContent = content.getBytes("utf-8");  
	                cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化  
	                byte[] result = cipher.doFinal(byteContent);  
	                return result; // 加密  
	        } catch (NoSuchAlgorithmException e) {  
	                e.printStackTrace();  
	        } catch (NoSuchPaddingException e) {  
	                e.printStackTrace();  
	        } catch (InvalidKeyException e) {  
	                e.printStackTrace();  
	        } catch (UnsupportedEncodingException e) {  
	                e.printStackTrace();  
	        } catch (IllegalBlockSizeException e) {  
	                e.printStackTrace();  
	        } catch (BadPaddingException e) {  
	                e.printStackTrace();  
	        }  
	        return null;  
	} 
	public static byte[] encrypt2(String content, String password) {  
        try {  
                SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");  
                Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");  
                byte[] byteContent = content.getBytes("UTF-8");  
                cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化  
                byte[] result = cipher.doFinal(byteContent);  
                return result; // 加密  
        } catch (NoSuchAlgorithmException e) {  
                e.printStackTrace();  
        } catch (NoSuchPaddingException e) {  
                e.printStackTrace();  
        } catch (UnsupportedEncodingException e) {
				e.printStackTrace();
		}  catch (InvalidKeyException e) {  
                e.printStackTrace();  
        } catch (IllegalBlockSizeException e) {  
                e.printStackTrace();  
        } catch (BadPaddingException e) {  
                e.printStackTrace();  
        }  
        return null;  
	}
	public static byte[] encrypt3(byte []content, String password) {  
        try {             
                KeyGenerator kgen = KeyGenerator.getInstance("AES");  
                kgen.init(128, new SecureRandom(password.getBytes()));  
                SecretKey secretKey = kgen.generateKey();  
                byte[] enCodeFormat = secretKey.getEncoded();  
                SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");  
                Cipher cipher = Cipher.getInstance("AES");// 创建密码器  
                //byte[] byteContent = content.getBytes("utf-8");  
                byte[] byteContent = content;
                cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化  
                byte[] result = cipher.doFinal(byteContent);  
                return result; // 加密  
        } catch (NoSuchAlgorithmException e) {  
                e.printStackTrace();  
        } catch (NoSuchPaddingException e) {  
                e.printStackTrace();  
        } catch (InvalidKeyException e) {  
                e.printStackTrace();  
        } catch (IllegalBlockSizeException e) {  
                e.printStackTrace();  
        } catch (BadPaddingException e) {  
                e.printStackTrace();  
        }  
        return null;  
	}
	public static byte[] encrypt4(byte []content, String password) {  
        try {             
                KeyGenerator kgen = KeyGenerator.getInstance("AES");  
                kgen.init(256, new SecureRandom(password.getBytes()));  
                SecretKey secretKey = kgen.generateKey();  
                byte[] enCodeFormat = secretKey.getEncoded();  
                SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");  
                Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");// 创建密码器  
                //byte[] byteContent = content.getBytes("utf-8");  
                byte[] byteContent = content;
                cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化  
                byte[] result = cipher.doFinal(byteContent);  
                return result; // 加密  
        } catch (NoSuchAlgorithmException e) {  
                e.printStackTrace();  
        } catch (NoSuchPaddingException e) {  
                e.printStackTrace();  
        } catch (InvalidKeyException e) {  
                e.printStackTrace();  
        } catch (IllegalBlockSizeException e) {  
                e.printStackTrace();  
        } catch (BadPaddingException e) {  
                e.printStackTrace();  
        }  
        return null;  
} 
	
	/**解密 
	 * @param content  待解密内容 
	 * @param password 解密密钥 
	 * @return 
	 */  
	public static byte[] decrypt(byte[] content, String password) {  
	        try {  
	                 KeyGenerator kgen = KeyGenerator.getInstance("AES");  
	                 kgen.init(256, new SecureRandom(password.getBytes()));  
	                 SecretKey secretKey = kgen.generateKey();  
	                 byte[] enCodeFormat = secretKey.getEncoded();  
	                 SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");              
	                 Cipher cipher = Cipher.getInstance("AES");// 创建密码器  
	                cipher.init(Cipher.DECRYPT_MODE, key);// 初始化  
	                byte[] result = cipher.doFinal(content);  
	                return result; // 加密  
	        } catch (NoSuchAlgorithmException e) {  
	                e.printStackTrace();  
	        } catch (NoSuchPaddingException e) {  
	                e.printStackTrace();  
	        } catch (InvalidKeyException e) {  
	                e.printStackTrace();  
	        } catch (IllegalBlockSizeException e) {  
	                e.printStackTrace();  
	        } catch (BadPaddingException e) {  
	                e.printStackTrace();  
	        }  
	        return null;  
	}
	public static byte[] decrypt2(byte[] content, String password) {  
        try {
                 KeyGenerator kgen = KeyGenerator.getInstance("AES");  
                 kgen.init(256, new SecureRandom(password.getBytes()));  
                 SecretKey secretKey = kgen.generateKey();  
                 byte[] enCodeFormat = secretKey.getEncoded();  
                 SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");              
                 Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");// 创建密码器  
                 cipher.init(Cipher.DECRYPT_MODE, key);// 初始化  
                byte[] result = cipher.doFinal(content);  
                return result; // 加密  
        } catch (NoSuchAlgorithmException e) {  
                e.printStackTrace();  
        } catch (NoSuchPaddingException e) {  
                e.printStackTrace();  
        } catch (InvalidKeyException e) {  
                e.printStackTrace();  
        } catch (IllegalBlockSizeException e) {  
                e.printStackTrace();  
        } catch (BadPaddingException e) {  
                e.printStackTrace();  
        }  
        return null;  
	}
	
	
	
	
	
	/**将二进制转换成16进制 
	 * @param buf 
	 * @return 
	 */  
	public static String parseByte2HexStr(byte buf[]) {  
	        StringBuffer sb = new StringBuffer();  
	        for (int i = 0; i < buf.length; i++) {  
	                String hex = Integer.toHexString(buf[i] & 0xFF);  
	                if (hex.length() == 1) {  
	                        hex = '0' + hex;  
	                }  
	                sb.append(hex.toUpperCase());  
	        }  
	        return sb.toString();  
	}
	/**将16进制转换为二进制 
	 * @param hexStr 
	 * @return 
	 */  
	public static byte[] parseHexStr2Byte(String hexStr) {  
	        if (hexStr.length() < 1)  
	                return null;  
	        byte[] result = new byte[hexStr.length()/2];  
	        for (int i = 0;i< hexStr.length()/2; i++) {  
	                int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);  
	                int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);  
	                result[i] = (byte) (high * 16 + low);  
	        }  
	        return result;  
	}  
	
}
