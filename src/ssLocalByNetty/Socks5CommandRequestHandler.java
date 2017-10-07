package ssLocalByNetty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.socksx.v5.DefaultSocks5CommandRequest;
import io.netty.handler.codec.socksx.v5.DefaultSocks5CommandResponse;
import io.netty.handler.codec.socksx.v5.Socks5AddressType;
import io.netty.handler.codec.socksx.v5.Socks5CommandRequest;
import io.netty.handler.codec.socksx.v5.Socks5CommandResponse;
import io.netty.handler.codec.socksx.v5.Socks5CommandStatus;
import io.netty.handler.codec.socksx.v5.Socks5CommandType;
import io.netty.util.concurrent.AbstractFuture;

public class Socks5CommandRequestHandler  extends SimpleChannelInboundHandler<DefaultSocks5CommandRequest>{

	public Socks5CommandRequestHandler()
	{
		System.out.println("创建   转发Handler");
	}
	@Override
	protected void channelRead0(final ChannelHandlerContext clientChannelContext,
			final DefaultSocks5CommandRequest msg) throws Exception {
		// DefaultSocks5CommandRequest、Socks5CommandRequest
		System.out.println("DefaultSocks5CommandRequest、Socks5CommandRequest");
		System.out.println("目标服务器  : " + msg.type() + "," + msg.dstAddr() + "," + msg.dstPort());
		String host = msg.dstAddr();
		int port = msg.dstPort();
		//启动客户端，向目标请求数据啊
		EventLoopGroup group = new NioEventLoopGroup();
		if(msg.type().equals(Socks5CommandType.CONNECT))
		{
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
			.option(ChannelOption.TCP_NODELAY,true)
			.handler(new ChannelInitializer<SocketChannel>(){
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					//从目标读入数据，输出到客户端
					ch.pipeline().addLast(new Dest2ClientHandler(clientChannelContext));
					
//					ch.pipeline()
//					.addLast(new ObjectDecoder(
//							1024*1024,ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())
//							));
//					ch.pipeline().addLast(new ObjectEncoder());
//					ch.pipeline().addLast(new SubReqClientHandler());	
					
				}
			});
            //ChannelFuture f = b.connect(host, port).sync();
			ChannelFuture f = b.connect(host, port);
			
            
            f.addListener(new ChannelFutureListener() {

				public void operationComplete(final ChannelFuture future) throws Exception {
					if(future.isSuccess()) {
						//System.out.println("成功连接目标服务器");
						clientChannelContext.pipeline().addLast(new Client2DestHandler(future));
						Socks5CommandResponse commandResponse = new DefaultSocks5CommandResponse(Socks5CommandStatus.SUCCESS, Socks5AddressType.IPv4);
						clientChannelContext.writeAndFlush(commandResponse);
					} else {
						Socks5CommandResponse commandResponse = new DefaultSocks5CommandResponse(Socks5CommandStatus.FAILURE, Socks5AddressType.IPv4);
						clientChannelContext.writeAndFlush(commandResponse);
					}
				}
				
			});
            
		}else {
			clientChannelContext.fireChannelRead(msg);
		}
		
		//DefaultSocks5CommandResponse response = new DefaultSocks5CommandResponse();
		
	}
	

}
