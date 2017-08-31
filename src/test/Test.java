package test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class Test {

	public static void main(String[] args) {
		String str1 = "你好w";
		//byte []str2 = str1.getBytes();
		//System.out.println(str2.length);
		int a = 10;
		long b = 12L;
		ByteBuf buf =Unpooled.buffer(1024);  
		buf.writeInt(a);
		buf.writeLong(b);
		
		int c = buf.readInt();
		System.out.println(c);
		System.out.println(buf.writerIndex());
		
	}

}
