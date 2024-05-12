package io.github.mqzen.menus.listeners;

import io.github.mqzen.menus.base.PlayerMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public class MenuOpenListener implements Listener {
	
	
	@EventHandler
	public void onOpen(InventoryOpenEvent e) {
		Inventory inventory = e.getInventory();
		if (!(inventory.getHolder() instanceof PlayerMenu<?> menu))
			return;
		menu.preOpen(e);
	}
	
}
