package me.zowpy.emerald.shared;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.github.zowpy.jedisapi.JedisAPI;
import io.github.zowpy.jedisapi.redis.RedisCredentials;
import me.zowpy.emerald.shared.server.EmeraldServer;
import lombok.Getter;
import lombok.Setter;
import me.zowpy.emerald.shared.jedis.SharedJedisSubscriber;
import me.zowpy.emerald.shared.manager.GroupManager;
import me.zowpy.emerald.shared.manager.ServerManager;
import me.zowpy.emerald.shared.server.ServerProperties;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Setter 
    private ServerProperties serverProperties;

    public SharedEmerald(UUID uuid, RedisCredentials credentials) {
        this.uuid = uuid;
        
        this.serverManager = new ServerManager(this);
        this.groupManager = new GroupManager();
        
        (this.jedisAPI = new JedisAPI(credentials)).registerSubscriber(new SharedJedisSubscriber(this));
    }

    public void executeCommand(EmeraldServer server, String command) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("command", command);
        jsonObject.addProperty("uuid", server.getUuid().toString());

        jedisAPI.getJedisHandler().write("command###" + jsonObject);
    }

    /**
     * Get all online admin users.
     */
    public List<Player> getAdminUsers() {
        return Bukkit
                .getServer()
                .getOnlinePlayers()
                .stream()
                .filter(p -> p.hasPermission("emerald.admin"))
                .collect(Collectors.toList());
    }

}
