


public class PathUtil {
    /**
     * 1.获取当前类文件所在路径
     * /root/payara5/glassfish/domains/domain1/applications/webServer/WEB-INF/classes/com/yj/util/
     **/
    public static String getWebRootPath() {
        String webRootPath = PathUtil.class.getResource("").getPath().replaceAll("%20", " ");
        return webRootPath;
    }

    /**
     * 2.获取WEB-INF/ 上一级目录
     * /root/payara5/glassfish/domains/domain1/applications/webServer_war/
     */
    public static String getWebInfPath() {
        String webRootPath = PathUtil.class.getResource("").getPath().replaceAll("%20", " ");
        String webInfPath = webRootPath.substring(0, webRootPath.indexOf("WEB-INF"));
        return webInfPath;
    }

    /**
     * 2.获取classes/ 目录
     * /root/payara5/glassfish/domains/domain1/applications/webServer_war/WEB-INF/classes/
     */
    public static String getWebClassPath() {
        String webRootPath = PathUtil.class.getResource("").getPath().replaceAll("%20", " ");
        String webInfPath = webRootPath.substring(0, webRootPath.indexOf("classes") + 8);
        return webInfPath;
    }

    /*
    * 3.获取payara上的docroot路径
    * */
    public static String getWebDocRootPath() {
        String webRootPath = PathUtil.class.getResource("").getPath().replaceAll("%20", " ");
        String webInfPath = webRootPath.substring(0, webRootPath.indexOf("applications")) + "docroot";
        return webInfPath;
    }
}
