package io.github.mqzen.menus.misc.button.actions.impl;

import io.github.mqzen.menus.base.MenuView;
import io.github.mqzen.menus.misc.DataRegistry;
import io.github.mqzen.menus.misc.button.actions.ButtonClickAction;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * @Author <a href="https://github.com/Cobeine">Cobeine</a>
 */

public class CloseMenuAction implements ButtonClickAction {

    @Override
    public String tag() {
        return "CLOSE";
    }

    @Override
    public void execute(MenuView<?> menu, InventoryClickEvent event, DataRegistry data) {
        event.getWhoClicked().closeInventory();

    }
}
