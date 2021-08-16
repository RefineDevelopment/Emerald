package io.github.zowpy.emerald.shared.jedis;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.zowpy.emerald.shared.SharedEmerald;
import io.github.zowpy.emerald.shared.server.ServerProperties;
import io.github.zowpy.emerald.shared.server.ServerStatus;
import lombok.AllArgsConstructor;
import io.github.zowpy.emerald.shared.server.EmeraldServer;
import io.github.zowpy.jedisapi.redis.subscription.IncomingMessage;
import io.github.zowpy.jedisapi.redis.subscription.JedisSubscriber;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
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

@AllArgsConstructor
public class SharedJedisSubscriber extends JedisSubscriber {

    private SharedEmerald emerald;

    @IncomingMessage(payload = "updateservers")
    public void updateServers() {
        emerald.getServerManager().updateServers();
    }

    @IncomingMessage(payload = "updateserver")
    public void updateServer(JsonObject object) {
        EmeraldServer server = emerald.getServerManager().getByUUID(UUID.fromString(object.get("uuid").getAsString()));
        ServerProperties serverProperties = new ServerProperties();

        serverProperties.setName(object.get("name").getAsString());
        serverProperties.setUuid(UUID.fromString(object.get("uuid").getAsString()));
        serverProperties.setServerStatus(ServerStatus.valueOf(object.get("serverStatus").getAsString()));
        serverProperties.setGroup(emerald.getGroupManager().getByName(object.get("group").getAsString()));
        serverProperties.setIp(object.get("ip").getAsString());
        serverProperties.setPort(object.get("port").getAsInt());

        List<UUID> online = new ArrayList<>();
        for (JsonElement e : object.get("onlinePlayers").getAsJsonArray()) {
            online.add(UUID.fromString(e.getAsString()));
        }

        serverProperties.setOnlinePlayers(online);

        List<UUID> whitelist = new ArrayList<>();
        for (JsonElement e : object.get("whitelistedPlayers").getAsJsonArray()) {
            whitelist.add(UUID.fromString(e.getAsString()));
        }

        serverProperties.setWhitelistedPlayers(whitelist);
        serverProperties.setMaxPlayers(object.get("maxPlayers").getAsInt());
        serverProperties.setTps(object.get("tps").getAsDouble());

        if (server != null) {
            emerald.getServerManager().updateServer(server, serverProperties);
        }
    }
    @IncomingMessage(payload = "shutdownserver")
    public void shutdownServer(JsonObject object) {
        if (emerald.getUuid().equals(UUID.fromString(object.get("uuid").getAsString()))) {
            return;
        }

        EmeraldServer server = emerald.getServerManager().getByUUID(UUID.fromString(object.get("uuid").getAsString()));

        if (server != null) {
            emerald.getServerManager().setOffline(server, emerald.getJedisAPI().getJedisHandler().getJedisPool().getResource());

        }
    }

    @IncomingMessage(payload = "command")
    public void executeCommand(JsonObject object) {
        if (emerald.getUuid().equals(UUID.fromString(object.get("uuid").getAsString()))) {
            String command = object.get("command").getAsString();

            if (object.has("issuer")) {
                UUID uuid = UUID.fromString(object.get("issuer").getAsString());
                if (Bukkit.getPlayer(uuid) != null) {
                    Bukkit.getPlayer(uuid).chat("/" + command);
                    return;
                }
            }

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }

    @IncomingMessage(payload = "start")
    public void startServer(JsonObject object) {
        Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("emerald.admin")).collect(Collectors.toList())
                .forEach(player -> player.sendMessage(ChatColor.GREEN + "[Emerald] " + ChatColor.BOLD + object.get("name").getAsString() + ChatColor.WHITE + " went online!"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Emerald] " + ChatColor.BOLD + object.get("name").getAsString() + ChatColor.WHITE + " went online!");
    }
    @IncomingMessage(payload = "shutdown")
    public void stopServer(JsonObject object) {
        EmeraldServer server = emerald.getServerManager().getByName(object.get("name").getAsString());

        server.setStatus(ServerStatus.OFFLINE);
        Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("emerald.admin")).collect(Collectors.toList())
                .forEach(player -> player.sendMessage(ChatColor.GREEN + "[Emerald] " + ChatColor.BOLD + object.get("name").getAsString() + ChatColor.WHITE + " went offline!"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Emerald] " + ChatColor.BOLD + object.get("name").getAsString() + ChatColor.WHITE + " went offline!");
    }

}
