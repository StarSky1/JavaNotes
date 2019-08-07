
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;

import java.util.ArrayList;


/**
 * Created by dolphin on 14-4-25.
 */
public class WebSocketServer {

    private ArrayList<WebSocketListener> webSocketListenerArrayList = new ArrayList<>();
    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();
    private ServerBootstrap serverBootstrap;

    public WebSocketServer(final String webSocketPath) {
        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_RCVBUF, 10 * 1024 * 1024)
                .childOption(ChannelOption.SO_SNDBUF, 10 * 1024 * 1024)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                /* 用于处理 websocket 协议的管线设置：
                 * 1、添加 HttpServerCodec 进行 http 协议收包解码及发包编码，HttpServerCodec 同时包含 HttpRequest 的解码以及 HttpResponse
				 * 的编码，就不必再单独添加它们的各自编解码器了。
				 * 2、之后添加 HttpObjectAggregator 对解码后的 HttpRequest 分片数据和编码后的 HttpResponse 分片数据进行聚合，生成完整的
				 * FullHttpRequest 或 FullHttpResponse 数据。
				 * 3、添加 WebSocketServerCompressionHandler 用于对进行压缩过的 WebSocket 协议数据进行解压缩处理或者对要发出的未压缩原始 WebSocket
				 * 协议数据进行压缩处理再发出。WebSocket 协议支持压缩以便降低网络带宽占用。
				 * 4、添加 WebSocketServerProtocolHandler 这个 netty 提供的处理器，用于对 WebSocket 握手前的 Http 协议及握手后的 WebSocket 协议
				 * 进行解析处理，不必自己去实现这些握手判断以及协议升级的动作。
				 * 5、最后添加自己的 WebSocket 协议帧的处理器。
				 *
				 * 注意，这里 WebSocket 协议需要针对指定的 websocket path 才能生效哦，比如 /websocket，在客户端需要指定完整的协议路径为
				 * ws://server-ip:port/websocket 才能建立 websocket 通道。另一方面，由于我们使用了 websocket commpression 处理器，
				 * 实际上是应对了 websocket 的压缩扩展功能，所以 WebSocketServerProtocolHandler 构造函数必须使用下面的三个参数形式，尤
				 * 其是最后一个参数 allowExtensions 必须设置为 true，允许 websocket 扩展才能确保正常，否则客户端一旦发出消息就会导致服务
				 * 端主动断开连接。
				 */
                        ch.pipeline()
                                .addLast("httpServerCodec", new HttpServerCodec())
                                .addLast("httpAggregator", new HttpObjectAggregator(1024 * 1024))
                                .addLast("webSocketServerCompressionHandler", new WebSocketServerCompressionHandler())
                                .addLast("webSocketServerProtocolHandler", new WebSocketServerProtocolHandler(webSocketPath, null, true))
                                .addLast("webSocketFrameHandler", new ServerHandler());
                    }
                });

        LOGINFO.info("websocket server created");
    }

    public void bind() throws Exception {
        ChannelFuture channelFuture = serverBootstrap.bind(Constants.ServerIP, Constants.ServerPort).syncUninterruptibly();
        LOGINFO.info("websocket server listening port:" + Constants.ServerPort);

        channelFuture.channel().closeFuture().syncUninterruptibly(); // 一直等待直到服务器停止服务才返回
        LOGINFO.info("websocket server closed");
    }

    public void dispose() {
        webSocketListenerArrayList.clear();
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        LOGINFO.info("websocket server disposed");
    }

    public void addNetServerListener(WebSocketListener webSocketListener) {
        if (webSocketListener != null) webSocketListenerArrayList.add(webSocketListener);
    }

    public void removeNetServerListener(WebSocketListener webSocketListener) {
        if (webSocketListener != null) {
            for (WebSocketListener webSocketListener1 : webSocketListenerArrayList) {
                if (webSocketListener1 != null && webSocketListener1.equals(webSocketListener)) {
                    webSocketListenerArrayList.remove(webSocketListener);
                    break;
                }
            }
        }
    }

    // 内部类

    // 内部类 - 服务端网络事件处理器
    private class ServerHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

        @Override
        public void channelActive(final ChannelHandlerContext ctx) throws Exception {
            for (WebSocketListener webSocketListener : webSocketListenerArrayList) {
                webSocketListener.channelActive(ctx);
            }
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            for (WebSocketListener webSocketListener : webSocketListenerArrayList) {
                webSocketListener.channelInactive(ctx);
            }
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
            /* 此处结尾无需 release，因为继承自 SimpleChannelInboundHandler，channelRead0 会被 channelRead 调用，
             * 而 SimpleChannelInboundHandler 的 channelRead 会在结尾处自动进行 release 处理。
			 */
            for (WebSocketListener webSocketListener : webSocketListenerArrayList) {
                webSocketListener.channelRead0(ctx, msg);
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            for (WebSocketListener webSocketListener : webSocketListenerArrayList) {
                webSocketListener.exceptionCaught(ctx, cause);
            }
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            for (WebSocketListener webSocketListener : webSocketListenerArrayList) {
                webSocketListener.userEventTriggered(ctx, evt);
            }
        }
    }
}
