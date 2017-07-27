package socks5ByJava;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//从目标获得数据后，不断发给浏览器
public class ResponseThenReturn  extends Thread{
	private OutputStream osIn;
	private InputStream isOut;
	public ResponseThenReturn(OutputStream osIn,InputStream isOut)
	{
		this.osIn = osIn;
		this.isOut = isOut;
	}
	public void run()
	{
		byte buf[] = new byte[1024*10];
		int len;
		try{
			while((len = isOut.read(buf))!=-1)
			{
				if(len>0)
				{
					//System.out.println("目标返回: "+new String(buf, 0, len)); 
					osIn.write(buf,0,len);
					osIn.flush();
				}
				
			}
			
		}catch(IOException e)
		{
			System.out.println("与目标机器断开"); 
		}
	}

}
