
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.ChannelMatcher;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;


public class ChannelGroups {
    //ChannelGroup本质上是一个set,存入里面的元素无序，不重复
    private static final ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup("ChannelGroups", GlobalEventExecutor.INSTANCE);

    public static void add(Channel channel) {
        CHANNEL_GROUP.add(channel);
    }

    //向所有的通道广播
    public static ChannelGroupFuture broadcast(Object msg) {
        return CHANNEL_GROUP.writeAndFlush(msg);
    }

    //向ChannelMatcher指定的通道广播
    // ChannelMatcher==》ChannelMatchers.all()所有的通道
    //ChannelMatcher==》ChannelMatchers.is(Channel channel) channel指定的通道
    public static ChannelGroupFuture broadcast(Object msg, ChannelMatcher matcher) {
        return CHANNEL_GROUP.writeAndFlush(msg, matcher);
    }

    public static ChannelGroup flush() {
        return CHANNEL_GROUP.flush();
    }

    public static boolean remove(Channel channel) {
        return CHANNEL_GROUP.remove(channel);
    }

    public static ChannelGroupFuture disconnect() {
        return CHANNEL_GROUP.disconnect();
    }

    public static ChannelGroupFuture disconnect(ChannelMatcher matcher) {
        return CHANNEL_GROUP.disconnect(matcher);
    }

    public static boolean contains(Channel channel) {
        return CHANNEL_GROUP.contains(channel);
    }

    public static int size() {
        return CHANNEL_GROUP.size();
    }

    public static void sendMessage(SafeBean_All safeBeanNew){
        if (ChannelGroups.size() == 0) {
            LOGINFO.info("no client connect source service currently,do nothing");
        } else {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();
            WebsocketData response = new WebsocketData();
            response.setProtocol(Constants.PROTOCOL_101);
            response.setJsonString(gson.toJson(safeBeanNew));
            ChannelGroups.broadcast(new TextWebSocketFrame(gson.toJson(response)));
            LOGINFO.info("there are " + ChannelGroups.size() + " client connect source service,now broadcast gasinfos to them");
        }
    }
}
