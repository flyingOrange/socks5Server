package ssLocalByNetty;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.socksx.v5.DefaultSocks5PasswordAuthRequest;
import io.netty.handler.codec.socksx.v5.DefaultSocks5PasswordAuthResponse;
import io.netty.handler.codec.socksx.v5.Socks5PasswordAuthRequest;
import io.netty.handler.codec.socksx.v5.Socks5PasswordAuthResponse;
import io.netty.handler.codec.socksx.v5.Socks5PasswordAuthStatus;

public class Socks5PasswordAuthRequestHandler extends SimpleChannelInboundHandler<Socks5PasswordAuthRequest> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Socks5PasswordAuthRequest msg) throws Exception {
		//DefaultSocks5PasswordAuthRequest、Socks5PasswordAuthRequest
		System.out.println("DefaultSocks5PasswordAuthRequest、Socks5PasswordAuthRequest");
		String name = msg.username();
		String pwd = msg.password();
		System.out.println("用户名密码 : " +  name + "," + pwd);
		boolean res = name.equals("")&&pwd.equals("");
		//用户密码为空，说明为不鉴权
		if(!res)
		{
			System.out.println("用户密码为空，说明为不鉴权");
			Socks5PasswordAuthResponse passwordAuthResponse = new DefaultSocks5PasswordAuthResponse(Socks5PasswordAuthStatus.SUCCESS);
			ctx.writeAndFlush(passwordAuthResponse);
		}
		else//鉴定用户密码
		{
			System.out.println("鉴定用户密码");
			//res = SqliteOperat.check(name, pwd);
			//通过检验
			if(res)
			{
				Socks5PasswordAuthResponse passwordAuthResponse = new DefaultSocks5PasswordAuthResponse(Socks5PasswordAuthStatus.SUCCESS);
				ctx.writeAndFlush(passwordAuthResponse);
			}
			else{
				Socks5PasswordAuthResponse passwordAuthResponse = new DefaultSocks5PasswordAuthResponse(Socks5PasswordAuthStatus.FAILURE);
				ctx.writeAndFlush(passwordAuthResponse).addListener(ChannelFutureListener.CLOSE);
			}
			
		}
		
		
		
		
//		if(passwordAuth.auth(msg.username(), msg.password())) {
//			ProxyChannelTrafficShapingHandler.username(ctx, msg.username());
//			Socks5PasswordAuthResponse passwordAuthResponse = new DefaultSocks5PasswordAuthResponse(Socks5PasswordAuthStatus.SUCCESS);
//			ctx.writeAndFlush(passwordAuthResponse);
//		} else {
//			ProxyChannelTrafficShapingHandler.username(ctx, "unauthorized");
//			Socks5PasswordAuthResponse passwordAuthResponse = new DefaultSocks5PasswordAuthResponse(Socks5PasswordAuthStatus.FAILURE);
//			//发送鉴权失败消息，完成后关闭channel
//			ctx.writeAndFlush(passwordAuthResponse).addListener(ChannelFutureListener.CLOSE);
//		}
	}
	
}
