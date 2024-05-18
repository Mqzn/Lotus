package io.github.mqzen.menus.base.pagination;

import io.github.mqzen.menus.misc.button.Button;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public interface PageComponent {
	
	ItemStack toItem();
	
	void onClick(PageView pageView, InventoryClickEvent event);
	
	default Button toButton() {
		return Button.clickable(toItem(),
			(menu, clickEvent) -> onClick((PageView) menu, clickEvent));
	}
}
