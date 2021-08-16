package io.github.zowpy.emerald.command;

import io.github.zowpy.emerald.menu.ServerMenu;
import io.github.zowpy.emerald.utils.menu.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This Project is property of Zowpy © 2021
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 8/13/2021
 * Project: Emerald
 */

public class ServersCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("servers")) {

            if (sender instanceof Player) {
                Player player = ((Player) sender).getPlayer();
                if (player.hasPermission("emerald.admin")) {
                    new ServerMenu().openMenu(player);
                }else {
                    player.sendMessage(CC.RED + "You don't have enough permissions to execute this command!");
                }
            }else {
                sender.sendMessage(CC.RED + "This command can be executed in-game!");
            }
        }

        return false;
    }
}
