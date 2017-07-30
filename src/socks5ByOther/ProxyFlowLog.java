package socks5ByOther;

import io.netty.channel.ChannelHandlerContext;

public interface ProxyFlowLog {

	public void log(ChannelHandlerContext ctx);
	
}
