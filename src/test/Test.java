package test;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.netty.encryption.impl.AesCrypt;

import com.xiaoleilu.hutool.crypto.Mode;
import com.xiaoleilu.hutool.crypto.Padding;
import com.xiaoleilu.hutool.crypto.SecureUtil;
import com.xiaoleilu.hutool.crypto.symmetric.AES;
import com.xiaoleilu.hutool.crypto.symmetric.DES;
import com.xiaoleilu.hutool.crypto.symmetric.SymmetricAlgorithm;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class Test {

	public static void main(String[] args) {
//		int a = 10;
//		long b = 12L;
//		ByteBuf buf =Unpooled.buffer(1024);  
//		buf.writeInt(a);
//		buf.writeLong(b);
//		int c = buf.readInt();
//		System.out.println(c);
//		System.out.println(buf.writerIndex());
		
		String str="017B7D5106003500202389010000010000000000000377777706676F6F676C6503636F6D0000010001";
		System.out.println(new String(Encrypt.parseHexStr2Byte(str)));

	}
	  
	
	  

}








