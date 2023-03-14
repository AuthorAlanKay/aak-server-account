package org.aak.server.account.utils;

import cn.hutool.db.nosql.redis.RedisDS;
import redis.clients.jedis.Jedis;

public class RedisUtils {

    public static final Jedis JEDIS = RedisDS.create().getJedis();
}
