package ssLocal;

import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.socks.SocksAuthResponse;
import io.netty.handler.codec.socks.SocksAuthScheme;
import io.netty.handler.codec.socks.SocksAuthStatus;
import io.netty.handler.codec.socks.SocksCmdRequest;
import io.netty.handler.codec.socks.SocksCmdRequestDecoder;
import io.netty.handler.codec.socks.SocksCmdType;
import io.netty.handler.codec.socks.SocksInitResponse;
import io.netty.handler.codec.socks.SocksRequest;
import io.netty.handler.codec.socksx.SocksVersion;
import io.netty.handler.codec.socksx.v5.DefaultSocks5InitialRequest;
import io.netty.handler.codec.socksx.v5.DefaultSocks5InitialResponse;
import io.netty.handler.codec.socksx.v5.Socks5AuthMethod;
import io.netty.handler.codec.socksx.v5.Socks5InitialRequest;
import io.netty.handler.codec.socksx.v5.Socks5InitialResponse;

public class Socks5InitialRequestHandler extends SimpleChannelInboundHandler<Socks5InitialRequest>{

	public Socks5InitialRequestHandler()
	{
		System.out.println("创建请求Socks5InitialRequestHandler");
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx,
			Socks5InitialRequest request) throws Exception {
		
			//Socks5InitialRequest		DefaultSocks5InitialRequest
			//System.out.println("Socks5InitialRequestHandler、DefaultSocks5InitialRequest");

			if(request.version().equals(SocksVersion.SOCKS5))
			{
				//根据socks5协议可知，method保存的是客户端支持的方法列表
				List<Socks5AuthMethod> method = request.authMethods();
				System.out.println("是socks5协议！   "+method.size()+"    "+method.get(0).toString());
				//这里我们使用是无鉴权的
				Socks5InitialResponse initialResponse = new DefaultSocks5InitialResponse(Socks5AuthMethod.NO_AUTH);
				ctx.writeAndFlush(initialResponse);
				
				
//				if(method.contains(Socks5AuthMethod.PASSWORD))
//				{
//					System.out.println("PASSWORD");
//					Socks5InitialResponse initialResponse = new DefaultSocks5InitialResponse(Socks5AuthMethod.PASSWORD);
//					ctx.writeAndFlush(initialResponse);
//				}else if(method.contains(Socks5AuthMethod.NO_AUTH))
//				{
//					System.out.println("NO_AUTH");
//					Socks5InitialResponse initialResponse = new DefaultSocks5InitialResponse(Socks5AuthMethod.NO_AUTH);
//					ctx.writeAndFlush(initialResponse);
//				}else if(method.contains(Socks5AuthMethod.GSSAPI)||method.contains(Socks5AuthMethod.UNACCEPTED))
//				{
//					System.out.println("UNACCEPTED||GSSAPI");
//					ctx.channel().close();
//				}
				
			}else
			{
				System.out.println("不    是ss5协议!");
				ctx.channel().close();
			}
			
		
				
		}
		
}
