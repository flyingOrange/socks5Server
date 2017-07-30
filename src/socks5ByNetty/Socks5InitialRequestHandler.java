package socks5ByNetty;

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

	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx,
			Socks5InitialRequest msg) throws Exception {
		
			//Socks5InitialRequest		DefaultSocks5InitialRequest
			System.out.println("Socks5InitialRequestHandler、DefaultSocks5InitialRequest");
			
			if(msg.version().equals(SocksVersion.SOCKS5))
			{
				System.out.println("是socks5协议！");
				
				Socks5InitialResponse initialResponse = new DefaultSocks5InitialResponse(Socks5AuthMethod.NO_AUTH);
				//Socks5InitialResponse initialResponse = new DefaultSocks5InitialResponse(Socks5AuthMethod.PASSWORD);
				ctx.writeAndFlush(initialResponse);
			}else
			{
				System.out.println("不    是ss5协议!");
				ctx.close();
			}
			
					
//			if(msg.decoderResult().isFailure())
//			{
//				System.out.println("不是ss5协议");
//				ctx.fireChannelRead(msg);
//			}else
//			{
//				if(msg.version().equals(SocksVersion.SOCKS5))
//				{
//					System.out.println("是socks5协议！");
//					//System.out.println("返回一下");
//					Socks5InitialResponse initialResponse = new DefaultSocks5InitialResponse(Socks5AuthMethod.NO_AUTH);
//					//Socks5InitialResponse initialResponse = new DefaultSocks5InitialResponse(Socks5AuthMethod.PASSWORD);
//					ctx.writeAndFlush(initialResponse);
//				}
//			}
		
				
		}
		
}
