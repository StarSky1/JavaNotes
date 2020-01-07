
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Properties;

public class RedisPool {
    private static JedisPool pool;//jedis连接池
    private static Properties properties= PropertiesUtil.loadProperties("redis.properties");
    private static Integer maxTotal = Integer.parseInt(properties.getProperty("redis.max.total","20")); //最大连接数
    private static Integer maxIdle = Integer.parseInt(properties.getProperty("redis.max.idle","20"));//在jedispool中最大的idle状态(空闲的)的jedis实例的个数
    private static Integer minIdle = Integer.parseInt(properties.getProperty("redis.min.idle","20"));//在jedispool中最小的idle状态(空闲的)的jedis实例的个数

    private static Boolean testOnBorrow = Boolean.parseBoolean(properties.getProperty("redis.test.borrow","true"));//在borrow一个jedis实例的时候，是否要进行验证操作，如果赋值true。则得到的jedis实例肯定是可以用的。
    private static Boolean testOnReturn = Boolean.parseBoolean(properties.getProperty("redis.test.return","true"));//在return一个jedis实例的时候，是否要进行验证操作，如果赋值true。则放回jedispool的jedis实例肯定是可以用的。

    private static String redisIp = properties.getProperty("redis.ip");
    private static Integer redisPort = Integer.parseInt(properties.getProperty("redis.port"));


    private static void initPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);

        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);

        config.setBlockWhenExhausted(true);//连接耗尽的时候，是否阻塞，false会抛出异常，true阻塞直到超时。默认为true。

        pool = new JedisPool(config,redisIp,redisPort,1000*2);
    }

    //静态代码块，初始化Redis池
    static{
        initPool();
    }

    public static Jedis getJedis(){
        Jedis jedis=pool.getResource();
        //默认使用db0数据库，这里，我们手动选择db0数据库，方便后期更改
        jedis.select(0);
        return jedis;
    }


    public static void returnBrokenResource(Jedis jedis){
        jedis.close();
    }

    public static void returnResource(Jedis jedis){
        jedis.close();
    }

    public static void closePool(){
        pool.close();
    }

}
