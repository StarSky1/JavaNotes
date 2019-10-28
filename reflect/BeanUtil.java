
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * @author 杨靖
 * @CreatedAt 2019年10月28日 14点54分
 * @Description 处理JavaBean的工具类
 */
public class BeanUtil {

    public static <T> void copyBean(T source, T target) {
        Class classz = source.getClass();
        Field[] fields = classz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String name = field.getName();
            try {
//                Method getMethod = classz.getMethod("get" + name.substring(0, 1).toUpperCase()
//                        + name.substring(1), null);
//                Method setMethod = classz.getMethod("set" + name.substring(0, 1).toUpperCase()
//                        + name.substring(1), field.getType());
                //为避免字符串拼接，可以通过PropertyDescriptor反射得到fName字段的get、set方法
                //通过getReadMethod()方法调用类的get函数
                //通过getWriteMethod()方法来调用类的set方法
                PropertyDescriptor descriptor = new PropertyDescriptor(name, classz);
                Method getMethod=descriptor.getReadMethod();
                Method setMethod=descriptor.getWriteMethod();
                Object value = getMethod.invoke(source);
                setMethod.invoke(target, value);
            } catch (Exception e) {
                LOGINFO.error("copyBean异常",e);
                LOGERROR.error("copyBean异常",e);
            }
        }
    }

    public static void main(String[] args) {
        ABean source=new ABean();
        source.setID("test");
        ABean target=new ABean();
        copyBean(source,target);
        System.out.println(target.getID());
    }
}
