package com.taotao.rest.dao.impl;

import com.taotao.rest.dao.JedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;


public class JedisClientCluster implements JedisClient {

    @Autowired
    private JedisCluster jedisCluster;

    @Override
    public String get(String key) {
        String string = jedisCluster.get(key);
        return string;
    }

    @Override
    public String set(String key, String value) {
        String set = jedisCluster.set(key, value);
        return set;
    }

    @Override
    public String hget(String hash, String key) {
        String hget = jedisCluster.hget(hash, key);
        return hget;
    }

    @Override
    public Long hset(String hash, String key,String value) {
        Long hset = jedisCluster.hset(hash, key, value);
        return hset;
    }

    @Override
    public long incr(String key) {
        Long incr = jedisCluster.incr(key);
        return incr;
    }

    @Override
    public long expire(String key, long second) {
        Long expire = jedisCluster.expire(key, (int) second);
        return expire;
    }

    @Override
    public long ttl(String key) {
        Long ttl = jedisCluster.ttl(key);
        return ttl;
    }

    @Override
    public long del(String key) {
        return jedisCluster.del(key);
    }

    @Override
    public long hdel(String hkey,String key) {
        return jedisCluster.hdel(hkey,key);
    }
}
