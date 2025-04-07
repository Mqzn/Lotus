package io.github.mqzen.menus;

import io.github.mqzen.menus.base.Content;
import io.github.mqzen.menus.base.pagination.*;
import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.DataRegistry;
import io.github.mqzen.menus.misc.Slot;
import io.github.mqzen.menus.misc.button.Button;
import io.github.mqzen.menus.misc.button.actions.ButtonClickAction;
import io.github.mqzen.menus.misc.itembuilder.ItemBuilder;
import io.github.mqzen.menus.titles.MenuTitle;
import io.github.mqzen.menus.titles.MenuTitles;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ExampleAutoPage extends Page {


    /**
     * The range of button-filling of {@link PageComponent}
     * this page should have
     *
     * @param capacity the capacity for the page
     * @param opener   opener of this pagination
     * @return The range of button-filling of {@link PageComponent}
     * @see FillRange
     */
    @Override
    public FillRange getFillRange(Capacity capacity, Player opener) {
        return FillRange.start(capacity, Slot.of(10)) //here, the start is 0 automatically
            .end(Slot.of(25))
            .except(Slot.of(17), Slot.of(18));
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
        return MenuTitles.createModern("<gold>Example Page " + (index+1) + "/" + max);
    }
 
    @Override
    public @NotNull Capacity getCapacity(DataRegistry dataRegistry, Player player) {
        return Capacity.ofRows(4);
    }
 
    @Override
    public @NotNull Content getContent(DataRegistry dataRegistry, Player player, Capacity capacity) {
        Content.Builder builder = Content
            .builder(capacity)
            .setButton(31, Button.clickable(new ItemStack(Material.BARRIER), ButtonClickAction.plain((menu, event) -> {
                event.setCancelled(true);
                player.closeInventory();
            })));
        return builder.build();
    }

    @Override
    protected void onSwitchingToNextPage(
        @NotNull Pagination pagination,
        @NotNull Capacity capacity,
        @NotNull Slot slot,
        @NotNull PageView clickedView,
        @NotNull InventoryClickEvent event
    ) {
        Player player = (Player) event.getWhoClicked();
        player.playSound(player.getLocation(), Sound.ARROW_HIT, 1, 1);
    }

    @Override
    protected void onSwitchingToPreviousPage(
        @NotNull Pagination pagination,
        @NotNull Capacity capacity,
        @NotNull Slot slot,
        @NotNull PageView clickedView,
        @NotNull InventoryClickEvent event
    ) {
        Player player = (Player) event.getWhoClicked();
        player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1, 1);
    }
}