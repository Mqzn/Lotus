package io.github.mqzen.menus.base;

import io.github.mqzen.menus.Lotus;
import io.github.mqzen.menus.misc.DataRegistry;
import io.github.mqzen.menus.misc.Slot;
import io.github.mqzen.menus.misc.ViewData;
import io.github.mqzen.menus.misc.button.Button;
import io.github.mqzen.menus.misc.button.ButtonCondition;
import io.github.mqzen.menus.misc.button.ButtonUpdater;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * This class represents an open view of a menu.
 * It holds the menu's contents, title, and size/capacity.
 * It also contains a lot of useful methods for accessing open menu-view/inventory,
 * allowing it to be modified fancily!
 *
 * @param <M> the menu type this view is displayed using.
 */
public interface MenuView<M extends Menu> extends InventoryHolder {
	
	/**
	 * @return The API instance
	 */
	Lotus getAPI();
	
	/**
	 * Get the menu instance that will be used to
	 * create the menu
	 *
	 * @return the menu instance
	 */
	@NotNull M getMenu();
	
	/**
	 * The data that were created from the information held
	 * by the menu instance
	 *
	 * @return the menu-view's data
	 * @see Menu
	 * the data may be absent if the menu-view hasn't been open yet
	 */
	@NotNull Optional<ViewData> getData();
	
	/**
	 * sometimes menu views are opened with extra data embedded in them
	 * for example, In page view, it has its index and its pagination objects embedded inside it.
	 * so these objects are fetched through the method DataRegistry#getData()
	 *
	 * @return the extra data for this menu view
	 */
	@NotNull DataRegistry getExtraData();
	
	/**
	 * The current player opening this menu view
	 * may be empty if the menu-view created is not open yet
	 *
	 * @return the opener for this OPEN menu.
	 */
	Optional<Player> getPlayer();
	
	/**
	 * the current open inventory
	 * returns null if the view is NOT OPEN for some reason!
	 *
	 * @return The open inventory
	 */
	@Override
	@Nullable Inventory getInventory();
	
	
	/**
	 * Fetches the button in the contents of the view that corresponds to the slot parameter
	 * then executes this button's cached actions regarding InventoryClickEvent
	 *
	 * @param slot  the slot clicked
	 * @param event the click event
	 */
	void onClickedSlot(int slot, InventoryClickEvent event);
	
	/**
	 * Initializes the menu-view's data during pre-opening phase
	 *
	 * @param menu   the menu to be used in creating the menu view's data
	 * @param player the player opening
	 * @see ViewData
	 */
	void initialize(M menu, Player player);
	
	/**
	 * @return whether the menu view been open yet or not
	 */
	boolean isOpen();
	
	/**
	 * Opens the menu view using some information
	 * such as the way on how to open this view (ViewOpener) and the
	 * player who is opening and in which the view opened will be displayed for
	 *
	 * @param viewOpener Interface defining how to open this view
	 * @param player     the player opening this view
	 */
	void openView(ViewOpener viewOpener, Player player);
	
	
	/**
	 * Searches for buttons in the open view of the menu
	 *
	 * @param condition the condition that the buttons are searched for
	 * @return the buttons that meet the condition stated
	 */
	List<Button> searchForButtons(ButtonCondition condition);
	
	/**
	 * Replaces the ItemStack in that specific slot with a new ItemStack
	 *
	 * @param slot      the slot
	 * @param newItem   the new itemStack that will replace the old clicked itemStack
	 * @param overwrite whether we just replace the ItemStack and the actions onClick are completely overwritten and hence become empty (by creating a new button internally)
	 *                  or else, we change the ItemStack only with retaining their actions onClick
	 */
	void replaceItemStack(
		Slot slot,
		@Nullable ItemStack newItem,
		boolean overwrite
	);
	
	
	/**
	 * Replaces the button in that specific slot with a new button
	 *
	 * @param slot      the slot
	 * @param newButton the new button that will replace the old clicked button
	 */
	void replaceButton(@NotNull Slot slot, @Nullable Button newButton);
	
	
	/**
	 * Updates a button at a specific position
	 *
	 * @param slot          the position of the button to update
	 * @param buttonUpdater the updater of the button
	 */
	void updateButton(Slot slot, ButtonUpdater buttonUpdater);
	
	/**
	 * Updates an ItemStack at a specific position/slot
	 *
	 * @param slot             the position of the button to update
	 * @param itemStackUpdater the updater of the ItemStack
	 */
	void updateItemStack(Slot slot, Consumer<ItemStack> itemStackUpdater);
	
