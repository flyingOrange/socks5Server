package socks5ByJava;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//从浏览器端不断接收请求，再向目标请求
public class ReadThenRequst extends Thread{

	private InputStream isIn;
	private OutputStream osOut;
	
	public ReadThenRequst(InputStream isIn,OutputStream osOut)
	{
		this.isIn = isIn;
		this.osOut = osOut;
	}
	
	public void run()
	{
		try{
			byte buf[] = new byte[1024*10];
			int len;
			while((len = isIn.read(buf))!=-1)
			{
				if(len>0)
				{
					//System.out.println("浏览器请求: "+new String(buf, 0, len)); 
					osOut.write(buf, 0, len);
					osOut.flush();
				}
			}
			
		}catch(IOException e){
			//e.printStackTrace();
			System.out.println("浏览器断开请求"); 
		}	
	}
	
}
