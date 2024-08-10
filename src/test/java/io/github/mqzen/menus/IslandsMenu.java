package io.github.mqzen.menus;

import io.github.mqzen.menus.base.Content;
import io.github.mqzen.menus.base.pagination.Page;
import io.github.mqzen.menus.base.pagination.PageView;
import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.DataRegistry;
import io.github.mqzen.menus.misc.Slot;
import io.github.mqzen.menus.misc.button.Button;
import io.github.mqzen.menus.misc.itembuilder.impl.SimpleItemBuilder;
import io.github.mqzen.menus.titles.MenuTitle;
import io.github.mqzen.menus.titles.MenuTitles;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IslandsMenu extends Page {
 
    @Override
    public int getPageButtonsCount(@Nullable PageView pageView, Player player) {
        return 30;
    }
 
    @Override
    public ItemStack nextPageItem(Player player) {
        return SimpleItemBuilder.of(Material.ARROW)
                .display(Component.text("Next page"))
                .build();
    }
 
    @Override
    public ItemStack previousPageItem(Player player) {
        return SimpleItemBuilder.of(Material.ARROW)
                .display(Component.text("Previous page"))
                .build();
    }
 
    @Override
    public String getName() {
        return "Islands";
    }
 
    @Override
    public @NotNull MenuTitle getTitle(DataRegistry dataRegistry, Player player) {
        return MenuTitles.createLegacy("Islands");
    }
 
    @Override
    public @NotNull Capacity getCapacity(DataRegistry dataRegistry, Player player) {
        return Capacity.ofRows(6);
    }
 
    @Override
    public @NotNull Content getContent(DataRegistry dataRegistry, Player player, Capacity capacity) {
        /*Menu style = MenuStyle.of(
                "#########",
                "#-------#",
                "#-------#",
                "#-------#",
                "#-------#",
                "####-####"
        ).defaultStyle(ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("&f")
                .addDefaultItemFlags().build());
         */

        var builder = Content
                .builder(capacity)
                .setButton(Slot.of(5, 4),
                        Button.clickable(SimpleItemBuilder.of(Material.ARROW)
                        .display(Component.text("Back"))
                                .build(), (menuView, inventoryClickEvent) -> {
                            inventoryClickEvent.setCancelled(true);
                            System.out.println("Opening settings menu !");
                }));
 
        return builder.build();
    }
}