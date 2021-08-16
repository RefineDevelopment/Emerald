package io.github.zowpy.jedisapi.redis.subscription.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.zowpy.jedisapi.JedisAPI;
import lombok.AllArgsConstructor;
import io.github.zowpy.jedisapi.redis.JedisHandler;
import io.github.zowpy.jedisapi.redis.subscription.IncomingMessage;
import io.github.zowpy.jedisapi.redis.subscription.JedisSubscriber;
import redis.clients.jedis.JedisPubSub;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * This Project is property of Zowpy © 2021
 *
 * @author Zowpy
 * Created: 8/10/2021
 * Project: JedisAPI
 */

@AllArgsConstructor
public class JedisListener extends JedisPubSub {

    private JedisAPI jedisAPI;
    private JedisHandler jedisHandler;

    @Override
    public void onMessage(String channel, String message) {
        if (channel.equals(jedisHandler.getCredentials().getChannel())) {
            Executor executor = Executors.newFixedThreadPool(1);
            String[] args = message.split("###");
            executor.execute(() -> {

                if (args.length != 2) return;

                if (!jedisAPI.getPayloads().containsKey(args[0])) return;

                JedisSubscriber subscriber = jedisAPI.getPayloads().get(args[0]);

                if (subscriber == null) return;

                for (Method method : jedisAPI.getExecutors().get(subscriber)) {
                    IncomingMessage incomingMessage = method.getAnnotation(IncomingMessage.class);

                    if (incomingMessage.payload().equalsIgnoreCase(args[0])) {
                        try {
                            if (method.getParameters().length == 0) {
                                if (!method.isAccessible()) method.setAccessible(true);
                                method.invoke(subscriber);
                                return;
                            }
                            JsonObject object = new JsonParser().parse(args[1]).getAsJsonObject();
                            if (!method.isAccessible()) method.setAccessible(true);
                            method.invoke(subscriber, object);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }

            });
        }
    }
}
