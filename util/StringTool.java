
import java.io.UnsupportedEncodingException;

public class StringTool {

    public static String cutString(String s, int length) {
        try {
            byte[] bytes = s.getBytes("Unicode");
            int n = 0;
            int i = 2;
            for (; i < bytes.length && n < length; i++) {
                if (i % 2 == 1) {
                    n++;
                } else {
                    if (bytes[i] != 0) {
                        n++;
                    }
                }
            }

            if (i % 2 == 1) {
                if (bytes[i - 1] != 0) {
                    i = i - 1;
                } else {
                    i = i + 1;
                }
            }
            s = new String(bytes, 0, i, "Unicode");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    //计算字符串的字节长度，一个汉字两个字节长度
    //从头扫描根据字符的Ascii来获得具体的长度。如果是标准的字符，Ascii的范围是0至255，如果是汉字或其他全角字符，Ascii会大于255
    public static int getWordCount(String s) {
        int length = 0;
        try {
            for (int i = 0; i < s.length(); i++) {
                int ascii = Character.codePointAt(s, i);
                if (ascii >= 0 && ascii <= 255) {
                    length++;
                } else {
                    length += 2;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return length;
    }

    public static void main(String[] args) {
//        System.out.println("==>" + getWordCount("阿斯蒂芬："));
    }
//    public static void main(String[] args) throws UnsupportedEncodingException {
//        String s = "cddfdsfsd";
//        System.out.println(cutString(sss, 19));
////        System.out.println(s.substring(0, 6));
//    }
}
