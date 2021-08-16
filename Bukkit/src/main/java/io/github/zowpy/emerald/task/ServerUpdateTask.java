package io.github.zowpy.emerald.task;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.github.zowpy.emerald.EmeraldPlugin;
import io.github.zowpy.emerald.shared.server.ServerProperties;
import io.github.zowpy.emerald.shared.server.ServerStatus;
import io.github.zowpy.emerald.shared.util.TPSUtility;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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

public class ServerUpdateTask extends BukkitRunnable {

    public ServerUpdateTask() {
        this.runTaskTimerAsynchronously(EmeraldPlugin.getInstance(), 20L, 20*3L);

    }


    @Override
    public void run() {

        ServerProperties serverProperties = EmeraldPlugin.getInstance().getSharedEmerald().getServerProperties();
        serverProperties.setServerStatus(Bukkit.hasWhitelist() ? ServerStatus.WHITELISTED : ServerStatus.ONLINE);
        serverProperties.setOnlinePlayers(Bukkit.getOnlinePlayers().stream().map(Entity::getUniqueId).collect(Collectors.toList()));
        serverProperties.setWhitelistedPlayers(Bukkit.getWhitelistedPlayers().stream().map(OfflinePlayer::getUniqueId).collect(Collectors.toList()));
        serverProperties.setMaxPlayers(Bukkit.getMaxPlayers());
        serverProperties.setTps(TPSUtility.round(Double.parseDouble(TPSUtility.getTPS())));

        JsonObject object = new JsonObject();
        object.addProperty("uuid", serverProperties.getUuid().toString());
        object.addProperty("name", serverProperties.getName());
        object.addProperty("serverStatus", serverProperties.getServerStatus().name());
        object.addProperty("ip", serverProperties.getIp());
        object.addProperty("port", serverProperties.getPort());
        object.addProperty("group", serverProperties.getGroup().getName());
        object.addProperty("tps", serverProperties.getTps());

        JsonArray whitelistedPlayers = new JsonArray();

        for (UUID uuid : Bukkit.getWhitelistedPlayers().stream().map(OfflinePlayer::getUniqueId).collect(Collectors.toList())) {
            whitelistedPlayers.add(new JsonPrimitive(uuid.toString()));
        }

        object.add("whitelistedPlayers", whitelistedPlayers);

        JsonArray onlinePlayers = new JsonArray();
        for (Player player : Bukkit.getOnlinePlayers()) {
            onlinePlayers.add(new JsonPrimitive(player.getUniqueId().toString()));
        }

        object.add("onlinePlayers", onlinePlayers);
        object.addProperty("maxPlayers", serverProperties.getMaxPlayers());

        EmeraldPlugin.getInstance().getSharedEmerald().getJedisAPI().getJedisHandler().write("updateserver###" + object.toString());
    }

}


         /*ServerProperties serverProperties = EmeraldPlugin.getInstance().getSharedEmerald().getServerProperties();
        serverProperties.setServerStatus(Bukkit.hasWhitelist() ? ServerStatus.WHITELISTED : ServerStatus.ONLINE);
        serverProperties.setOnlinePlayers(Bukkit.getOnlinePlayers().stream().map(Entity::getUniqueId).collect(Collectors.toList()));
        serverProperties.setWhitelistedPlayers(Bukkit.getWhitelistedPlayers().stream().map(OfflinePlayer::getUniqueId).collect(Collectors.toList()));
        serverProperties.setMaxPlayers(Bukkit.getMaxPlayers());
        serverProperties.setTps(TPSUtility.round(Double.parseDouble(TPSUtility.getTPS())));

        EmeraldPlugin.getInstance().getSharedEmerald().getServerManager().updateServer(EmeraldPlugin.getInstance().getSharedEmerald().getServerManager().getByUUID(serverProperties.getUuid()), serverProperties);
        EmeraldPlugin.getInstance().getSharedEmerald().getServerManager().updateServers();
*/
