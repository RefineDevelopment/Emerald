package me.zowpy.emerald.menu;

import me.zowpy.emerald.EmeraldPlugin;
import me.zowpy.emerald.shared.server.EmeraldServer;
import me.zowpy.emerald.shared.server.ServerStatus;
import me.zowpy.emerald.utils.ItemBuilder;
import me.zowpy.emerald.utils.XMaterial;
import me.zowpy.emerald.utils.menu.Button;
import me.zowpy.emerald.utils.menu.pagination.PaginatedMenu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This Project is property of Zowpy © 2021
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 8/13/2021
 * Project: Emerald
 */

public class ServerMenu extends PaginatedMenu {

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&7Servers";
    }

    @Override
    public int getSize() {
        return 9*4;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> toReturn = new HashMap<>();

        int i = 0;
        for (EmeraldServer server : EmeraldPlugin.getInstance().getSharedEmerald().getServerManager().getEmeraldServers()) {
            toReturn.put(i, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return new ItemBuilder(server.getStatus() == ServerStatus.ONLINE ? XMaterial.GREEN_WOOL.parseItem() : server.getStatus() == ServerStatus.WHITELISTED ? XMaterial.YELLOW_WOOL.parseItem() : XMaterial.RED_WOOL.parseItem())
                            .name(server.getName())
                            .lore(Arrays.asList(
                                 "&aName: &f" + server.getName(),
                                 "&aStatus: " + server.getStatus().getMessage(),
                                 "&aGroup: " + server.getGroup().getName(),
                                 "&aOnlinePlayers: &f" + server.getOnlinePlayers(),
                                 "&aMaxPlayers: &f" + server.getMaxPlayers()
                            )).build();
                }
            });
            i++;
        }

        return toReturn;
    }
}
