package socks5ByNetty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.socksx.v5.DefaultSocks5CommandRequest;

public class Socks5CommandRequestHandler  extends SimpleChannelInboundHandler<DefaultSocks5CommandRequest>{

	@Override
	protected void channelRead0(ChannelHandlerContext ctx,
			DefaultSocks5CommandRequest msg) throws Exception {
		System.out.println("目标服务器  : " + msg.type() + "," + msg.dstAddr() + "," + msg.dstPort());
		
		
	}

}
