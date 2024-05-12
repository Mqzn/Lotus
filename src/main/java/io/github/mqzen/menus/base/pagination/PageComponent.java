package io.github.mqzen.menus.base.pagination;

import io.github.mqzen.menus.misc.Button;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public interface PageComponent {
	
	ItemStack toItem();
	
	void onClick(Page page, InventoryClickEvent event);
	
	default Button toButton() {
		return Button.clickable(toItem(),
			(menu, clickEvent) -> onClick((Page) menu, clickEvent));
	}
}
