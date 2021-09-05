package me.zowpy.emerald.command;

import me.zowpy.emerald.EmeraldPlugin;
import me.zowpy.emerald.shared.server.EmeraldServer;
import me.zowpy.emerald.shared.server.ServerStatus;
import me.zowpy.emerald.shared.util.TPSUtility;
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

            boolean isPlayer = sender instanceof Player;
            Player player = isPlayer ? (Player) sender : null;

            EmeraldPlugin.getInstance().getSharedEmerald().getServerManager().getAsList().thenAccept(emeraldServers -> {
                for (EmeraldServer server : emeraldServers) {
                    sender.sendMessage(ChatColor.STRIKETHROUGH + "-----------------");
                    sender.sendMessage(ChatColor.GREEN + "name: " + ChatColor.WHITE + server.getName());
                    sender.sendMessage(ChatColor.GREEN + "status: " + server.getServerStatus().getMessage());
                    sender.sendMessage(ChatColor.GREEN + "group: " + ChatColor.WHITE + server.getGroup().getName());
                    sender.sendMessage(ChatColor.GREEN + "onlinePlayers: " + ChatColor.WHITE + server.getOnlinePlayers().size());
                    sender.sendMessage(ChatColor.GREEN + "tps: " + ChatColor.WHITE + TPSUtility.getTPS());
                    sender.sendMessage(ChatColor.GREEN + "joinable: " + ChatColor.WHITE + ((server.getServerStatus() == ServerStatus.OFFLINE) ? "No" : (!isPlayer || server.getServerStatus() == ServerStatus.WHITELISTED && !server.getWhitelistedPlayers().contains(player.getUniqueId()) ? "No" : server.getWhitelistedPlayers().contains(player.getUniqueId()) ? "Yes" : "No")));
                    sender.sendMessage(ChatColor.GREEN + "maxPlayers: " + ChatColor.WHITE + server.getMaxPlayers());
                    sender.sendMessage(ChatColor.STRIKETHROUGH + "-----------------");
                }
            });

        }

        return false;
    }
}
