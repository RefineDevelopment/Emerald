package io.github.zowpy.jedisapi;

import lombok.Getter;
import io.github.zowpy.jedisapi.redis.JedisHandler;
import io.github.zowpy.jedisapi.redis.RedisCredentials;
import io.github.zowpy.jedisapi.redis.subscription.IncomingMessage;
import io.github.zowpy.jedisapi.redis.subscription.JedisSubscriber;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This Project is property of Zowpy © 2021
 *
 * @author Zowpy
 * Created: 8/10/2021
 * Project: JedisAPI
 */

@Getter
public class JedisAPI {

    private final RedisCredentials credentials;
    private final JedisHandler jedisHandler;
    public final List<JedisSubscriber> jedisSubscribers = new ArrayList<>();
    private final Map<JedisSubscriber, Method[]> executors = new HashMap<>();
    private final Map<String, JedisSubscriber> payloads = new HashMap<>();

    public JedisAPI(RedisCredentials credentials) {
        this.credentials = credentials;
        this.jedisHandler = new JedisHandler(this, credentials);
    }

    public void registerSubscriber(JedisSubscriber jedisSubscriber) {
        Class<? extends JedisSubscriber> clazz = jedisSubscriber.getClass();
        List<Method> methods = new ArrayList<>();
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(IncomingMessage.class)) {
                methods.add(method);
                IncomingMessage message = method.getAnnotation(IncomingMessage.class);
                payloads.put(message.payload(), jedisSubscriber);
            }
        }

        jedisSubscribers.add(jedisSubscriber);
        executors.put(jedisSubscriber, methods.toArray(new Method[0]));
    }
}
