package me.zowpy.emerald.task;

import com.google.gson.JsonObject;
import me.zowpy.emerald.EmeraldPlugin;
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

public class ServerUpdateTask extends Thread {

    public ServerUpdateTask() {
        this.setName("Emerald-Server-Update-Thread");
    }

    @Override
    public void run() {

        while (true) {

            try {
                this.update();
            }catch (Exception e) {
                e.printStackTrace();
            }

            try {
                sleep(50*20);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void update() {
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
    }
}
