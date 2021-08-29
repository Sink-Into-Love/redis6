package com.it.jedis;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JedisDemo {

    public static void main(String[] args) {
        //创建jedis对象
        Jedis jedis = new Jedis("192.168.52.129", 6379);

        String ping = jedis.ping();
        System.out.println(ping);
    }

    /**
     * 操作key
     */
    @Test
    public void demo1() {
        Jedis jedis = new Jedis("192.168.52.129", 6379);

        jedis.set("name", "zph");

        System.out.println(jedis.get("name")); //取值
        System.out.println(jedis.exists("name")); //是否存在
        System.out.println(jedis.ttl("name")); //过期时间

        Set<String> keys = jedis.keys("*"); //所有值
        for (String key : keys) {
            System.out.println(key);
        }
        System.out.println(keys.size());

        //设置多个可以-value
        String mset = jedis.mset("k1", "v1", "k2", "v2");
        System.out.println(mset);
        System.out.println(jedis.mget("k1", "k2"));
    }

    /**
     * 操作list
     */
    @Test
    public void demo2() {
        Jedis jedis = new Jedis("192.168.52.129", 6379);

        jedis.lpush("key1", "zph1", "zph2", "zph3");
        List<String> key1 = jedis.lrange("key1", 0, -1);

        System.out.println(key1);
    }

    /**
     * 操作set
     */
    @Test
    public void demo3() {
        Jedis jedis = new Jedis("192.168.52.129", 6379);

        //redis.clients.jedis.exceptions.JedisDataException: WRONGTYPE Operation against a key holding the wrong kind of value
//        jedis.sadd("name", "FALSE");

        jedis.sadd("non-repetitive", "TRUE1");
        jedis.sadd("non-repetitive", "TRUE2");
        jedis.sadd("non-repetitive", "TRUE3");

        Set<String> smembers = jedis.smembers("non-repetitive");
        System.out.println(smembers);
    }

    /**
     * 操作hash
     */
    @Test
    public void demo4() {
        Jedis jedis = new Jedis("192.168.52.129", 6379);

        jedis.hset("users", "age", "18");
        String hget = jedis.hget("users", "age");
        System.out.println(hget);

        Map<String,String> map = new HashMap<String,String>();
        map.put("telphone","13810169999");
        map.put("address","atguigu");
        map.put("email","abc@163.com");
        jedis.hmset("hash",map);
        List<String> result = jedis.hmget("hash", "telphone","email");
        for (String element : result) {
            System.out.println(element);
        }
    }

    /**
     * 操作zset
     */
    @Test
    public void demo5() {
        Jedis jedis = new Jedis("192.168.52.129", 6379);

        jedis.zadd("China", 100d, "Beijing");
        jedis.zadd("China", 90d, "Shanghai");
        jedis.zadd("China", 80d, "Guangzhou");
        jedis.zadd("China", 70d, "Shenzhen");
        Set<String> zrange = jedis.zrange("China", 0, -1);
        for (String e : zrange) {
            System.out.println(e);
        }
    }
}
