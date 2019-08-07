
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

/**
 * Created by Dolphin on 2014-07-18.
 */
public interface WebSocketListener {

	void channelActive(ChannelHandlerContext ctx) throws Exception;

	void channelInactive(ChannelHandlerContext ctx) throws Exception;

	void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception;
	
	void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception;

	void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception;
}
