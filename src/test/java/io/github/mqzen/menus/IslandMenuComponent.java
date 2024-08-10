package io.github.mqzen.menus;

import io.github.mqzen.menus.base.pagination.PageComponent;
import io.github.mqzen.menus.base.pagination.PageView;
import io.github.mqzen.menus.misc.itembuilder.impl.SimpleItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class IslandMenuComponent implements PageComponent {

    private final String name;
    public IslandMenuComponent(String name) {
        this.name = name;
    }
    @Override
    public ItemStack toItem() {
        return SimpleItemBuilder.of(Material.GRASS)
                .display(Component.text(name, NamedTextColor.GRAY)).build();
    }
 
    @Override
    public void onClick(PageView pageView, InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        player.sendMessage("Visiting island " + name);
    }
}