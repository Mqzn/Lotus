package io.github.mqzen.menus.misc;

import io.github.mqzen.menus.base.PlayerMenu;
import org.bukkit.event.inventory.InventoryClickEvent;

@FunctionalInterface
public interface ButtonAction {
	
	void execute(PlayerMenu<?> menu, InventoryClickEvent event);
	
}
