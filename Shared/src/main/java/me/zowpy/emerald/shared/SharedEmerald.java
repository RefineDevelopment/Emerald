package me.zowpy.emerald.shared;

import lombok.Getter;
import me.zowpy.emerald.EmeraldPlugin;
import me.zowpy.emerald.shared.jedis.SharedJedisSubscriber;
import me.zowpy.emerald.shared.manager.ServerManager;
import me.zowpy.jedisapi.JedisAPI;
import me.zowpy.jedisapi.redis.RedisCredentials;

import java.util.UUID;

/**
 * This Project is property of Zowpy © 2021
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 8/10/2021
 * Project: Emerald
 */

@Getter
public class SharedEmerald {

    private final UUID uuid;
    private final JedisAPI jedisAPI;
    private final ServerManager serverManager;
    private final EmeraldPlugin emeraldPlugin;

    public SharedEmerald(UUID uuid, EmeraldPlugin emeraldPlugin, RedisCredentials credentials) {
        this.uuid = uuid;
        this.emeraldPlugin = emeraldPlugin;
        this.jedisAPI = new JedisAPI(credentials);
        jedisAPI.registerSubscriber(new SharedJedisSubscriber(this));

        this.serverManager = new ServerManager(this);

    }

}
