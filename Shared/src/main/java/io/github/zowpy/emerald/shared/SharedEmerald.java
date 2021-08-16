package io.github.zowpy.emerald.shared;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.github.zowpy.emerald.shared.server.EmeraldServer;
import lombok.Getter;
import lombok.Setter;
import io.github.zowpy.emerald.shared.jedis.SharedJedisSubscriber;
import io.github.zowpy.emerald.shared.manager.GroupManager;
import io.github.zowpy.emerald.shared.manager.ServerManager;
import io.github.zowpy.emerald.shared.server.ServerProperties;
import io.github.zowpy.jedisapi.JedisAPI;
import io.github.zowpy.jedisapi.redis.RedisCredentials;

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

    public static Gson GSON = new GsonBuilder().serializeNulls().create();

    private final UUID uuid;
    private final JedisAPI jedisAPI;
    private final ServerManager serverManager;
    private final GroupManager groupManager;

    @Setter private ServerProperties serverProperties;

    public SharedEmerald(UUID uuid, RedisCredentials credentials) {
        this.uuid = uuid;
        this.jedisAPI = new JedisAPI(credentials);
        jedisAPI.registerSubscriber(new SharedJedisSubscriber(this));

        this.serverManager = new ServerManager(this);
        this.groupManager = new GroupManager();

    }

    public void executeCommand(EmeraldServer server, String command) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("command", command);
        jsonObject.addProperty("uuid", server.getUuid().toString());

        jedisAPI.getJedisHandler().write("command###" + jsonObject.toString());
    }



}
