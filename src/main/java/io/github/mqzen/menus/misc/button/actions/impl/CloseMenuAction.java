package io.github.mqzen.menus.misc.button.actions.impl;

import io.github.mqzen.menus.base.MenuView;
import io.github.mqzen.menus.misc.button.actions.ButtonClickAction;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * @author <a href="https://github.com/Cobeine">Cobeine</a>
 */

public final class CloseMenuAction implements ButtonClickAction {

    @Override
    public String tag() {
        return "CLOSE";
    }

    @Override
    public void execute(MenuView<?> menu, InventoryClickEvent event) {
        event.getWhoClicked().closeInventory();
    }
}
