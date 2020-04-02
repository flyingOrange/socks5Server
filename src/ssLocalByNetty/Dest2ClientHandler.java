package ssLocalByNetty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class Dest2ClientHandler extends ChannelInboundHandlerAdapter
{
	ChannelHandlerContext clientChannelContext = null;//客户端的连接
	public Dest2ClientHandler(ChannelHandlerContext clientChannelContext)
	{
		this.clientChannelContext = clientChannelContext;
	}
	

	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
	{
		//System.out.println("从目标服务器获取数据!");
		clientChannelContext.writeAndFlush(msg);
	}
	@Override
	public void channelInactive(ChannelHandlerContext ctx2) throws Exception {
		//System.out.println("目标服务器断开连接");
		clientChannelContext.channel().close();
	}

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        System.out.println("server exceptionCaught..");
        //cause.printStackTrace();
        ctx.close();
    }
}