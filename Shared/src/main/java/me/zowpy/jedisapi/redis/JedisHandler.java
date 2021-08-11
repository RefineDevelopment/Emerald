package me.zowpy.jedisapi.redis;

import lombok.Getter;
import me.zowpy.jedisapi.JedisAPI;
import me.zowpy.jedisapi.redis.subscription.impl.JedisListener;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * This Project is property of Zowpy © 2021
 *
 * @author Zowpy
 * Created: 8/10/2021
 * Project: JedisAPI
 */

@Getter
public class JedisHandler {

    private JedisAPI jedisAPI;
    private RedisCredentials credentials;
    private Jedis jedis;
    private JedisPool jedisPool;

    public JedisHandler(JedisAPI jedisAPI, RedisCredentials credentials) {
        this.jedisAPI = jedisAPI;
        this.credentials = credentials;
        connect();
    }

    private void connect() {
        try {
            this.jedisPool = new JedisPool(new JedisPoolConfig(), credentials.getHost(), credentials.getPort());
            this.jedis = jedisPool.getResource();
            if (credentials.isAuth()) jedis.auth(credentials.getPassword());
            new Thread(() -> jedis.subscribe(new JedisListener(jedisAPI, this),credentials.getChannel())).start();
            jedis.connect();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void write(String message) {
        try (Jedis jedis = jedisPool.getResource()) {
            this.jedis = jedis;
            if (credentials.isAuth()) jedis.auth(credentials.getPassword());
            jedis.publish(credentials.getChannel(), message);
        }finally {
            jedis.close();
        }
    }


}
