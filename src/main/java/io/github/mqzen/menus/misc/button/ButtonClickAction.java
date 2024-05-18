package io.github.mqzen.menus.misc.button;

import io.github.mqzen.menus.base.MenuView;
import org.bukkit.event.inventory.InventoryClickEvent;

@FunctionalInterface
public interface ButtonClickAction {
	
	ButtonClickAction CLOSE_MENU = (menu, event) -> event.getWhoClicked().closeInventory();
	
	
	void execute(MenuView<?> menu, InventoryClickEvent event);
	
}
