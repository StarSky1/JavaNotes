

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
            //如果配置文件存在jar包之外
            //            String redisConf=System.getProperty("user.dir") + File.separator + "config" + File.separator+filename;
//            File file = new File(redisConf);
//            if (file.exists()) {
//                System.out.println("redis.properties配置文件存在，其路径为：" + file.getCanonicalPath());
//                in = new FileInputStream(file);
//                properties.load(in);
//            } else {
//                System.out.println("log4j2.xml配置文件不存在，log4j2初始化失败");
//            }
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