import redis.clients.jedis.Jedis;

import java.util.Map;

/**
 * redis工具类
 */
public class RedisPoolUtil {


    /**
     * 设置key的有效期，单位是秒
     *
     * @param key
     * @param exTime
     * @return
     */
    public static Long expire(String key, int exTime) {
        Jedis jedis = null;
        Long result = null;
        try {
            //从Redis连接池中获得Jedis对象
            jedis = RedisPool.getJedis();
            //设置成功则返回Jedis对象
            result = jedis.expire(key, exTime);
        } catch (Exception e) {
            LOGERROR.error("expire key:{} error", key, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    //exTime的单位是秒
    //设置key-value并且设置超时时间
    public static String setEx(String key, String value, int exTime) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.setex(key, exTime, value);
        } catch (Exception e) {
            LOGERROR.error("setex key:{} value:{} error", key, value, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static String set(String key, String value) {
        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.set(key, value);
        } catch (Exception e) {
            LOGERROR.error("set key:{} value:{} error", key, value, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static String get(String key) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            LOGERROR.error("get key:{} error", key, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static <T> T hget(String hkey,String key,Class<T> type){
        Jedis jedis=null;
        T t=null;
        try {
            jedis=RedisPool.getJedis();
            String str=jedis.hget(hkey,key);
            t=GsonUtil.jsonToObject(str,type);
        }catch (Exception e){
            LOGERROR.error("get key:{} error", key, e);
            RedisPool.returnBrokenResource(jedis);
            return t;
        }
        RedisPool.returnResource(jedis);
        return t;
    }

    public static Map<String,String> hgetAll(String key){
        Jedis jedis=null;
        Map<String,String> result=null;
        try{
            jedis=RedisPool.getJedis();
            result=jedis.hgetAll(key);
        }catch (Exception e){
            LOGERROR.error("get key:{} error", key, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static Long hset(String hkey,String key,String value){
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.hset(hkey,key, value);
        } catch (Exception e) {
            LOGERROR.error("set key:{} value:{} error", key, value, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static boolean hexists(String hkey,String key){
        Jedis jedis=null;
        boolean result=false;
        try{
            jedis=RedisPool.getJedis();
            result=jedis.hexists(hkey,key);
        }catch (Exception e){
            LOGERROR.error("get key:{} error", key, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static boolean exists(String key){
        Jedis jedis=null;
        boolean result=false;
        try{
            jedis=RedisPool.getJedis();
            result=jedis.exists(key);
        }catch (Exception e){
            LOGERROR.error("get key:{} error", key, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static Long del(String key) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            LOGERROR.error("del key:{} error", key, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

}

