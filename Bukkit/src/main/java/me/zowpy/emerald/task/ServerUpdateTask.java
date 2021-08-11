package me.zowpy.emerald.task;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.zowpy.emerald.EmeraldPlugin;
import me.zowpy.emerald.shared.server.EmeraldServer;
import me.zowpy.emerald.shared.server.ServerProperties;
import me.zowpy.emerald.shared.server.ServerStatus;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

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
        this.runTaskTimerAsynchronously(EmeraldPlugin.getInstance(), 20*5L, 20L);
    }

    @Override
    public void run() {
        ServerProperties serverProperties = EmeraldPlugin.getInstance().getSharedEmerald().getServerProperties();
        serverProperties.setServerStatus(Bukkit.hasWhitelist() ? ServerStatus.WHITELISTED : ServerStatus.ONLINE);
        serverProperties.setIp(Bukkit.getIp());
        serverProperties.setPort(Bukkit.getPort());
        serverProperties.setName(EmeraldPlugin.getInstance().getSettingsFile().getConfig().getString("server-name"));
        serverProperties.setOnlinePlayers(Bukkit.getOnlinePlayers().size());
        serverProperties.setMaxPlayers(Bukkit.getMaxPlayers());

        JsonObject object = new JsonObject();
        object.addProperty("uuid", serverProperties.getUuid().toString());
        //object.addProperty("properties", EmeraldPlugin.GSON.toJson(serverProperties));

        System.out.println(new JsonParser().parse("{uuid:aebbce3d-2dff-4f4a-90c9-6997a125b745}") != null);
        EmeraldPlugin.getInstance().getSharedEmerald().getJedisAPI().getJedisHandler().write("updateserver###" + object.toString());
    }
}
