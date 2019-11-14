

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.java_websocket.drafts.Draft_6455;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Hello world!
 */
public class App {

    public static Logger LOGINFO = null;
    public static Logger LOGERROR = null;

    public static MsgWebSocketClient msgWebSocketClient = null;
    public static Executor fixedThreadPool = Executors.newFixedThreadPool(2);


    static {
        //初始化log4j配置文件
        Log4JConfigure.initLog4JConfigure();
        //获得log4j对象
        LOGINFO = (Logger) LogManager.getLogger("LogInfo");
        LOGERROR = (Logger) LogManager.getLogger("LogError");

    }

    public static void main(String[] args) {
        try {
            //启动定时线程，每隔一段时间更新一次LED屏幕健康的探头信息
            //每个一段时间，更新一次探头设备信息
            ScheduledExecutorService scheduExec=Executors.newScheduledThreadPool(3);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                scheduExec.shutdownNow();
                LOGINFO.info("shutdown finished!");
            }));
            // 循环任务，以上一次任务的结束时间计算下一次任务的开始时间
            scheduExec.scheduleWithFixedDelay(() -> {},1,1, TimeUnit.MINUTES);
            // 定时任务，每隔一个分钟，更新一次
            scheduExec.scheduleWithFixedDelay(() -> {},1,1, TimeUnit.MINUTES);
            // 定时任务，每隔10秒，更新一次
            Executor threadPool=Executors.newCachedThreadPool();
            scheduExec.scheduleWithFixedDelay(() -> {},1,10,TimeUnit.SECONDS);

            Thread bindThread = new Thread() {
                @Override
                public void run() {
                    WebSocketServerHandler wh = new WebSocketServerHandler(Constants.WebSocketPath);
                }
            };
            fixedThreadPool.execute(bindThread);

            Thread connectThread = new Thread() {
                @Override
                public void run() {
                    try {
                        //然后初始化websocket客户端，并建立连接，这里设置重连参数为true
                        msgWebSocketClient = new MsgWebSocketClient(new URI(Constants.TGMonitorServer_URL), new Draft_6455(), true);
                        msgWebSocketClient.setMsgWebSocketClient_old(msgWebSocketClient);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                        LOGINFO.info(e.getLocalizedMessage());
                        LOGERROR.error(e.getLocalizedMessage());
                    }
                }
            };
            fixedThreadPool.execute(connectThread);
        } catch (Exception e) {
            e.printStackTrace();
            LOGINFO.info("异常信息：" + e.getLocalizedMessage() + ",server is ready to exit");
            LOGERROR.error("异常信息：" + e.getLocalizedMessage() + ",server is ready to exit");
            System.exit(0); //程序退出
        }
    }
}
