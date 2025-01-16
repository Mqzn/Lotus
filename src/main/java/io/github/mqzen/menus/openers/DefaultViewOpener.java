package io.github.mqzen.menus.openers;

import io.github.mqzen.menus.Lotus;
import io.github.mqzen.menus.base.MenuView;
import io.github.mqzen.menus.base.ViewOpener;
import io.github.mqzen.menus.misc.ViewData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public final class DefaultViewOpener implements ViewOpener {
	
	/**
	 * Creates an inventory , opens it for the player using the dynamic data
	 * of the menu that is cached within 'ViewData'
	 *
	 * @param manager  the manager
	 * @param player   the player opening this menu
	 * @param menu     the menu to open
	 * @param viewData the data of the menu to open
	 * @return the menu inventory opened for this player
	 */
	@Override
	public @NotNull Inventory openMenu(Lotus manager, Player player,
	                                   MenuView<?> menu, ViewData viewData) {
		int size = viewData.capacity().getTotalSize();
		String title = viewData.title().asString();
		
		Inventory inv = Bukkit.createInventory(menu, size, title);
		
		viewData.content().forEachItem((slot, button) ->
			inv.setItem(slot.getSlot(), button.getItem()));

		player.openInventory(inv);
		return inv;
	}
}
