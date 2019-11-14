

import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    //加密
    public static String EncoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
//        MessageDigest md5=MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        //加密后的字符串
//        return base64en.encode(md5.digest(str.getBytes("utf-8")));
        return base64en.encode(MD5(str).getBytes("utf-8"));

    }
//    public static String encode(String text){
//        try {
//            MessageDigest digest =MessageDigest.getInstance("md5");
//            byte [] result=digest.digest(text.getBytes());
//            StringBuilder sb=new StringBuilder();
//            for(byte b:result){
//                int number=b&0xff;
//                String hex=Integer.toHexString(number);
//                if(hex.length()==1){
//                    sb.append("0"+hex);
//                }else {
//                    sb.append(hex);
//                }
//            }
//            return sb.toString();
//        } catch (NoSuchAlgorithmException e) {
//
//            return "";
//        }
//    }

    //验证
    public static boolean verify(String pwd1,String pwd2) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        return EncoderByMd5(pwd1).equals(pwd2);
    }
    private static String MD5(String password) {
        try {
            // 得到一个信息摘要器
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(password.getBytes());
            StringBuffer buffer = new StringBuffer();
            // 把没一个byte 做一个与运算 0xff;
            for (byte b : result) {
                // 与运算
                int number = b & 0xff;// 加盐
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }

            // 标准的md5加密后的结果
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
    public static void main(String[] args)throws Exception{
        System.out.println("test:" + EncoderByMd5("123"));
    }
}
