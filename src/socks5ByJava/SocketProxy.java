package socks5ByJava;

import java.net.InetSocketAddress;
import java.net.ServerSocket;  
import java.net.Socket;  
  
public class SocketProxy {  
    /** 
     * @param args 
     */  
    public static void main(String[] args) throws Exception {  
        //ServerSocket serverSocket = new ServerSocket(8888);  
        ServerSocket serverSocket=new ServerSocket();
        serverSocket.setReuseAddress(true); //设置 ServerSocket 的选项
        serverSocket.bind(new InetSocketAddress(8888)); //与 8080 端口绑定
        while (true) {  
            Socket socket = null;  
            try {  
                socket = serverSocket.accept();  
                new SocketThread(socket).start();  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
    }  
}  