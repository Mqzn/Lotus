package io.github.mqzen.menus;

import io.github.mqzen.menus.base.Content;
import io.github.mqzen.menus.base.pagination.FillRange;
import io.github.mqzen.menus.base.pagination.Page;
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

public final class ExamplePlainPage extends Page {

    private final int amountOfItems;

    public ExamplePlainPage(int amountOfItems) {
        this.amountOfItems = amountOfItems;
    }


    /**
     * The number of buttons this pageView should have
     *
     * @param capacity the capacity for the page
     * @param opener   opener of this pagination
     * @return The number of buttons this pageView should have
     */
    @Override
    public FillRange getFillRange(Capacity capacity, Player opener) {
        return FillRange.start(capacity).end(Slot.of(amountOfItems));
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
        return "Example plain-pagination";
    }

    @Override
    public @NotNull MenuTitle getTitle(DataRegistry dataRegistry, Player player) {
        int index = dataRegistry.getData("index");
        return MenuTitles.createLegacy("&6Example Plain page #" + (index + 1));
    }

    @Override
    public @NotNull Capacity getCapacity(DataRegistry dataRegistry, Player player) {
        return Capacity.ofRows(3);
    }


    @Override
    public @NotNull Content getContent(DataRegistry extraData, Player opener, Capacity capacity) {
        var builder = Content.builder(capacity);
        for (int i = 0; i < amountOfItems; i++) {
            builder.setButton(i, new ExampleMenuComponent("" + i).toButton());
        }
        return builder.build();
    }
}
