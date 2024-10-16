package io.github.mqzen.menus;

import io.github.mqzen.menus.base.Content;
import io.github.mqzen.menus.base.pagination.FillRange;
import io.github.mqzen.menus.base.pagination.Page;
import io.github.mqzen.menus.base.pagination.Pagination;
import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.DataRegistry;
import io.github.mqzen.menus.misc.Slot;
import io.github.mqzen.menus.misc.itembuilder.ItemBuilder;
import io.github.mqzen.menus.titles.MenuTitle;
import io.github.mqzen.menus.titles.MenuTitles;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ExampleAutoPage extends Page {


    /**
     * The number of buttons this pageView should have
     *
     * @param capacity the capacity for the page
     * @param opener   opener of this pagination
     * @return The number of buttons this pageView should have
     */
    @Override
    public FillRange getFillRange(Capacity capacity, Player opener) {
        return FillRange.start(capacity)
            .end(Slot.of(12))
            .except(Slot.of(0));
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
                .setDisplay("&ePrevious page")
                .build();
    }
 
    @Override
    public String getName() {
        return "Example auto-pagination";
    }
 
    @Override
    public @NotNull MenuTitle getTitle(DataRegistry dataRegistry, Player player) {
        int index = dataRegistry.getData("index");
        Pagination pagination = dataRegistry.getData("pagination");
        int max = pagination.getMaximumPages();
        return MenuTitles.createModern("<gold>Example Page #" + (index+1) + "/" + max);
    }
 
    @Override
    public @NotNull Capacity getCapacity(DataRegistry dataRegistry, Player player) {
        return Capacity.ofRows(4);
    }
 
    @Override
    public @NotNull Content getContent(DataRegistry dataRegistry, Player player, Capacity capacity) {
        var builder = Content
                .builder(capacity);
        return builder.build();
    }
}