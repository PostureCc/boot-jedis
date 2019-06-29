package com.chan.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

/**
 * @Auther: Chan
 * @Date: 2019/6/29 16:19
 * @Description:
 */
@Component
public class JedisService {

    @Value("${shiro.expireTime}")
    private int expireTime;

    private static final int OUT_TIME = 1000 * 60 * 2;

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.database}")
    private int database;

    /**
     * 获取连接
     */
    public Jedis newJedis() {
        Jedis jedis = new Jedis(host, port, OUT_TIME);
//        if (StringUtils.isNotEmpty(password)) jedis.auth(password);
        jedis.auth(password);
        jedis.select(database);
        return jedis;
    }

    /**
     * 关闭连接
     */
    public void closeJedis(Jedis jedis) {
        jedis.close();
    }

    /**
     * 保存（超时时间为系统登录失效时间）
     *
     * @param key   键
     * @param value 值（字符串）
     */
    public void setJedis(String key, String value) {
        setJedis(key, value, expireTime);
    }

    /**
     * 保存
     * OUT_TIME 连接超时时间
     *
     * @param key
     * @param value
     * @param expireTime 数据超时时间（秒），为null时永久有效/button/edit
     */
    public void setJedis(String key, String value, Integer expireTime) {
        Jedis jedis = newJedis();
        jedis.set(key, value);
        if (expireTime != null) {
            jedis.expire(key, expireTime);
        }
        jedis.close();
    }


    /**
     * 保存（根据条件是否覆盖）
     * EX是秒，PX是毫秒
     * OUT_TIME 连接超时时间
     *
     * @param key
     * @param value
     * @param nxxx       NX是不存在时才set， XX是存在时才set
     * @param expireTime
     */
    public void setJedis(String key, String value, String nxxx, Integer expireTime) {
        Jedis jedis = newJedis();
        if (expireTime != null) {
            jedis.set(key, value, nxxx, "EX", expireTime);
        } else {
            jedis.set(key, value);
        }
        jedis.close();
    }


    /**
     * 判断key是否存在
     *
     * @param key
     * @return
     */
    public Boolean exists(String key) {
        Jedis jedis = newJedis();
        Boolean exists = jedis.exists(key);
        jedis.close();
        return exists;
    }


    /**
     * 根据key获取
     *
     * @param key
     * @return
     */
    public String getJedis(String key) {
        Jedis jedis = newJedis();
        String res = jedis.get(key);
        jedis.close();
        return res;
    }


    /**
     * 根据key删除
     *
     * @param key
     */
    public void delJedis(String... key) {
        Jedis jedis = newJedis();
        jedis.del(key);
        jedis.close();
    }

    /**
     * 根据key删除
     *
     * @param key
     */
    public void delJedis(String key) {
        Jedis jedis = newJedis();
        jedis.del(key);
        jedis.close();
    }


    /**
     * 根据key模糊查询keys
     *
     * @param key
     * @return
     */
    public Set<String> likeKeys(String key) {
        Jedis jedis = newJedis();
        Set<String> keys = jedis.keys(key);
        jedis.close();
        return keys;
    }

    /**
     * 获取key的过期时间
     */
    public Long getTtl(String key) {
        Jedis jedis = newJedis();
        Long ttl = jedis.ttl(key);
        jedis.close();
        return ttl;
    }

}
