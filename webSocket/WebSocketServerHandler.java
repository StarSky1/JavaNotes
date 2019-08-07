
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;


/**
 * Created by Dolphin on 2017-03-11.
 */
public class WebSocketServerHandler implements WebSocketListener {

    private WebSocketServer webSocketServer;

    public WebSocketServerHandler() {

        try {
            dispose();

            webSocketServer = new WebSocketServer(Constants.WebSocketPath);
            webSocketServer.addNetServerListener(this);
            webSocketServer.bind();
        } catch (Exception e) {
            e.printStackTrace();
            LOGINFO.info("异常信息：" + e.getLocalizedMessage() + ",server is ready to exit");
            LOGERROR.error("异常信息：" + e.getLocalizedMessage() + ",server is ready to exit");
            System.exit(0);
        }
    }

    public void dispose() {
        try {
            if (webSocketServer != null) {
                webSocketServer.dispose();
                webSocketServer = null;
                LOGINFO.info("WebSocketServer dispose");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGINFO.info(e.getLocalizedMessage());
            LOGERROR.error(e.getLocalizedMessage());
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGINFO.info("websocket client " + ctx.channel().remoteAddress() + " connected");

        ////当有客户端连上服务时，将channel保存在ChannelGroups中，并起一个线程通过该通道向客户端进行广播
        ChannelGroups.add(ctx.channel());
        LOGINFO.info("currently there are " + ChannelGroups.size() + " websocket client connect to server");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGINFO.info("websocket client " + ctx.channel().remoteAddress() + " disconnected");

        //当客户端与服务端的连接中断，那么将当前的channel从ChannelGroups中移除
        ChannelGroups.remove(ctx.channel());
        LOGINFO.info("currently there are " + ChannelGroups.size() + " websocket client connect to server");
        //关闭数据传输通道
        ctx.close();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
        // websocket 协议帧支持文本和二进制两种，这里仅针对文本类型做处理
        //WebSocketFrame extends==>DefaultByteBufHolder
        if (msg != null) {
            if (msg instanceof TextWebSocketFrame) {
                LOGINFO.info("received meaasge:" + ((TextWebSocketFrame) msg).text() + " from client:" + ctx.channel().remoteAddress());
            } else {
                LOGINFO.info("websocket frame type:" + msg.getClass().getName() + " unsupported currently");
            }
        } else {
            LOGINFO.info("msg is null");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGINFO.info("websocket client:" + ctx.channel().remoteAddress() + " happens error: " + cause.getLocalizedMessage());
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // do nothing
    }
}
