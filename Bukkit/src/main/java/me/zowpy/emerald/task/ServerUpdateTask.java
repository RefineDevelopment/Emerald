package me.zowpy.emerald.task;

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
        this.runTaskTimerAsynchronously(EmeraldPlugin.getInstance(), 0L, 20*3L);

    }

    @Override
    public void run() {
        ServerProperties serverProperties = EmeraldPlugin.getInstance().getSharedEmerald().getServerProperties();
        serverProperties.setServerStatus(Bukkit.hasWhitelist() ? ServerStatus.WHITELISTED : ServerStatus.ONLINE);
        serverProperties.setOnlinePlayers(Bukkit.getOnlinePlayers().stream().map(Entity::getUniqueId).collect(Collectors.toList()));
        serverProperties.setWhitelistedPlayers(Bukkit.getWhitelistedPlayers().stream().map(OfflinePlayer::getUniqueId).collect(Collectors.toList()));
        serverProperties.setMaxPlayers(Bukkit.getMaxPlayers());
        serverProperties.setTps(TPSUtility.round(Double.parseDouble(TPSUtility.getTPS())));

        EmeraldPlugin.getInstance().getSharedEmerald().getServerManager().saveServer(serverProperties);
    }
}
