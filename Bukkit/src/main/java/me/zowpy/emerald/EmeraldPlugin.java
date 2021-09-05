package me.zowpy.emerald;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import me.zowpy.emerald.command.ServerInfoCommand;
import me.zowpy.emerald.shared.SharedEmerald;
import me.zowpy.emerald.shared.server.EmeraldGroup;
import me.zowpy.emerald.shared.server.ServerProperties;
import me.zowpy.emerald.shared.server.ServerStatus;
import me.zowpy.emerald.shared.util.TPSUtility;
import me.zowpy.emerald.task.ServerUpdateTask;
import me.zowpy.emerald.utils.ConfigFile;
import me.zowpy.emerald.utils.IPUtil;
import io.github.zowpy.jedisapi.redis.RedisCredentials;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;

import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class EmeraldPlugin extends JavaPlugin {

    @Getter private static EmeraldPlugin instance;
    public final static Gson GSON = new GsonBuilder().serializeNulls().create();

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
        serverProperties.setIp(IPUtil.getIP());
        serverProperties.setPort(getServer().getPort());
        serverProperties.setName(settingsFile.getConfig().getString("server-name"));
        serverProperties.setOnlinePlayers(getServer().getOnlinePlayers().stream().map(Entity::getUniqueId).collect(Collectors.toList()));
        serverProperties.setWhitelistedPlayers(getServer().getWhitelistedPlayers().stream().map(OfflinePlayer::getUniqueId).collect(Collectors.toList()));
        serverProperties.setMaxPlayers(getServer().getMaxPlayers());
        serverProperties.setTps(Double.parseDouble(TPSUtility.getTPS()));

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

        for (String s : settingsFile.getConfig().getStringList("server-groups")) {
            sharedEmerald.getGroupManager().getGroups().add(new EmeraldGroup(s));
        }

        serverProperties.setGroup(sharedEmerald.getGroupManager().getByName(settingsFile.getConfig().getString("server-group")));

        sharedEmerald.setServerProperties(serverProperties);
        /*  Create the current server to redis cache  */
        sharedEmerald.getServerManager().saveServer(serverProperties);



        jedis = sharedEmerald.getJedisAPI().getJedisHandler().getJedisPool().getResource();

        JsonObject object = new JsonObject();
        object.addProperty("name", serverProperties.getName());

        sharedEmerald.getJedisAPI().getJedisHandler().write("start###"+ object.toString());

        serverUpdateTask = new ServerUpdateTask();

        getCommand("serverinfo").setExecutor(new ServerInfoCommand());


    }

    @Override
    public void onDisable() {
        serverUpdateTask.cancel();

        serverProperties.setServerStatus(ServerStatus.OFFLINE);
        sharedEmerald.getServerManager().saveServer(serverProperties);

        instance = null;
    }
}
