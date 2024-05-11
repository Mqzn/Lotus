package io.github.mqzen.menus.base;

import io.github.mqzen.menus.misc.MenuCache;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public interface Opener {
	
	/**
	 * Creates an inventory , opens it for the player using the dynamic data
	 * of the menu that is cached within 'MenuCache'
	 *
	 * @param manager the manager
	 * @param player the player opening this menu
	 * @param menu the menu to open
	 * @param menuCache the data of the menu to open
	 * @return the menu inventory opened for this player
	 */
	@NotNull Inventory openMenu(Lotus manager, Player player,
	                            PlayerMenu<?> menu, MenuCache menuCache);
}
