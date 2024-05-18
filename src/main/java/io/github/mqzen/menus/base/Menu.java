package io.github.mqzen.menus.base;


import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.DataRegistry;
import io.github.mqzen.menus.titles.MenuTitle;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

public interface Menu {
	
	/**
	 * @return The unique name for this menu
	 */
	String getName();
	
	/**
	 * Type of inventory you're creating
	 *
	 * @return the type of inventory
	 */
	default InventoryType getMenuType() {
		return InventoryType.CHEST;
	}
	
	/**
	 * @param opener the player who is opening this menu
	 * @return the title for this menu
	 */
	@NotNull MenuTitle getTitle(DataRegistry extraData, Player opener);
	
	/**
	 * @param opener the player who is opening this menu
	 * @return the capacity/size for this menu
	 */
	@NotNull Capacity getCapacity(DataRegistry extraData, Player opener);
	
	/**
	 * Creates the content for the menu
	 *
	 * @param opener   the player opening this menu
	 * @param capacity the capacity set by the user above
	 * @return the content of the menu to add (this includes items)
	 */
	@NotNull Content getContent(DataRegistry extraData, Player opener, Capacity capacity);
	
	/**
	 * What's going to happen on close
	 *
	 * @param menu  the menu closing
	 * @param event the event of closing the inventory of this menu
	 */
	default void onClose(MenuView<?> menu, InventoryCloseEvent event) {
	}
	
	/**
	 * What's going to happen on opening of the menu's inventory
	 *
	 * @param playerMenuView the holder of the inventory opening
	 * @param event          the inventory open event
	 */
	default void onOpen(MenuView<?> playerMenuView, InventoryOpenEvent event) {
	}
	
	
}
