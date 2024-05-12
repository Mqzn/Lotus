package io.github.mqzen.menus.listeners;

import io.github.mqzen.menus.base.Lotus;
import io.github.mqzen.menus.base.PlayerMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public final class MenuClickListener implements Listener {
	
	private final Lotus manager;
	
	public MenuClickListener(Lotus manager) {
		this.manager = manager;
	}
	
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onClick(InventoryClickEvent e) {
		Player clicker = (Player) e.getWhoClicked();
		
		Inventory topInventory = e.getInventory();
		Inventory bottomInventory = e.getView().getBottomInventory();
		Inventory clickedInventory = e.getClickedInventory();
		
		//TODO create a powerful checker for bugs
		
		PlayerMenu<?> menu = manager.getOpenMenus(clicker.getUniqueId()).orElseGet(() -> {
			if (topInventory.getHolder() instanceof PlayerMenu<?> playerMenu) {
				manager.setOpenMenus(clicker, playerMenu);
				return playerMenu;
			}
			return null;
		});
		
		if (menu == null) {
			e.setCancelled(!manager.isAllowOutsideClick());
			return;
		}
		
		int clickedSlot = e.getSlot();
		if (clickedInventory == null)
			return;
		
		if (clickedInventory.equals(bottomInventory))
			return;
		
		if (clickedInventory.equals(topInventory)) {
			menu.executeItemAction(clickedSlot, e);
		}
	}
	
}
