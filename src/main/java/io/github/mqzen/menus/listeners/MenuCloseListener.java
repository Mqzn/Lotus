package io.github.mqzen.menus.listeners;

import io.github.mqzen.menus.base.Lotus;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class MenuCloseListener implements Listener {
	
	private final Lotus manager;
	
	public MenuCloseListener(Lotus manager) {
		this.manager = manager;
	}
	
	
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		
		Player closer = (Player) e.getPlayer();
		manager.getOpenMenus(closer.getUniqueId())
			.ifPresent((menu) ->
				manager.preClosePlayerMenu(menu, e));
	}
}
