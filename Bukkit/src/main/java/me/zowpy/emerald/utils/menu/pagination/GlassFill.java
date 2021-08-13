package me.zowpy.emerald.utils.menu.pagination;

import lombok.AllArgsConstructor;
import me.zowpy.emerald.utils.ItemBuilder;
import me.zowpy.emerald.utils.menu.Button;
import me.zowpy.emerald.utils.menu.CC;
import me.zowpy.emerald.utils.menu.pagination.PaginatedMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class GlassFill extends Button {

    private PaginatedMenu menu;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder item = new ItemBuilder(Material.STAINED_GLASS_PANE);

        item.durability((byte) 7);

        item.name(CC.translate(" "));

        return item.build();
    }

    @Override
    public void clicked(Player player, int i, ClickType clickType, int hb) {
    }


}
