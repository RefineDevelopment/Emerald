package me.zowpy.emerald.shared.jedis;

import com.google.gson.JsonObject;
import me.zowpy.emerald.shared.SharedEmerald;
import io.github.zowpy.jedisapi.redis.subscription.IncomingMessage;
import io.github.zowpy.jedisapi.redis.subscription.JedisSubscriber;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

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

@AllArgsConstructor
public class SharedJedisSubscriber extends JedisSubscriber {

    private final SharedEmerald emerald;

    @IncomingMessage(payload = "command")
    public void execute(JsonObject object) {
        if (emerald.getUuid().equals(UUID.fromString(object.get("uuid").getAsString()))) { // Validate server.
            String command = object.get("command").getAsString();

            if (object.has("issuer")) { // If we provided an issuer, read the uuid and if they are on this server make sure they execute it.
                UUID uuid = UUID.fromString(object.get("issuer").getAsString());
                if (Bukkit.getPlayer(uuid) != null) {
                    Bukkit.getPlayer(uuid).chat("/" + command);
                    return;
                }
            }

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command); // If no issuer was provided or found execute as server.
        }
    }

    @IncomingMessage(payload = "start")
    public void start(JsonObject object) {
        String name = object.get("name").getAsString();
        System.out.println(name);

        emerald.getAdminUsers().forEach(player -> player.sendMessage(ChatColor.GREEN + "[Emerald] " + ChatColor.BOLD + name + ChatColor.WHITE + " went online!"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Emerald] " + ChatColor.BOLD + name + ChatColor.WHITE + " went online!");
    }

    @IncomingMessage(payload = "shutdown")
    public void shutdown(JsonObject object) {
        String name = object.get("name").getAsString();

        emerald.getAdminUsers().forEach(player -> player.sendMessage(ChatColor.GREEN + "[Emerald] " + ChatColor.BOLD + name + ChatColor.WHITE + " went offline!"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Emerald] " + ChatColor.BOLD + name + ChatColor.WHITE + " went offline!");
    }

}
