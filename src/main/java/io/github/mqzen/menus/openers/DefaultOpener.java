package io.github.mqzen.menus.openers;

import io.github.mqzen.menus.base.Lotus;
import io.github.mqzen.menus.base.Opener;
import io.github.mqzen.menus.base.PlayerMenu;
import io.github.mqzen.menus.misc.MenuCache;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class DefaultOpener implements Opener {
	
	/**
	 * Creates an inventory , opens it for the player using the dynamic data
	 * of the menu that is cached within 'MenuCache'
	 *
	 * @param manager  the manager
	 * @param player   the player opening this menu
	 * @param menu     the menu to open
	 * @param menuCache the data of the menu to open
	 * @return the menu inventory opened for this player
	 */
	@Override
	public @NotNull Inventory openMenu(Lotus manager, Player player,
	                                   PlayerMenu<?> menu, MenuCache menuCache) {
		int size = menuCache.capacity().getTotalSize();
		Component title = menuCache.title().asComponent();
		
		Inventory inv = Bukkit.createInventory(menu, size, title);
		
		menuCache.content().forEachItem((slot, button)->
			inv.setItem(slot.getSlot(),button.getItem()));
		
		return Objects.requireNonNull(player.openInventory(inv)).getTopInventory();
	}
}
