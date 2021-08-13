package me.zowpy.emerald;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import me.zowpy.emerald.command.ServerInfoCommand;
import me.zowpy.emerald.command.ServersCommand;
import me.zowpy.emerald.shared.SharedEmerald;
import me.zowpy.emerald.shared.server.ServerProperties;
import me.zowpy.emerald.shared.server.ServerStatus;
import me.zowpy.emerald.task.ServerUpdateTask;
import me.zowpy.emerald.utils.ConfigFile;
import me.zowpy.jedisapi.redis.RedisCredentials;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;

import java.util.UUID;

@Getter
public class EmeraldPlugin extends JavaPlugin {

    @Getter private static EmeraldPlugin instance;
    public static Gson GSON = new GsonBuilder().serializeNulls().create();

    private Jedis jedis;

    private SharedEmerald sharedEmerald;
    private ServerProperties serverProperties;

    private ServerUpdateTask serverUpdateTask;

    private ConfigFile settingsFile;

    @Override
    public void onEnable() {
        instance = this;

        this.settingsFile = new ConfigFile(this, "settings");

        /*  Create current server properties  */

        serverProperties = new ServerProperties();
        serverProperties.setServerStatus(getServer().hasWhitelist() ? ServerStatus.WHITELISTED : ServerStatus.ONLINE);
        serverProperties.setIp(getServer().getIp());
        serverProperties.setPort(getServer().getPort());
        serverProperties.setName(settingsFile.getConfig().getString("server-name"));
        serverProperties.setOnlinePlayers(getServer().getOnlinePlayers().size());
        serverProperties.setMaxPlayers(getServer().getMaxPlayers());

        UUID uuid;
        if (settingsFile.getConfig().getString("server-uuid").equalsIgnoreCase("null")) {
            uuid = UUID.randomUUID();
            settingsFile.getConfig().set("server-uuid", uuid.toString());
            settingsFile.save();
        }else {
            uuid = UUID.fromString(settingsFile.getConfig().getString("server-uuid"));
        }

        serverProperties.setUuid(uuid);

        /*  Create SharedEmerald instance  */
        sharedEmerald = new SharedEmerald(uuid, new RedisCredentials(
                settingsFile.getConfig().getString("redis.host"),
                settingsFile.getConfig().getString("redis.auth.password"),
                "EMERALD:BUKKIT",
                settingsFile.getConfig().getInt("redis.port"),
                settingsFile.getConfig().getBoolean("redis.auth.enabled")
        ));

        sharedEmerald.setServerProperties(serverProperties);
        /*  Create the current server to redis cache  */
        sharedEmerald.getServerManager().createServer();

        sharedEmerald.getServerManager().updateServers();

        jedis = sharedEmerald.getJedisAPI().getJedisHandler().getJedisPool().getResource();

        serverUpdateTask = new ServerUpdateTask();

        getCommand("serverinfo").setExecutor(new ServerInfoCommand());
        getCommand("servers").setExecutor(new ServersCommand());


    }

    @Override
    public void onDisable() {
        serverUpdateTask.cancel();

        sharedEmerald.getServerManager().setOffline(sharedEmerald.getServerManager().getByUUID(serverProperties.getUuid()), jedis);

        instance = null;
    }
}
