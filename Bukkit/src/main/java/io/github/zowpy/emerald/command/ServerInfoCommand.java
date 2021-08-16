package io.github.zowpy.emerald.command;

import io.github.zowpy.emerald.EmeraldPlugin;
import io.github.zowpy.emerald.shared.server.EmeraldServer;
import io.github.zowpy.emerald.shared.server.ServerStatus;
import io.github.zowpy.emerald.shared.util.TPSUtility;
import javafx.print.PageLayout;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

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

            DecimalFormat format = new DecimalFormat("##.##");
            for (EmeraldServer server : EmeraldPlugin.getInstance().getSharedEmerald().getServerManager().getEmeraldServers()) {
                sender.sendMessage(ChatColor.STRIKETHROUGH + "-----------------");
                sender.sendMessage(ChatColor.GREEN + "name: " + ChatColor.WHITE + server.getName());
                sender.sendMessage(ChatColor.GREEN + "status: " + server.getStatus().getMessage());
                sender.sendMessage(ChatColor.GREEN + "group: " + ChatColor.WHITE + server.getGroup().getName());
                sender.sendMessage(ChatColor.GREEN + "onlinePlayers: " + ChatColor.WHITE + server.getOnlinePlayers().size());
                sender.sendMessage(ChatColor.GREEN + "tps: " + ChatColor.WHITE + format.format(TPSUtility.round(server.getTps())));
                sender.sendMessage(ChatColor.GREEN + "joinable: " + ChatColor.WHITE + ((server.getStatus() == ServerStatus.OFFLINE) ? "No" : (!isPlayer || server.getStatus() == ServerStatus.WHITELISTED && !server.getWhitelistedPlayers().contains(player.getUniqueId()) ? "No" : server.getWhitelistedPlayers().contains(player.getUniqueId()) ? "Yes" : "No")));
                sender.sendMessage(ChatColor.GREEN + "maxPlayers: " + ChatColor.WHITE + server.getMaxPlayers());
                sender.sendMessage(ChatColor.STRIKETHROUGH + "-----------------");
            }
        }

        return false;
    }
}
