package io.github.zowpy.emerald.shared.server;

import org.bukkit.ChatColor;
import org.bukkit.Server;

/**
 * This Project is property of Zowpy © 2021
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 8/10/2021
 * Project: Emerald
 */
public enum ServerStatus {

    ONLINE(ChatColor.GREEN + "Online"), OFFLINE(ChatColor.RED + "Offline"), WHITELISTED(ChatColor.YELLOW + "Whitelisted");

    String message;

    ServerStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
