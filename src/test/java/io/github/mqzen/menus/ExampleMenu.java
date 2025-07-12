package io.github.mqzen.menus;

import io.github.mqzen.menus.base.Content;
import io.github.mqzen.menus.base.Menu;
import io.github.mqzen.menus.base.MenuView;
import io.github.mqzen.menus.base.animation.AnimationTaskData;
import io.github.mqzen.menus.base.animation.TransformingButton;
import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.DataRegistry;
import io.github.mqzen.menus.misc.itembuilder.ItemBuilder;
import io.github.mqzen.menus.titles.MenuTitle;
import io.github.mqzen.menus.titles.MenuTitles;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class ExampleMenu implements Menu {

    /**
     * @return The unique name for this menu
     */
    @Override
    public String getName() {
        return "example menu";
    }

    /**
     * @param extraData the data container for this menu for extra data
     * @param opener    the player who is opening this menu
     * @return the title for this menu
     */
    @Override
    public @NotNull MenuTitle getTitle(DataRegistry extraData, Player opener) {
        return MenuTitles.createLegacy("&cExample Menu");
    }

    /**
     * @param extraData the data container for this menu for extra data
     * @param opener    the player who is opening this menu
     * @return the capacity/size for this menu
     */
    @Override
    public @NotNull Capacity getCapacity(DataRegistry extraData, Player opener) {
        return Capacity.ofRows(3);//Alternative approach: Capacity.ofRows(3)
    }
    
    
    private final static ItemStack[] ITEM_FRAMES = {
            ItemBuilder.legacy(Material.FIREWORK).build(),
            ItemBuilder.legacy(Material.TNT).build(),
            ItemBuilder.legacy(Material.SKULL_ITEM, 1, (short)1).build(),
            ItemBuilder.legacy(Material.SNOW_BALL).build()
    };
    
    @Override
    public @NotNull Content getContent(
            DataRegistry extraData,
            Player opener,
            Capacity capacity
    ) {
        
        TransformingButton randomGameButton = new TransformingButton(
                AnimationTaskData
                        .builder()
                        .delay(10L)
                        .ticks(12L) // Change the delay to 20 ticks (1 second)
                        .async(true)
                        .build(),
                
                (item) -> ItemBuilder.legacy(item)
                        .setDisplay("&aRandom Game")
                        .setLore("&7Click to play a random game")
                        .build(),
                ITEM_FRAMES
        ).click((view, event) -> {
            // Handle the click event here
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("You clicked the random game button!");
        });

        return Content.builder(capacity)
            .setButton(1, new ExampleAutoAnimatedButton())
            .setButton(2, randomGameButton)
            .build();
    }

    @Override
    public void onPostClick(MenuView<?> playerMenuView, InventoryClickEvent event) {
        //happens after button click is executed
        event.setCancelled(true);
    }
}
