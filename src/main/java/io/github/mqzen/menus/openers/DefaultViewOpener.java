package io.github.mqzen.menus.openers;

import io.github.mqzen.menus.base.MenuView;
import io.github.mqzen.menus.base.ViewOpener;
import io.github.mqzen.menus.misc.ViewData;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class DefaultViewOpener implements ViewOpener {
	
	/**
	 * Creates an inventory , opens it for the player using the dynamic data
	 * of the menu that is cached within 'ViewData'
	 *
	 * @param player   the player opening this menu
	 * @param menu     the menu to open
	 * @param viewData the data of the menu to open
	 * @return the menu inventory opened for this player
	 */
	@Override
	public @NotNull Inventory openMenu(Player player,
	                                   MenuView<?> menu, ViewData viewData) {
		int size = viewData.capacity().getTotalSize();
		Component title = viewData.title().asComponent();
		
		Inventory inv = Bukkit.createInventory(menu, size, title);
		
		viewData.content().forEachItem((slot, button) ->
			inv.setItem(slot.getSlot(), button.getItem()));
		
		return Objects.requireNonNull(player.openInventory(inv)).getTopInventory();
	}
}
