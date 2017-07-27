package socks5ByJava;
import java.io.IOException;  
import java.io.InputStream;  
import java.io.OutputStream;  
import java.net.Socket;  

public class SocketThread extends Thread{
	//接入本机其他进程连接
	private Socket socketIn;
	//和本机进程通信的数据流
	private InputStream isIn;
	private OutputStream osIn;
	//向外部请求的连接
    private Socket socketOut;  
    private InputStream isOut;  
    private OutputStream osOut;  
	/***********************sock5协议相关*********************************/
	private byte[] buffer = new byte[4096];  
	/*
	· X’00’ 不需要认证
	· X’01’ GSSAPI
	· X’02’ 用户名/密码
	· X’03’ -- X’7F’ 由IANA分配
	· X’80’ -- X’FE’ 为私人方法所保留的
	· X’FF’ 没有可以接受的方法
	*/
    private static final byte[] VER = { 0x5, 0x0 };  
    
    private static final byte[] CONNECT_OK = { 0x5, 0x0, 0x0, 0x1, 0, 0, 0, 0, 0, 0 };  
	
    /***********************sock5协议相关*********************************/
	public SocketThread(Socket socketIn)
	{
		this.socketIn = socketIn;
		try{
			isIn = socketIn.getInputStream();
			osIn = socketIn.getOutputStream();
		} catch (IOException e) {
			System.out.println("输入输出流出错");
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		try {
			System.out.println("新连接:"+socketIn.getInetAddress()+":" + socketIn.getPort());
			int receiveLen = isIn.read(buffer);
			System.out.println("收到数据长度"+receiveLen);
			System.out.println("receive: " + bytesToHexString(buffer, 0, receiveLen));  
            osIn.write(VER);  
            osIn.flush();  
            System.out.println("send: " + bytesToHexString(VER, 0, VER.length));  
            receiveLen = isIn.read(buffer);  
            System.out.println("receive: " + bytesToHexString(buffer, 0, receiveLen));  
            // 查找主机和端口  
            String host = findHost(buffer, 4, 7);  
            int port = findPort(buffer, 8, 9);  
            System.out.println("host=" + host + ",port=" + port);  
            //向外请求
            socketOut = new Socket(host,port);
            isOut = socketOut.getInputStream();
            osOut = socketOut.getOutputStream();
            //向浏览器返回请求码   ip:7B CE 4C 8A(123.206.76.138)     port:1F 90(8080)
            //byte code[] = {0x05,0x01,0x00,0x01,0x7B,(byte) 0xCE,0x4C,(byte) 0x8A,0x1F,(byte) 0x90};
            
            for (int i = 4; i <= 9; i++) {  
                CONNECT_OK[i] = buffer[i];  
            }  
            osIn.write(CONNECT_OK);  
            osIn.flush();  
            //System.out.println("send:" + bytesToHexString(code, 0, code.length)); 
            //将对外请求的数据转发给浏览器
            ReadThenRequst request = new ReadThenRequst(isIn,osOut);
            request.start();
            ResponseThenReturn response = new ResponseThenReturn(osIn,isOut);
            response.start();
            
            request.join();
            response.join();
             
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("与浏览器的一个线程(进程)断开");  
		}catch(InterruptedException e){
			System.out.println("线程出错");
		}finally
		{
			if(socketIn != null)
			{
				try {
					socketIn.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
	public static String bytesToHexString(byte[] bArray, int begin, int end) {  
        StringBuffer sb = new StringBuffer(bArray.length);  
        String sTemp;  
        for (int i = begin; i < end; i++) {  
            sTemp = Integer.toHexString(0xFF & bArray[i]);  
            if (sTemp.length() < 2)  
                sb.append(0);  
            sb.append(sTemp.toUpperCase());  
            sb.append(" ");  
        }  
        return sb.toString();  
    }  
	
	public static String findHost(byte[] bArray, int begin, int end) {  
        StringBuffer sb = new StringBuffer();  
        for (int i = begin; i <= end; i++) {  
            sb.append(Integer.toString(0xFF & bArray[i]));  
            sb.append(".");  
        }  
        sb.deleteCharAt(sb.length() - 1);  
        return sb.toString();  
    }  
  
    public static int findPort(byte[] buf, int begin, int end) {  
    	//端口号用2字节存储
    	//8080=0x1F90   0x1F=31,0x90=144    8080=31*256+144
    	int high = buf[begin] & 0xFF;
    	int low = buf[end] & 0xFF;
    	//Java 总是把 byte 当做有符处理；我们可以通过将其和 0xFF 进行二进制与得到它的无符值 
    	//System.out.println("high="+high+  "    low="+low);
        int port = high*256 + low;  
        return port;  
    }  
	
	
	
	
	
}
