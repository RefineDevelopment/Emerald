package me.zowpy.emerald.command;

import me.zowpy.emerald.EmeraldPlugin;
import me.zowpy.emerald.shared.server.EmeraldServer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This Project is property of Zowpy © 2021
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 8/11/2021
 * Project: Emerald
 */

public class ServerInfoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("serverinfo")) {
            if (sender instanceof Player && !((Player) sender).hasPermission("emerald.admin")) {
                sender.sendMessage(ChatColor.RED + "You don't have enough permissions to execute this command!");
                return true;
            }

            for (EmeraldServer server : EmeraldPlugin.getInstance().getSharedEmerald().getServerManager().getEmeraldServers()) {
                sender.sendMessage(ChatColor.STRIKETHROUGH + "-----------------");
                sender.sendMessage(ChatColor.AQUA + "name: " + ChatColor.WHITE + server.getName());
                sender.sendMessage(ChatColor.AQUA + "status: " + server.getStatus().getMessage());
                sender.sendMessage(ChatColor.AQUA + "onlinePlayers: " + ChatColor.WHITE + server.getOnlinePlayers());
                sender.sendMessage(ChatColor.AQUA + "maxPlayers: " + ChatColor.WHITE + server.getMaxPlayers());
                sender.sendMessage(ChatColor.STRIKETHROUGH + "-----------------");
            }
        }

        return false;
    }
}
