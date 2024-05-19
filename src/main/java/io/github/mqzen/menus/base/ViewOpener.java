package io.github.mqzen.menus.base;

import io.github.mqzen.menus.misc.ViewData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * Defines how the menu view is being opened
 */
public interface ViewOpener {
	
	/**
	 * Creates an inventory , opens it for the player using the dynamic data
	 * of the menu that is cached within 'ViewData'
	 *
	 * @param player       the player opening this menu
	 * @param baseMenuView the menu view to open
	 * @param viewData     the data of the menu to open
	 * @return the menu inventory opened for this player
	 */
	@NotNull Inventory openMenu(Player player,
	                            MenuView<?> baseMenuView, ViewData viewData);
}
