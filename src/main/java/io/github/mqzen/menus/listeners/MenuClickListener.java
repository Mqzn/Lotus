package io.github.mqzen.menus.listeners;

import io.github.mqzen.menus.Lotus;
import io.github.mqzen.menus.base.MenuView;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public final class MenuClickListener implements Listener {
	
	private final Lotus manager;
	
	public MenuClickListener(Lotus api) {
		this.manager = api;
	}
	
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onClick(InventoryClickEvent e) {
		Player clicker = (Player) e.getWhoClicked();
		
		Inventory topInventory = e.getInventory();
		Inventory bottomInventory = e.getView().getBottomInventory();
		Inventory clickedInventory = e.getClickedInventory();
		
		//TODO create a powerful checker for bugs
		
		MenuView<?> menu = manager.getMenuView(clicker.getUniqueId()).orElseGet(() -> {
			if (topInventory.getHolder() instanceof MenuView<?> playerMenu) {
				manager.setOpenView(clicker, playerMenu);
				return playerMenu;
			}
			return null;
		});
		
		if (menu == null) {
			e.setCancelled(!manager.isAllowOutsideClick());
			return;
		}
		
		if (clickedInventory == null)
			return;
		
		if (clickedInventory.equals(bottomInventory))
			return;
		
		if (clickedInventory.equals(topInventory)) {
			menu.onClick(e);
		}
	}
	
}