	/**
	 * Updates all buttons that meets a certain criteria
	 *
	 * @param condition     the criteria that the buttons must meet so that they get updated
	 * @param buttonUpdater the function that updates the buttons which meet the criteria
	 */
	void updateAll(ButtonCondition condition, ButtonUpdater buttonUpdater);
	
	/**
	 * The current live content of the view
	 * basically represents the current menu content displayed in this view
	 *
	 * @return the content for this view
	 * @throws NullPointerException if the view is not open yet and tried to access the content while being closed/not open
	 */
	default Content getContent() {
		return getData().orElseThrow(NullPointerException::new).content();
	}
	
	/**
	 * @return the type of menu being viewed/to be viewed(just in case if the player hasn't opened it yet) by the player
	 */
	default InventoryType getType() {
		return getMenu().getMenuType();
	}

	default boolean onPreClick(InventoryClickEvent event) {
		return getMenu().onPreClick(this, event);
	}

	/**
	 * Defines what's going to happen after the button click action
	 * @param event the click event
	 */
	default void onPostClick(InventoryClickEvent event) {
		getMenu().onPostClick(this, event);
	}
	
	/**
	 * What occurs/is executed on opening of this menu view
	 *
	 * @param event the open event for the view's internal inventory
	 */
	default void onOpen(InventoryOpenEvent event) {
		getMenu().onOpen(this, event);
	}

	/**
	 * Handles the drag event in an inventory.
	 *
	 * @param event the InventoryDragEvent that occurs when an item is dragged within the inventory
	 */
	default void onDrag(InventoryDragEvent event) {
		getMenu().onDrag(this, event);
	}

	/**
	 * What occurs/is executed on closing of this menu view
	 *
	 * @param event the close event for the view's internal inventory
	 */
	default void onClose(InventoryCloseEvent event) {
		getMenu().onClose(this, event);
	}
	
	
	/**
	 * Executes instructions on click event
	 * Internally: BaseMenuView#onClickedSlot
	 *
	 * @param event the click event
	 */
	default void handleOnClick(InventoryClickEvent event) {
		if(!onPreClick(event) ) {
			return;
		}
		onClickedSlot(event.getSlot(), event);
		onPostClick(event);
	}
	
	/**
	 * Adds one or more buttons to the inventory content of the view
	 *
	 * @param buttons the buttons to add
	 */
	default void addButtons(Button... buttons) {
		getContent().addButton(buttons);
	}
	
	/**
	 * Replaces the button in that specific numerical-slot with a new button
	 *
	 * @param slot      the slot
	 * @param newButton the new button that will replace the old clicked button
	 */
	default void replaceButton(int slot,
	                           @Nullable Button newButton) {
		replaceButton(Slot.of(slot), newButton);
	}
	
	/**
	 * Replaces the button in that specific row and column with a new button
	 *
	 * @param row       the row of the button to replace with the new button
	 * @param col       the column of the button to replace with the new button
	 * @param newButton the new button that will replace the old clicked button
	 */
	default void replaceButton(int row, int col, @Nullable Button newButton) {
		replaceButton(Slot.of(row, col), newButton);
	}
	
	/**
	 * Replaces the clicked button during the click-event with a new button
	 *
	 * @param event     the click-event
	 * @param newButton the new button that will replace the old clicked button
	 */
	default void replaceClickedButton(InventoryClickEvent event, @Nullable Button newButton) {
		replaceButton(event.getSlot(), newButton);
	}
	
	/**
	 * Replaces the ItemStack in that specific slot with a new ItemStack
	 *
	 * @param slot      the slot
	 * @param newItem   the new itemStack that will replace the old clicked itemStack
	 * @param overwrite whether we just replace the ItemStack and the actions onClick are completely overwritten and hence become empty (by creating a new button internally)
	 *                  or else we change the ItemStack only with retaining their actions onClick
	 */
	default void replaceItemStack(int slot, boolean overwrite, @Nullable ItemStack newItem) {
		replaceItemStack(Slot.of(slot), newItem, overwrite);
	}
	
