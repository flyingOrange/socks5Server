package socks5ByNetty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.socksx.SocksVersion;
import io.netty.handler.codec.socksx.v5.DefaultSocks5InitialRequest;
import io.netty.handler.codec.socksx.v5.DefaultSocks5InitialResponse;
import io.netty.handler.codec.socksx.v5.Socks5AuthMethod;
import io.netty.handler.codec.socksx.v5.Socks5InitialResponse;

public class Socks5InitialRequestHandler extends SimpleChannelInboundHandler<DefaultSocks5InitialRequest>{

	@Override
	protected void channelRead0(ChannelHandlerContext ctx,
			DefaultSocks5InitialRequest msg) throws Exception {
		
		if(msg.decoderResult().isFailure()) {
			System.out.println("不是ss5协议");
			ctx.fireChannelRead(msg);
		} else {
			//System.out.println("是socks5协议！");
			if(msg.version().equals(SocksVersion.SOCKS5)) {
//				if(proxyServer.isAuth()) {
//					Socks5InitialResponse initialResponse = new DefaultSocks5InitialResponse(Socks5AuthMethod.PASSWORD);
//					ctx.writeAndFlush(initialResponse);
//				} else {
//					Socks5InitialResponse initialResponse = new DefaultSocks5InitialResponse(Socks5AuthMethod.NO_AUTH);
//					ctx.writeAndFlush(initialResponse);
//				}
//			}
				System.out.println("返回一下");
				//Socks5InitialResponse initialResponse = new DefaultSocks5InitialResponse(Socks5AuthMethod.NO_AUTH);
				Socks5InitialResponse initialResponse = new DefaultSocks5InitialResponse(Socks5AuthMethod.PASSWORD);
				ctx.writeAndFlush(initialResponse);
				
		}
		
	}
	}
}
