package me.zowpy.emerald.task;

import lombok.SneakyThrows;
import me.zowpy.emerald.EmeraldPlugin;
import me.zowpy.emerald.shared.server.ServerProperties;
import me.zowpy.emerald.shared.server.ServerStatus;
import me.zowpy.emerald.shared.util.TPSUtility;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

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
        serverProperties.setTps(TPSUtility.getRecentTps()[1]);

        EmeraldPlugin.getInstance().getSharedEmerald().getServerManager().updateServer(EmeraldPlugin.getInstance().getSharedEmerald().getServerManager().getByUUID(serverProperties.getUuid()), serverProperties);
        EmeraldPlugin.getInstance().getSharedEmerald().getServerManager().updateServers();


    }

}


  /*  public void update() {
        ServerProperties serverProperties = EmeraldPlugin.getInstance().getSharedEmerald().getServerProperties();
        serverProperties.setServerStatus(Bukkit.hasWhitelist() ? ServerStatus.WHITELISTED : ServerStatus.ONLINE);
        serverProperties.setIp(Bukkit.getIp());
        serverProperties.setPort(Bukkit.getPort());
        serverProperties.setName(EmeraldPlugin.getInstance().getSettingsFile().getConfig().getString("server-name"));
        serverProperties.setOnlinePlayers(Bukkit.getOnlinePlayers().size());
        serverProperties.setMaxPlayers(Bukkit.getMaxPlayers());

        JsonObject object = new JsonObject();
        object.addProperty("uuid", serverProperties.getUuid().toString());
        object.addProperty("properties", EmeraldPlugin.GSON.toJson(serverProperties));

        EmeraldPlugin.getInstance().getSharedEmerald().getJedisAPI().getJedisHandler().write("updateserver###" + object.toString());
    } */
