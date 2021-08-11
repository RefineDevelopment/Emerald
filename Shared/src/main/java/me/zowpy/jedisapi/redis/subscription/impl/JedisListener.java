package me.zowpy.jedisapi.redis.subscription.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import me.zowpy.jedisapi.JedisAPI;
import me.zowpy.jedisapi.redis.JedisHandler;
import me.zowpy.jedisapi.redis.subscription.IncomingMessage;
import me.zowpy.jedisapi.redis.subscription.JedisSubscriber;
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
            System.out.println(args[1]);
            executor.execute(() -> {

                if (args.length != 2) return;

                if (!jedisAPI.getPayloads().containsKey(args[0])) return;

                JedisSubscriber subscriber = jedisAPI.getPayloads().get(args[0]);

                if (subscriber == null) return;

                JsonObject object = JsonParser.parseString(args[1]).getAsJsonObject();

                for (Method method : jedisAPI.getExecutors().get(subscriber)) {
                    IncomingMessage incomingMessage = method.getAnnotation(IncomingMessage.class);

                    if (incomingMessage.payload().equalsIgnoreCase(args[0])) {
                        try {
                            method.setAccessible(true);
                            method.invoke(subscriber, object);
                            method.setAccessible(false);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }

            });
        }
    }
}
