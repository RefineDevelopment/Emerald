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
public class Glass extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder item =  new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(CC.translate(" "));
        return item.build();
    }

    @Override
    public void clicked(Player player, int i, ClickType clickType, int hb) {
        //Button.playNeutral(player);
    }

}
