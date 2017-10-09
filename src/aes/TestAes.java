package aes;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class TestAes {

	/**
	 * @param args
	 * @throws NoSuchAlgorithmException 
	 * @throws NoSuchPaddingException 
	 * @throws InvalidAlgorithmParameterException 
	 * @throws InvalidKeyException 
	 */
	public static void main(String[] args) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchPaddingException, NoSuchAlgorithmException {
		String message = "orangehaha";
		String key = "12345";
		AES aes = new AES();
		byte[] b = aes.encrypt(message, key, "CFB");
		
		
	}

}
