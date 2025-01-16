package io.github.mqzen.menus;

import io.github.mqzen.menus.base.Content;
import io.github.mqzen.menus.base.Menu;
import io.github.mqzen.menus.base.MenuView;
import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.DataRegistry;
import io.github.mqzen.menus.misc.button.Button;
import io.github.mqzen.menus.misc.button.actions.ButtonClickAction;
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

    /**
     * Creates the content for the menu
     *
     * @param extraData the data container for this menu for extra data
     * @param opener    the player opening this menu
     * @param capacity  the capacity set by the user above
     * @return the content of the menu to add (this includes items)
     */
    @Override
    public @NotNull Content getContent(DataRegistry extraData,
                                       Player opener, Capacity capacity) {

        Button borderPane = Button.clickable(
            ItemBuilder.legacy(Material.STAINED_GLASS_PANE, 1, (short) 5)
                .setDisplay("&r").build(),
            ButtonClickAction.plain((menuView, event) -> {
                event.setCancelled(true);
                //we want nothing to happen here
                menuView.updateButton(event.getSlot(), (button) -> {
                    assert button.getItem() != null;
                    var item = button.getItem();
                    short buttonData = button.getNamedData("data");
                    buttonData++;
                    if (buttonData > 12) {
                        buttonData = 0;
                    }
                    button.setNamedData("data", buttonData);
                    item.setDurability(buttonData);
                    button.setItem(item);
                });

            })
        ).setNamedData("data", (short) 5);

        return Content.builder(capacity)
            .setButton(0, Button.clickable(new ItemStack(Material.GOLDEN_APPLE), ButtonClickAction.plain((menu, event)-> {
                menu.getAPI().openMenu(opener,new ExampleMenu2());
            })))
            .build();
    }

    @Override
    public void onPostClick(MenuView<?> playerMenuView, InventoryClickEvent event) {
        //happens after button click is executed
        event.setCancelled(true);
    }
}
