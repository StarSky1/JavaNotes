package com.example.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;


@Component
public class RedisClient {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /** ---------------------------------- redis消息队列 ---------------------------------- */
    /**
     * 存值
     * @param key 键
     * @param value 值
     * @return
     */
    public boolean lpush(String key, String value) {
        try {
            redisTemplate.opsForList().leftPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 取值 - <rpop：非阻塞式>
     * @param key 键
     * @return
     */
    public String rpop(String key) {
        try {
            return redisTemplate.opsForList().rightPop(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 取值 - <brpop：阻塞式> - 推荐使用
     * @param key 键
     * @param timeout 超时时间
     * @param timeUnit 给定单元粒度的时间段
     *                 TimeUnit.DAYS          //天
     *                 TimeUnit.HOURS         //小时
     *                 TimeUnit.MINUTES       //分钟
     *                 TimeUnit.SECONDS       //秒
     *                 TimeUnit.MILLISECONDS  //毫秒
     * @return
     */
    public String brpop(String key, long timeout, TimeUnit timeUnit) {
        try {
            return redisTemplate.opsForList().rightPop(key, timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查看值
     * @param key 键
     * @param start 开始
     * @param end 结束 0 到 -1代表所有值
     * @return
     */
    public List<String> lrange(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
