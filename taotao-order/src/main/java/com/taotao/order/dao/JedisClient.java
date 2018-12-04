package com.taotao.order.dao;

public interface JedisClient {
    String get(String key);
    String set(String key,String value); // set key1 123
    Long hset(String hash,String key,String value); // hget hash1 key1
    String hget(String hash,String key); // hget hash1 key1 123
    long incr(String key); // incr a
    long expire(String key, long second); // expire key 2000
    long ttl(String key);// 有效期  -2  过时  -1：永久
    long del(String key);
    long hdel(String hkey,String key);

}
