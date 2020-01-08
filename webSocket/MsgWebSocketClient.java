
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;



public class MsgWebSocketClient extends WebSocketClient {

    //是否自动重连功能，默认情况下，不开启客户端重连服务的功能
    private boolean reconnectToServer = false;
    private boolean isConnectted = false;

    private URI serverUri;
    private Draft protocolDraft;
    private MsgWebSocketClient msgWebSocketClient_old;
    private String url;

    private Gson gson;

    //计算websocket客户端重连次数，全局属性
    private static int reconnectCount = 0;

    public MsgWebSocketClient(URI serverUri, Draft protocolDraft, boolean reconnectToServer) {
        super(serverUri, protocolDraft);
        this.serverUri = serverUri;
        this.protocolDraft = protocolDraft;
        this.reconnectToServer = reconnectToServer;
        this.url = "ws://" + serverUri.getHost() + ":" + serverUri.getPort() + serverUri.getPath();

        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();

        ////初始化客户端对象后，进行连接
        MsgWebSocketClient.this.connect();
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        LOGINFO.info("client connect websocket server:" + url + " sucessfully");
        isConnectted = true;

        //重连成功，计数重置
        resetReconnectCount();
    }

    @Override
    public void onMessage(String s) {
        LOGINFO.info("");
        LOGINFO.info("client received message:" + s);
        DataHandler.handleData(s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        //当MsgWebSocketClient断开与Server的连接时，onClose方法大约每隔一秒触发一次
        LOGINFO.info("connection betwween websocket client with server:" + url + " is closed");
        isConnectted = false;

        //启动websocket客户端重连线程，等待5s后进行重连
        ReconnectThread reconnectThread = new ReconnectThread(serverUri, protocolDraft, msgWebSocketClient_old,reconnectToServer);
        reconnectThread.start();
    }

    @Override
    public void onError(Exception e) {
        LOGINFO.info("server:" + url + " send the error:" + e.getLocalizedMessage());
    }

    public boolean isConnectted() {
        return isConnectted;
    }

    public void setConnectted(boolean connectted) {
        isConnectted = connectted;
    }

    public boolean isReconnectToServer() {
        return reconnectToServer;
    }

    public void setReconnectToServer(boolean reconnectToServer) {
        this.reconnectToServer = reconnectToServer;
    }

    public MsgWebSocketClient getMsgWebSocketClient_old() {
        return msgWebSocketClient_old;
    }

    public void setMsgWebSocketClient_old(MsgWebSocketClient msgWebSocketClient_old) {
        this.msgWebSocketClient_old = msgWebSocketClient_old;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    //重连成功，计数重置
    public void resetReconnectCount() {
        if (reconnectCount != 0) {
            reconnectCount = 0;
        }
    }

    //关闭websocket连接，销毁websocket客户端
    public void destroyWebSocketClient() {
        if (msgWebSocketClient_old != null) {
            LOGINFO.info("client active close the connection with server:" + url + ",and destroy websocket client");
            msgWebSocketClient_old.setReconnectToServer(false);
            msgWebSocketClient_old.close();
            msgWebSocketClient_old = null;
        }
    }

    //客户端重连线程
    private class ReconnectThread extends Thread {
        private URI serverUri_new;
        private Draft protocolDraft_new;

        private MsgWebSocketClient myMsgWebSocketClient;
        private boolean reconnectToServer_new;

        public ReconnectThread(URI serverUri, Draft protocolDraft, MsgWebSocketClient myMsgWebSocketClient, boolean reconnectToServer_new) {
            this.serverUri_new = serverUri;
            this.protocolDraft_new = protocolDraft;
            this.myMsgWebSocketClient = myMsgWebSocketClient;
            this.reconnectToServer_new = reconnectToServer_new;
        }

        @Override
        public void run() {
            try {
                //重连计数增加
                reconnectCount++;
                LOGINFO.info("websocket客户端第 " + reconnectCount + " 次重连服务，ReconnectThread线程先休眠5秒");
                Thread.currentThread().sleep(5000);

                if (myMsgWebSocketClient != null) {
                    //myMsgWebSocketClient.setReconnectToServer(false);
                    myMsgWebSocketClient.close();
                    myMsgWebSocketClient = null;
                }
                myMsgWebSocketClient = new MsgWebSocketClient(serverUri_new, protocolDraft_new, reconnectToServer_new);
                myMsgWebSocketClient.setMsgWebSocketClient_old(myMsgWebSocketClient);
            } catch (Exception e) {
                e.printStackTrace();
                LOGINFO.info(e.getLocalizedMessage());
                LOGERROR.error(e.getLocalizedMessage());
            }
        }
    }
}