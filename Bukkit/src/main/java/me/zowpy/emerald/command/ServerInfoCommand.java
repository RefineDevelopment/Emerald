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
        if (sender instanceof Player && !sender.hasPermission("emerald.admin")) { /* If our executor is a player and if they do not have the admin permission. */
            sender.sendMessage(ChatColor.RED + "You don't have enough permissions to execute this command!");
            return true;
        }

        boolean isPlayer = sender instanceof Player;
        Player player = isPlayer ? (Player) sender : null;

        EmeraldPlugin.getInstance().getSharedEmerald().getServerManager().getAsList().thenAccept(emeraldServers -> {
            System.out.println("pog " + emeraldServers.size());
            for (EmeraldServer server : emeraldServers) {
                sender.sendMessage(ChatColor.STRIKETHROUGH + "-----------------");
                sender.sendMessage(ChatColor.GREEN + "Name: " + ChatColor.WHITE + server.getName());
                sender.sendMessage(ChatColor.GREEN + "Status: " + server.getServerStatus().getMessage());
                sender.sendMessage(ChatColor.GREEN + "Group: " + ChatColor.WHITE + server.getGroup().getName());
                sender.sendMessage(ChatColor.GREEN + "Online Players: " + ChatColor.WHITE + server.getOnlinePlayers().size());
                sender.sendMessage(ChatColor.GREEN + "Tps: " + ChatColor.WHITE + TPSUtility.getTPS());
                sender.sendMessage(ChatColor.GREEN + "Joinable: " + ChatColor.WHITE + ((server.getServerStatus() == ServerStatus.OFFLINE) ? "No" : (!isPlayer || server.getServerStatus() == ServerStatus.WHITELISTED && !server.getWhitelistedPlayers().contains(player.getUniqueId()) ? "No" : server.getWhitelistedPlayers().contains(player.getUniqueId()) ? "Yes" : "No")));
                sender.sendMessage(ChatColor.GREEN + "Maximum Players: " + ChatColor.WHITE + server.getMaxPlayers());
                sender.sendMessage(ChatColor.STRIKETHROUGH + "-----------------");
            }
        });
        sender.sendMessage("penius");
        return false;
    }
}
