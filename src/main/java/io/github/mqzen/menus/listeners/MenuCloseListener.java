package io.github.mqzen.menus.listeners;

import io.github.mqzen.menus.Lotus;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public final class MenuCloseListener implements Listener {
	
	private final Lotus api;
	
	public MenuCloseListener(Lotus api) {
		this.api = api;
	}
	
	
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		
		Player closer = (Player) e.getPlayer();
		api.getMenuView(closer.getUniqueId())
			.ifPresent((menu) ->
				api.closeView(menu, e));
	}
}
