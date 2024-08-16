package io.github.mqzen.menus.base.pagination;

import io.github.mqzen.menus.misc.button.Button;
import io.github.mqzen.menus.misc.button.actions.ButtonClickAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public interface PageComponent {
	
	ItemStack toItem();
	
	void onClick(PageView pageView, InventoryClickEvent event);
	
	default Button toButton() {
		return Button.clickable(toItem(),
				ButtonClickAction.plain((menu, clickEvent) -> onClick((PageView) menu, clickEvent)));
	}
}
