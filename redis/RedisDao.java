package com.example.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisDao {
	@Autowired
    private StringRedisTemplate template;

    public  void setKey(String key,String value){
        ValueOperations<String, String> ops = template.opsForValue();
        ops.set(key,value,7, TimeUnit.DAYS);//1小时过期
    }

    public String getValue(String key){
        ValueOperations<String, String> ops = this.template.opsForValue();
        return ops.get(key);
    }

    /**
     * 获取hash中field对应的值
     *
     * @param key
     * @param field
     * @return
     */
    public String hget(String key, String field) {
        Object val = template.opsForHash().get(key, field);
        return val == null ? null : val.toString();
    }

    /**
     * 添加or更新hash的值
     *
     * @param key
     * @param field
     * @param value
     */
    public void hset(String key, String field, String value) {
        template.opsForHash().put(key, field, value);
    }


}

