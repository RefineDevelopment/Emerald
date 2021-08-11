package me.zowpy.emerald.shared.jedis;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import me.zowpy.emerald.shared.SharedEmerald;
import me.zowpy.emerald.shared.server.EmeraldServer;
import me.zowpy.emerald.shared.server.ServerProperties;
import me.zowpy.jedisapi.redis.subscription.IncomingMessage;
import me.zowpy.jedisapi.redis.subscription.JedisSubscriber;

import java.util.UUID;

/**
 * This Project is property of Zowpy © 2021
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 8/10/2021
 * Project: Emerald
 */

@AllArgsConstructor
public class SharedJedisSubscriber extends JedisSubscriber {

    private SharedEmerald emerald;

    @IncomingMessage(payload = "updateservers")
    public void updateServers(JsonObject object) {
        emerald.getServerManager().updateServers();
    }

    @IncomingMessage(payload = "updateserver")
    public void updateServer(JsonObject object) {
        EmeraldServer server = emerald.getServerManager().getByUUID(UUID.fromString(object.get("uuid").getAsString()));

        if (server != null) {
            emerald.getServerManager().updateServer(server, SharedEmerald.GSON.fromJson(object.get("properties").getAsString(), ServerProperties.class));
        }
    }
}
