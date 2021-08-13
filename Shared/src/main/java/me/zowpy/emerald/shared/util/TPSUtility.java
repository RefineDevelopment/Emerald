package me.zowpy.emerald.shared.util;

import org.bukkit.Bukkit;
import org.bukkit.Server;

import java.lang.reflect.Field;

/**
 * This Project is property of Zowpy © 2021
 * Redistribution of this Project is not allowed
 *
 * @author ThatKawaiiSam
 * Created: 8/13/2021
 * Project: Emerald
 */

// Credits to ThatKawaiiSam for this util
public class TPSUtility {

    private static Object minecraftServer;
    private static Field recentTps;
    private static Boolean isPaperSpigot = null;

    public static double[] getRecentTps() {

        try {
            return getRecentTpsRefl();
        } catch (Throwable throwable) {
            return new double[]{20, 20, 20};
        }
    }

    private static double[] getRecentTpsRefl() throws Throwable {
        if (minecraftServer == null) {
            Server server = Bukkit.getServer();
            Field consoleField = server.getClass().getDeclaredField("console");
            consoleField.setAccessible(true);
            minecraftServer = consoleField.get(server);
        }
        if (recentTps == null) {
            recentTps = minecraftServer.getClass().getSuperclass().getDeclaredField("recentTps");
            recentTps.setAccessible(true);
        }
        return (double[]) recentTps.get(minecraftServer);
    }
}
