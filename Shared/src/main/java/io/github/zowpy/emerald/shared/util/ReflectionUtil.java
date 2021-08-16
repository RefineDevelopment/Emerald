package io.github.zowpy.emerald.shared.util;

import org.bukkit.Bukkit;

/**
 * This Project is property of Zowpy © 2021
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 8/15/2021
 * Project: Emerald
 */

public class ReflectionUtil {

    private static final String name = Bukkit.getServer().getClass().getPackage().getName();
    private static final String version = name.substring(name.lastIndexOf('.') + 1);

    public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
