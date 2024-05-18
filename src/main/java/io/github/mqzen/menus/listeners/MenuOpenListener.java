package io.github.mqzen.menus.listeners;

import io.github.mqzen.menus.Lotus;
import io.github.mqzen.menus.base.MenuView;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public class MenuOpenListener implements Listener {
	
	private final Lotus api;
	
	public MenuOpenListener(Lotus api) {
		this.api = api;
	}
	
	@EventHandler
	public void onOpen(InventoryOpenEvent e) {
		Inventory inventory = e.getInventory();
		if (!(inventory.getHolder() instanceof MenuView<?> menu))
			return;
		api.setOpenView((Player) e.getPlayer(), menu);
		menu.onOpen(e);
	}
	
}
