package io.github.mqzen.menus.base.pagination;

import io.github.mqzen.menus.base.Content;
import io.github.mqzen.menus.base.Menu;
import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.Slot;
import io.github.mqzen.menus.misc.button.Button;
import io.github.mqzen.menus.misc.button.actions.ButtonClickAction;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract class representing a Page in a paginated menu system.
 * A Page provides methods to define its button layout and interaction behavior.
 */
public abstract class Page implements Menu {
	
	protected Page() {
	
	}
	
	/**
	 * The range of button-filling of {@link PageComponent}
	 * this page should have
	 *
	 * @param capacity the capacity for the page
	 * @param opener   opener of this pagination
	 *
	 * @see FillRange
	 *
	 * @return The range of button-filling of {@link PageComponent}
	 */
	public abstract FillRange getFillRange(Capacity capacity, Player opener);
	
	/**
	 * Determines and returns the slot designated for the "next page" button in a paginated menu system.
	 *
	 * @param capacity the capacity of the current page
	 * @return the slot designated for the "next page" button
	 */
	public Slot nextPageSlot(Capacity capacity) {
		return Slot.last(capacity);
	}
	
	/**
	 * Calculates the slot position for the "previous page" button on a paginated menu.
	 *
	 * @param capacity the total capacity of slots available in the menu
	 * @return the slot position for the "previous page" button
	 */
	public Slot previousPageSlot(Capacity capacity) {
		return nextPageSlot(capacity).subtractBy(8);
	}
	
	/**
	 * Retrieves the item stack representing the button for navigating to the next page in a paginated menu.
	 *
	 * @param player the player interacting with the menu and for whom the item stack is being retrieved
	 * @return the item stack representing the next page button for the specified player
	 */
	public abstract ItemStack nextPageItem(Player player);
	
	/**
	 * Provides the item stack representation for the "previous page" button in the paginated menu.
	 *
	 * @param player the player for whom the previous page item is being created
	 * @return the ItemStack representing the previous page button
	 */
	public abstract ItemStack previousPageItem(Player player);
	
	/**
	 * Generates the default content layout for a paginated menu system.
	 * This includes setting up the next and previous page buttons if applicable.
	 *
	 * @param pagination the pagination object to control page navigation
	 * @param pageView the current page view state
	 * @param capacity the capacity of the menu
	 * @param player the player interacting with the menu
	 * @return the content of the menu with navigation buttons set
	 */
	public final Content defaultContent(
		Pagination pagination,
		PageView pageView,
		Capacity capacity,
		Player player
	) {
		Slot previousButtonSlot = previousPageSlot(capacity);
		Slot nextButtonSlot = nextPageSlot(capacity);
		
		Content content = Content.empty(capacity);

		if (!pagination.isLast(pageView))
			content.setButton(nextButtonSlot, Button.clickable(nextPageItem(player),
					ButtonClickAction.plain((menu, event) -> {
						event.setCancelled(true);
						onSwitchingToNextPage(pagination, capacity, nextButtonSlot, (PageView) menu, event);
						pagination.next();
					})));
		
		if (!pagination.isFirst(pageView))
			content.setButton(previousButtonSlot, Button.clickable(previousPageItem(player),
					ButtonClickAction.plain((menu, event) -> {
						event.setCancelled(true);
						onSwitchingToPreviousPage(pagination, capacity, previousButtonSlot, (PageView) menu, event);
						pagination.previous();
					})));
		
		return content;
	}

	protected void onSwitchingToNextPage(
		@NotNull Pagination pagination,
		@NotNull Capacity capacity,
		@NotNull Slot clickedSlot,
		@NotNull PageView clickedView,
		@NotNull InventoryClickEvent event
	) {

	}

	protected void onSwitchingToPreviousPage(
		@NotNull Pagination pagination,
		@NotNull Capacity capacity,
		@NotNull Slot clickedSlot,
		@NotNull PageView clickedView,
		@NotNull InventoryClickEvent event
	) {

	}
}
