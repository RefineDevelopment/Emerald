package me.zowpy.emerald.shared.server;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;

/**
 * This Project is property of Zowpy © 2021
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 8/10/2021
 * Project: Emerald
 */
@RequiredArgsConstructor
public enum ServerStatus {

    OFFLINE(ChatColor.RED),
    ONLINE(ChatColor.GREEN),
    WHITELISTED(ChatColor.YELLOW);

    final ChatColor color;


    public String getMessage() {
        return
                this.color.toString() + // Get the color specified for this status.
                        WordUtils.capitalize(name().toLowerCase()); // Capitalize it correctly.
    }
}
