

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class FileUtil {

    /**
     * 读取txt文件
     * @param filename
     * @return
     */
    public static String readTXT(String filename) {
        StringBuilder stringBuilder=new StringBuilder();
        try {
            FileReader fr = new FileReader(filename);
            BufferedReader bf = new BufferedReader(fr);
            String str;
            // 按行读取字符串
            while ((str = bf.readLine()) != null) {
                stringBuilder.append(str);
            }
            bf.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String res=stringBuilder.toString();
        return res;
    }
}
