package socks5ByNetty;

import java.io.ByteArrayOutputStream;

import org.netty.encryption.CryptFactory;
import org.netty.encryption.ICrypt;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
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
import test.Encrypt;

//测试密码是 12345
public class ServerHandler extends ChannelInboundHandlerAdapter {
	private ICrypt crypt;
	public ServerHandler()
	{
		this.crypt = CryptFactory.get("aes-256-cfb", "12345");
	}
	
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
		//logger.error("HostHandler error", cause);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
	}
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buff = (ByteBuf)msg;
		int len = buff.readableBytes();
		byte []arr = new byte[len];
		System.out.println("receive data length:"+len);
		buff.getBytes(0, arr);
		//解密
		ByteArrayOutputStream localOutStream = new ByteArrayOutputStream();
		crypt.decrypt(arr, arr.length, localOutStream);
		//解密后的byte数组
		byte []data = localOutStream.toByteArray();
		System.out.println(Encrypt.parseByte2HexStr(data));
		System.out.println(new String(data));
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
		System.out.println("host = " + host + ",port = " + port + ",dataBuff = " + dataBuff.readableBytes());
		ctx.channel().pipeline().addLast(new ClientProxyHandler(host, port, ctx, dataBuff, crypt));
		ctx.channel().pipeline().remove(this);
		
		
		
	}
	
}
