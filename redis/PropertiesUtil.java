

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;



public class PropertiesUtil {

    /**
     * 加载配置文件
     * @param filename
     * @return
     */
    public static Properties loadProperties(String filename) {
        Properties properties = new Properties();
        InputStream in = null;
        try {
            in = PropertiesUtil.class
                    .getResourceAsStream(filename);
            properties.load(in);
        } catch (IOException e) {
            LOGINFO.error("读取配置文件失败：",e);
            LOGERROR.error("读取配置文件失败：",e);
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    LOGINFO.error("读取配置文件失败：",e);
                    LOGERROR.error("读取配置文件失败：",e);
                }
        }
        return properties;
    }

}