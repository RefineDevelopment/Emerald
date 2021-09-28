package me.zowpy.emerald.shared.util;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.Objects;

/**
 * This Project is property of Zowpy © 2021
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 8/13/2021
 * Project: Emerald
 */

public class TPSUtility {

    private static final DecimalFormat format = new DecimalFormat("##.##");

    private static Object minecraftServer;
    private static Field recentTps;

    public static double[] getRecentTps() {
        try {
            if (minecraftServer == null) {
                minecraftServer = Objects.requireNonNull(ReflectionUtil.getNMSClass("MinecraftServer")).getMethod("getServer").invoke(null);
                recentTps = minecraftServer.getClass().getField("recentTps");
            }

            return (double[]) recentTps.get(minecraftServer);
        } catch (Exception e) {
            e.printStackTrace();
            return new double[]{20, 20, 20};
        }
    }

    public static String getTPS() {
        return format.format(Math.max(getRecentTps()[0], 20));
    }
}
