/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.util.Properties;

/**
 * @author Dolphin
 */
public class JConstS {

    private static final Properties properties = new Properties();

    static {
        try {
            //  System.getProperty("user.dir")代表src里面的java文件生成jar包后的工作目录
            //  System.out.println("JConstS对应的工程目录==>" + System.getProperty("user.dir"));
            //这里也可以使用类的运行目录来解决相对路径的问题
            //File file = new File(System.getProperty("user.dir") + File.separator + "config" + File.separator + "config.properties");
            //使用类的相对路径，获取代码编译后的classes目录位置
            File file=new File(JConstS.class.getResource("/").getFile()+ "config.properties");
            //System.out.println("sss==>"+file.getAbsolutePath());
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
            properties.load(new BufferedReader(isr));
            // properties.load(new FileInputStream(new File(System.getProperty("user.dir") + "/config/config.properties")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getValue(String key) {
        String value = (String) properties.get(key);
        return value;
    }

    //public static void main(String[] args) {
    //    System.out.println("==>" + System.getProperty("user.dir"));
    //}
}
