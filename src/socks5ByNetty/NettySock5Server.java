package socks5ByNetty;

import orange.netty.NettyOrderServerDemo;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NettySock5Server {
	
	public static void main(String[] args) {
		new NettySock5Server().connect(8888);
	}

	public void connect(int port) throws InterruptedException
	{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try{
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup,workerGroup)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG,100)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline()
					.addLast(new ObjectDecoder(
						1024*1024,ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())
					));
					ch.pipeline().addLast(new ObjectEncoder());
					ch.pipeline().addLast(new SubReqServerHandler());	
				}
			});
			System.out.println("服务器启动");
			//绑定端口,同步等待成功
			ChannelFuture f = b.bind(port).sync();
			//等待服务器监听端口关闭
			f.channel().closeFuture().sync();	
		}finally
		{
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();	
		}	
	}
	

}
