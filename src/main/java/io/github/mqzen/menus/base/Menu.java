package io.github.mqzen.menus.base;


import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.DataRegistry;
import io.github.mqzen.menus.titles.MenuTitle;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.jetbrains.annotations.NotNull;

/**
 * The Menu interface provides the structure for creating custom menus
 * in an inventory system. It allows you to define the name, type, title,
 * capacity, and content of the menu. Additionally, it provides hooks
 * for handling events such as clicks, opening, and closing of the menu.
 */
public interface Menu {
	
	/**
	 * @return The unique name for this menu
	 */
	String getName();
	
	/**
	 * Type of inventory you're creating
	 * @return the type of inventory
	 */
	default InventoryType getMenuType() {
		return InventoryType.CHEST;
	}
	
	/**
	 * @param extraData the data container for this menu for extra data
	 * @param opener the player who is opening this menu
	 * @return the title for this menu
	 */
	@NotNull MenuTitle getTitle(DataRegistry extraData, Player opener);
	
	/**
	 * @param extraData the data container for this menu for extra data
	 * @param opener the player who is opening this menu
	 * @return the capacity/size for this menu
	 */
	@NotNull Capacity getCapacity(DataRegistry extraData, Player opener);
	
	/**
	 * Creates the content for the menu
	 *
	 * @param extraData the data container for this menu for extra data
	 * @param opener   the player opening this menu
	 * @param capacity the capacity set by the user above
	 * @return the content of the menu to add (this includes items)
	 */
	@NotNull Content getContent(DataRegistry extraData, Player opener, Capacity capacity);
	
	/**
	 * What's going to happen before the click
	 * @param playerMenuView the menu view that the player has clicked on.
	 * @param event the click event
	 * @return if true, it continues to initiate the click actions of the button, otherwise it doesn't
	 */
	default boolean onPreClick(MenuView<?> playerMenuView, InventoryClickEvent event) {
		return true;
	}

	/**
	 * What's going to happen after the click
	 * @param playerMenuView the menu view that the player has clicked on.
	 * @param event the click event
	 */
	default void onPostClick(MenuView<?> playerMenuView, InventoryClickEvent event) {
	}
	
	/**
	 * What's going to happen on close
	 *
	 * @param playerMenuView  the menu closing
	 * @param event the event of closing the inventory of this menu
	 */
	default void onClose(MenuView<?> playerMenuView, InventoryCloseEvent event) {
	}
	
	/**
	 * What's going to happen on opening of the menu's inventory
	 *
	 * @param playerMenuView the holder of the inventory opening
	 * @param event          the inventory open event
	 */
	default void onOpen(MenuView<?> playerMenuView, InventoryOpenEvent event) {
	}

	/**
	 * Handles the inventory drag event within the menu. This method is called when a player
	 * drags an item within the inventory associated with this menu.
	 *
	 * @param playerMenuView the menu view that the player is interacting with.
	 * @param event the drag event that occurred in the inventory.
	 */
	default void onDrag(MenuView<?> playerMenuView, InventoryDragEvent event) {
	}
	
}
