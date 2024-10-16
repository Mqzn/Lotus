package io.github.mqzen.menus.base;

import io.github.mqzen.menus.Lotus;
import io.github.mqzen.menus.misc.ViewData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * The ViewOpener interface defines a contract for creating and opening an
 * inventory for a player using dynamic data from a cached menu within 'ViewData'.
 */
public interface ViewOpener {
	
	/**
	 * Creates an inventory , opens it for the player using the dynamic data
	 * of the menu that is cached within 'ViewData'
	 *
	 * @param manager      the manager
	 * @param player       the player opening this menu
	 * @param baseMenuView the menu view to open
	 * @param viewData     the data of the menu to open
	 * @return the menu inventory opened for this player
	 */
	@NotNull Inventory openMenu(Lotus manager, Player player,
	                            MenuView<?> baseMenuView, ViewData viewData);
}