	/**
	 * Replaces the ItemStack in that specific row:column with a new ItemStack
	 *
	 * @param row       the row
	 * @param col       the column
	 * @param newItem   the new itemStack that will replace the old clicked itemStack
	 * @param overwrite whether we just replace the ItemStack and the actions onClick are completely overwritten and hence become empty (by creating a new button internally)
	 *                  or else we change the ItemStack only with retaining their actions onClick
	 */
	default void replaceItemStack(int row, int col, boolean overwrite, @Nullable ItemStack newItem) {
		replaceItemStack(Slot.of(row, col), newItem, overwrite);
	}
	
	/**
	 * Replaces the ItemStack in that specific slot with a new ItemStack
	 *
	 * @param slot    the slot
	 * @param newItem the new itemStack that will replace the old clicked itemStack
	 */
	default void replaceItemStack(Slot slot, @Nullable ItemStack newItem) {
		replaceItemStack(slot, newItem, true);
	}
	
	/**
	 * Replaces the ItemStack in that specific row:column with a new ItemStack
	 *
	 * @param row     the row
	 * @param col     the column
	 * @param newItem the new itemStack that will replace the old clicked itemStack
	 */
	default void replaceItemStack(int row, int col, @Nullable ItemStack newItem) {
		replaceItemStack(row, col, true, newItem);
	}
	
	/**
	 * Replaces the ItemStack in that specific slot with a new ItemStack
	 *
	 * @param slot    the slot
	 * @param newItem the new itemStack that will replace the old clicked itemStack
	 */
	default void replaceItemStack(int slot, @Nullable ItemStack newItem) {
		replaceItemStack(Slot.of(slot), newItem, true);
	}
	
	/**
	 * Replaces the clicked button during the click-event with a new button
	 *
	 * @param event        the click-event
	 * @param newItemStack the new ItemStack that will replace the old clicked ItemStack
	 * @param overwrite    whether we just replace the ItemStack and the actions onClick are completely overwritten and hence become empty (by creating a new button internally)
	 *                     or else, we change the ItemStack only with retaining their actions onClick
	 */
	default void replaceClickedItemStack(InventoryClickEvent event, @Nullable ItemStack newItemStack, boolean overwrite) {
		int slot = event.getSlot();
		getContent().getButton(slot).ifPresent((oldButton) -> {
			Button toReplace = overwrite ? Button.empty(newItemStack) : oldButton.setItem(newItemStack);
			replaceButton(slot, toReplace);
		});
	}
	
	/**
	 * Replaces the clicked button during the click-event with a new button
	 *
	 * @param event        the click-event
	 * @param newItemStack the new ItemStack that will replace the old clicked ItemStack
	 */
	default void replaceClickedItemStack(InventoryClickEvent event, @Nullable ItemStack newItemStack) {
		replaceClickedItemStack(event, newItemStack, true);
	}
	
	/**
	 * Updates a button at a specific position
	 *
	 * @param slot          the position of the button to update
	 * @param buttonUpdater the updater of the button
	 */
	default void updateButton(int slot, ButtonUpdater buttonUpdater) {
		updateButton(Slot.of(slot), buttonUpdater);
	}
	
	/**
	 * Updates a button at a specific x:y position (row:column)
	 *
	 * @param row           the row of the button to update
	 * @param col           the column of the button to update
	 * @param buttonUpdater the updater of the button
	 */
	default void updateButton(int row, int col, ButtonUpdater buttonUpdater) {
		updateButton(Slot.of(row, col), buttonUpdater);
	}
	
	/**
	 * Updates an ItemStack at a specific position/slot
	 *
	 * @param slot             the position of the button to update
	 * @param itemStackUpdater the updater of the ItemStack
	 */
	default void updateItemStack(int slot, Consumer<ItemStack> itemStackUpdater) {
		updateItemStack(Slot.of(slot), itemStackUpdater);
	}
	
	
	/**
	 * Updates an ItemStack at a specific x:y position (row:column)
	 *
	 * @param row               the row of the ItemStack to update
	 * @param col               the column of the ItemStack to update
	 * @param itemStackConsumer the updater of the ItemStack
	 */
	default void updateItemStack(int row, int col, Consumer<ItemStack> itemStackConsumer) {
		updateItemStack(Slot.of(row, col), itemStackConsumer);
	}

	/**
	 * Refreshes the current state of the object.
	 * <p>
	 * This method is used to reinitialize or reset the object's state,
	 * ensuring that it is up-to-date with any changes or updates that
	 * may have occurred. Implementing this method may involve tasks
	 * such as clearing caches, reloading configuration settings, or
	 * reinitializing internal variables to their default states.
	 */
	void refresh();
}
