
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    //加密
    public static String EncoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5=MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        //加密后的字符串
        return base64en.encode(md5.digest(str.getBytes("utf-8")));
    }

    //验证
    public static boolean verify(String pwd1,String pwd2) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        return EncoderByMd5(pwd1).equals(pwd2);
    }

    //public static void main(String[] args)throws Exception{
    //    System.out.println("test:" + EncoderByMd5("123"));
    //    System.out.println("test:" + EncoderByMd5("123"));
    //    System.out.println("==>"+verify("sss","sss"));
    //}
}
