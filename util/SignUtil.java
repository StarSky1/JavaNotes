

/**
 * 签名验证类
 * Created by 老肖 on 2018/12/14.
 */
public class SignUtil {

    public static String produceSign() {
        String token = null;
        try {
            //参数1:nameid  2:num 3:content    注意如果String类型没有就填空字串  本令牌过期时间暂为24小时
            token = JwtToken.createToken("王test", 99, "somethings");
            LOGINFO.info("token===>" + token);
        } catch (Exception e) {
            e.printStackTrace();
            LOGINFO.info(e.getLocalizedMessage());
            LOGERROR.error(e.getLocalizedMessage());
        }
        return token;
    }

    public static void main(String[] args) {
        System.out.println("==>" + produceSign());
    }
}
