package me.zowpy.emerald.utils.menu.buttons;

import lombok.AllArgsConstructor;
import me.zowpy.emerald.utils.ItemBuilder;
import me.zowpy.emerald.utils.menu.Button;
import me.zowpy.emerald.utils.menu.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class CloseButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder item =  new ItemBuilder(Material.INK_SACK).durability(1).name(CC.translate("&cClose"));
        return item.build();
    }

    @Override
    public void clicked(Player player, int i, ClickType clickType, int hb) {
        Button.playNeutral(player);

        player.closeInventory();
    }

}
