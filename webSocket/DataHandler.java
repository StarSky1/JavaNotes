
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;


public class GasDataHandler {

    private static Gson gson;

    static {
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();
    }

    public static void handleData(String s){
        try {
            //遍历ChannelGroup里面的client，根据client里面不同的协议，对数据做不同的处理并转发
            if (ChannelGroup.hasDataRequestFromClient()) {
                DataParseAndCut dataParseAndCut = new DataParseAndCut(s);
                for (ChannelBean channelBean : ChannelGroup.getChannelBeanList()) {
                    if (channelBean.getProtocolBeanList().size() != 0) {
                        //当channelBean里面没有协议请求时，不做任何处理
                        for (int i = 0; i < channelBean.getProtocolBeanList().size(); i++) {
                            int protocol = channelBean.getProtocolBeanList().get(i).getProtocol();
                            String screenID = channelBean.getProtocolBeanList().get(i).getScreenID();
                            LOGINFO.info("client:" + channelBean.getChannel().remoteAddress() + " protocol:" + protocol + " screenID:" + screenID);
                            switch (protocol) {
                                case:
                                    
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            } else {
                LOGINFO.info("there is no data request from client,do nothing");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGINFO.info(e.getLocalizedMessage());
            LOGERROR.error(e.getLocalizedMessage());
        }
    }
}
