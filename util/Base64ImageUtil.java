
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * 图片与base64字符串的相互转换
 * 注意：如果图片过大,转成字符串,无法转换完全,因为字符串有最大长度限制65534字节，
 * 所以通过base64字符串传递图片时，我们需要限定图片文件的大小
 * Created by 老肖 on 2019/8/28.
 */
public class Base64ImageUtil {

    //本地图片转化成base64字符串
    public static String convertLocalImageToBase64Str(String imgFile) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        FileInputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        String base64Str = encoder.encode(data).replaceAll("\r|\n", "");
        //LOGINFO.info("base64Str==>" + base64Str);
        return base64Str;
    }

    // base64字符串转化成图片
    public static boolean convertBase64StrToImage(String imgStr, String imgFilePath) {
        if (imgStr == null || !isBase64(imgStr)) {
            // 图像数据为空或者非有效的based4字符串
            return false;
        }

        OutputStream out = null;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            // Base64解码,对字节数组字符串进行Base64解码并生成图片
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    // 调整异常数据
                    b[i] += 256;
                }
            }

            File file = new File(imgFilePath);
            if (!file.exists()) {
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                file.createNewFile();
            }

            //新生成的图片
            out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //网络图片转换成base64字符串
    public static String convertNetImageToBase64Str(String netImagePath) {
        ByteArrayOutputStream data = new ByteArrayOutputStream();

        InputStream is = null;
        try {
            // 创建URL
            URL url = new URL(netImagePath);
            byte[] by = new byte[1024];
            // 创建链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            is = conn.getInputStream();
            // 将内容读取内存中
            int len = -1;
            while ((len = is.read(by)) != -1) {
                data.write(by, 0, len);
            }

            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        String base64Str = encoder.encode(data.toByteArray()).replaceAll("\r|\n", "");
        //LOGINFO.info("base64Str==>" + base64Str);
        return base64Str;
    }

    public static boolean isBase64(String str) {
        String base64Pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        return Pattern.matches(base64Pattern, str);
    }
}
