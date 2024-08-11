package io.github.mqzen.menus;

import io.github.mqzen.menus.base.Content;
import io.github.mqzen.menus.base.pagination.Page;
import io.github.mqzen.menus.base.pagination.PageView;
import io.github.mqzen.menus.base.style.TextLayout;
import io.github.mqzen.menus.base.style.TextLayoutPane;
import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.DataRegistry;
import io.github.mqzen.menus.misc.button.Button;
import io.github.mqzen.menus.misc.itembuilder.ItemBuilder;
import io.github.mqzen.menus.titles.MenuTitle;
import io.github.mqzen.menus.titles.MenuTitles;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExampleAutoPage extends Page {
 
    @Override
    public int getPageButtonsCount(@Nullable PageView pageView, Player player) {
        return 12;
    }
 
    @Override
    public ItemStack nextPageItem(Player player) {
        return ItemBuilder.legacy(Material.PAPER)
                .setDisplay("&aNext page")
                .build();
    }
 
    @Override
    public ItemStack previousPageItem(Player player) {
        return ItemBuilder.legacy(Material.PAPER)
                .setDisplay("Previous page")
                .build();
    }
 
    @Override
    public String getName() {
        return "Islands";
    }
 
    @Override
    public @NotNull MenuTitle getTitle(DataRegistry dataRegistry, Player player) {
        int index = dataRegistry.getData("index");
        return MenuTitles.createModern("<gold>Islands Page #" + (index+1));
    }
 
    @Override
    public @NotNull Capacity getCapacity(DataRegistry dataRegistry, Player player) {
        return Capacity.ofRows(4);
    }
 
    @Override
    public @NotNull Content getContent(DataRegistry dataRegistry, Player player, Capacity capacity) {
        var builder = Content
                .builder(capacity);
        var button = Button.clickable(ItemBuilder.legacy(Material.STAINED_GLASS_PANE, 1, (short) 5)
                .setDisplay("Hi")
                .build(),
                (menuView, inventoryClickEvent) -> inventoryClickEvent.setCancelled(true));
        TextLayoutPane pane = new TextLayoutPane(capacity,
                TextLayout.builder().setDefault(button).build(),
                "#########",
                        "#       #",
                        "#       #",
                        "#########"
                );

        builder = builder.applyPane(pane);

        /**
         * old code here
        builder.draw(0, 8, Direction.RIGHT,button);
        builder.draw(9, 27, Direction.DOWNWARDS, button);
        builder.draw(28, 35, Direction.RIGHT, button);
        builder.draw(35, 8, Direction.UPWARDS, button);
         **/
        return builder.build();
    }
}