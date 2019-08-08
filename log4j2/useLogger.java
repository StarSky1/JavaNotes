//使用log4j的方法
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

	public static Logger LOGINFO = null;
    public static Logger LOGERROR = null;

    //初始化log4j配置文件
    Log4JConfigure.initLog4JConfigure();
    //获得log4j对象
    LOGINFO = (Logger) org.apache.logging.log4j.LogManager.getLogger("LogInfo");
    LOGERROR = (Logger) LogManager.getLogger("LogError");