package ssLocalByNetty;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class Client2DestHandler extends ChannelInboundHandlerAdapter
{
	private ChannelFuture destChannelFuture = null;
	public Client2DestHandler(ChannelFuture destChannelFuture)
	{
		this.destChannelFuture = destChannelFuture;
	}
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		//System.out.println("将客户端的消息转发给目标服务器端");
		destChannelFuture.channel().writeAndFlush(msg);
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		//System.out.println("客户端断开连接");
		destChannelFuture.channel().close();
	}
	
	
}