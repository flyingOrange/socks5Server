package socks5ServerByNetty;

import java.io.ByteArrayOutputStream;

import org.netty.encryption.CryptFactory;
import org.netty.encryption.ICrypt;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicInteger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import test.Encrypt;

//测试密码是 12345
public class ServerHandler extends ChannelInboundHandlerAdapter {
	//加密
	private ICrypt crypt;
	//流量记录
	private AtomicInteger byteFlow;
	public ServerHandler()
	{
		this.crypt = CryptFactory.get("aes-256-cfb", "12345");
		this.byteFlow = new AtomicInteger();
		System.out.println("ServerHandler constructor");
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("ServerHandler channelReadComplete");
	}
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
		//把使用流量字节数保存到数据库 （最好清零）
		System.out.println("ServerHandler save byteFlow = " + byteFlow.get());
		
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
	}
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buff = (ByteBuf)msg;
		int len = buff.readableBytes();
		byte []arr = new byte[len];
		//System.out.println("receive data length:"+len);
		buff.getBytes(0, arr);
		//记录流量
		byteFlow.addAndGet(len);
		//解密
		ByteArrayOutputStream localOutStream = new ByteArrayOutputStream();
		crypt.decrypt(arr, arr.length, localOutStream);
		//解密后的byte数组
		byte []data = localOutStream.toByteArray();
		//System.out.println(Encrypt.parseByte2HexStr(data));
		//System.out.println(new String(data));
		//封装成ByteBuf以方便操作字节
		ByteBuf dataBuff = Unpooled.buffer();
		dataBuff.writeBytes(data);
		/*************处理收到的数据****************/
		// ATYP | DST.ADDR | DST.PORT |
		//   1  | Variable |    2     |
		String host = null;
		int port = 0;
		if(dataBuff.readableBytes()<2)
			return;
		byte ATYP = dataBuff.readByte();
		if(ATYP == (byte)0x01)//ipv4   6字节(DST.ADDR 4字节+DST.PORT  2字节)
		{
			if(dataBuff.readableBytes()<6)
				return;
			//获取IP和端口
			byte[] ipBytes = new byte[4];
			dataBuff.readBytes(ipBytes);
			host = InetAddress.getByAddress(ipBytes).toString().substring(1);	
			port = dataBuff.readShort();
			
		}else if(ATYP == 0x03)//域名
		{
			//域名长度
			int domainLength = dataBuff.readByte();
			//域名
			byte domain[] = new byte[domainLength];
			dataBuff.readBytes(domain);
			host = new String(domain);
			port = dataBuff.readShort();
			
		}
		else if(ATYP == 0x04)//IPV6  目前不支持哟
		{
			throw new IllegalStateException("unknown address type: " + ATYP);
		}
		//System.out.println("host = " + host + ",port = " + port + ",dataBuff = " + dataBuff.readableBytes());
		ctx.channel().pipeline().addLast(new ClientProxyHandler(host, port, ctx, dataBuff, crypt,byteFlow));
		ctx.channel().pipeline().remove(this);
		
		//丢弃已接收的消息
		//ReferenceCountUtil.release(msg);
		
	}
	
}
