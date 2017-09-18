package test;

import java.util.ArrayList;

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
//		
//		int c = buf.readInt();
//		System.out.println(c);
//		System.out.println(buf.writerIndex());
//		String content = "test中文";
//		//随机生成密钥
//		byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
//		//构建
//		AES aes = SecureUtil.aes(key);
//		//加密
//		byte[] encrypt = aes.encrypt(content);
//		//解密
//		byte[] decrypt = aes.decrypt(encrypt);
//
//
//		
//		byte[] key2 = SecureUtil.generateKey(SymmetricAlgorithm.DES.getValue()).getEncoded();
//		DES des = SecureUtil.des(key2);
//		//加密为16进制表示
//		String encryptHex = des.encryptHex(content);
//		//解密为原字符串
//		String decryptStr = des.decryptStr(encryptHex);
//		System.out.println(decryptStr);
		
		System.out.println("start");
		ArrayList<Object> al = new ArrayList<Object>();
		while(true)
		{
			//al.add(new Object());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}








