package socks5ByNetty;

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
import io.netty.handler.codec.socks.SocksInitRequestDecoder;
import io.netty.handler.codec.socks.SocksMessageEncoder;
import io.netty.handler.codec.socksx.v5.Socks5AddressEncoder;
import io.netty.handler.codec.socksx.v5.Socks5CommandRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5InitialRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5PasswordAuthRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5ServerEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NettySock5Server {
	
	public static void main(String[] args) throws InterruptedException {
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
					System.out.println("INININININININ");
					//ch.pipeline().addLast(new Socks5ServerEncoder(Socks5AddressEncoder.DEFAULT));
					ch.pipeline().addLast(Socks5ServerEncoder.DEFAULT);
					
					//初始化
					ch.pipeline().addLast(new Socks5InitialRequestDecoder());
					//Socks5InitialRequestHandler        自己实现啊
					ch.pipeline().addLast(new Socks5InitialRequestHandler());
							
					//鉴权
					ch.pipeline().addLast(new Socks5PasswordAuthRequestDecoder());
					//Socks5PasswordAuthRequestHandler       自己实现鉴权
					ch.pipeline().addLast(new Socks5PasswordAuthRequestHandler());
					
					//返回数据
					ch.pipeline().addLast(new Socks5CommandRequestDecoder());
					//实现返回数据自己实现
					ch.pipeline().addLast(new Socks5CommandRequestHandler());       
					
					//ch.pipeline().addLast(new OrangeSocksServerHandler());	
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
