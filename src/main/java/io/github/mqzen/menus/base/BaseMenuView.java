package io.github.mqzen.menus.base;

import io.github.mqzen.menus.Lotus;
import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.DataRegistry;
import io.github.mqzen.menus.misc.Slot;
import io.github.mqzen.menus.misc.ViewData;
import io.github.mqzen.menus.misc.button.Button;
import io.github.mqzen.menus.misc.button.ButtonCondition;
import io.github.mqzen.menus.misc.button.ButtonUpdater;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Represents player menu
 */
public class BaseMenuView<M extends Menu> implements MenuView<M> {
	
	protected final M menu;
	protected final DataRegistry dataRegistry = DataRegistry.empty();
	
	protected final Lotus api;
	
	protected ViewData currentOpenedData = null;
	protected Inventory currentOpenInventory = null;
	protected Player currentOpener = null;
	
	public BaseMenuView(Lotus api, M menu) {
		this.api = api;
		this.menu = menu;
	}
	
	/**
	 * @return The API instance
	 */
	@Override
	public Lotus getAPI() {
		return api;
	}
	
	/**
	 * Get the menu instance that will be used to
	 * create the menu
	 *
	 * @return the menu instance
	 */
	@Override
	public @NotNull M getMenu() {
		return menu;
	}
	
	/**
	 * The data that were created from the information held
	 * by the menu instance
	 *
	 * @return the menu-view's data
	 * @see Menu
	 * the data may be absent if the menu-view hasn't been open yet
	 */
	@Override
	public @NotNull Optional<ViewData> getData() {
		return Optional.ofNullable(currentOpenedData);
	}
	
	/**
	 * sometimes menu views are opened with extra data embedded in them
	 * for example: In page view , it has its index and its pagination objects embedded inside it.
	 * so these objects are fetched through the method DataRegistry#getData()
	 *
	 * @return the extra data for this menu view
	 */
	@Override
	public @NotNull DataRegistry getExtraData() {
		return dataRegistry;
	}
	
	/**
	 * The current player opening this menu view
	 * may be empty if the menu-view created is not open yet
	 *
	 * @return the opener for this OPEN menu.
	 */
	@Override
	public Optional<Player> getPlayer() {
		return Optional.ofNullable(currentOpener);
	}
	
	/**
	 * the current open inventory
	 *
	 * @return The open inventory
	 */
	@Override
	public @Nullable Inventory getInventory() {
		return currentOpenInventory;
	}
	
	/**
	 * Fetches the button  in the contents of the view that corresponds to the slot parameter
	 * then executes this button's cached actions regarding InventoryClickEvent
	 *
	 * @param slot  the slot clicked
	 * @param event the click event
	 */
	@Override
	public void onClickedSlot(int slot, InventoryClickEvent event) {
		if (!isOpen()) {
			api.sendComponent(event.getWhoClicked(), Component.text("Current Menu View's state (is closed and) doesn't allow for executing button actions ", NamedTextColor.RED));
			return;
		}
		
		this.getContent().getButton(slot)
			.ifPresent((button -> button.executeOnClick(this, event)));
	}
	
	/**
	 * Initializes the menu-view's data during pre-opening phase
	 *
	 * @param menu   the menu to be used in creating the menu view's data
	 * @param player the player opening
	 * @see ViewData
	 */
	@Override
	public void initialize(M menu, Player player) {
		Capacity capacity = menu.getCapacity(dataRegistry, player);
		currentOpenedData = new ViewData(menu.getTitle(dataRegistry, player),
			capacity, menu.getContent(dataRegistry, player, capacity));
	}
	
	/**
	 * @return whether the menu view been open yet or not
	 */
	@Override
	public boolean isOpen() {
		return currentOpenedData != null
			&& currentOpener != null
			&& currentOpenInventory != null;
	}
	
	/**
	 * Opens the menu view using some information
	 * such as the way on how to open this view (ViewOpener) and the
	 * player who is opening and in which the view opened will be displayed for
	 *
	 * @param viewOpener Interface defining how to open this view
	 * @param player     the player opening this view
	 */
	@Override
	public void openView(ViewOpener viewOpener, Player player) {
		currentOpener = player;
		initialize(menu, player);
		currentOpenInventory = viewOpener.openMenu(api, player, this, currentOpenedData);
	}
	
	/**
	 * Searches for buttons in the open view of the menu
	 *
	 * @param condition the condition that the buttons are searched for
	 * @return the buttons that meet the condition stated
	 */
	@Override
	public List<Button> searchForButtons(ButtonCondition condition) {
		return getContent().stream()
			.filter((entry) -> condition.accepts(entry.getKey(), entry.getValue()))
			.map(Map.Entry::getValue)
			.collect(Collectors.toList());
	}
	
	/**
	 * Replaces the ItemStack in that specific slot with a new ItemStack
	 *
	 * @param slot      the slot
	 * @param newItem   the new itemStack that will replace the old clicked itemStack
	 * @param overwrite whether we just replace the ItemStack and the actions onClick are completely overwritten and hence become empty (by creating a new button internally)
	 *                  or else we change the ItemStack only with retaining their actions onClick
	 */
	@Override
	public void replaceItemStack(Slot slot, @Nullable ItemStack newItem, boolean overwrite) {
		if (!isOpen())
			return;
		getContent().getButton(slot).ifPresent((oldButton) -> {
			Button toReplace = overwrite ? Button.empty(newItem) : oldButton.setItem(newItem);
			getContent().setButton(slot, toReplace);
		});
		
	}
	
	/**
	 * Replaces the button in that specific slot with a new button
	 *
	 * @param slot      the slot
	 * @param newButton the new button that will replace the old clicked button
	 */
	@Override
	public void replaceButton(@NotNull Slot slot, @Nullable Button newButton) {
		if (!isOpen())
			return;
		
		getContent().setButton(slot, newButton);
	}
	
	/**
	 * Updates a button at a specific position
	 *
	 * @param slot          the position of the button to update
	 * @param buttonUpdater the updater of the button
	 */
	@Override
	public void updateButton(Slot slot, ButtonUpdater buttonUpdater) {
		getContent().updateButton(slot, buttonUpdater::update);
	}
	
	/**
	 * Updates an ItemStack at a specific position/slot
	 *
	 * @param slot             the position of the button to update
	 * @param itemStackUpdater the updater of the ItemStack
	 */
	@Override
	public void updateItemStack(Slot slot, Consumer<ItemStack> itemStackUpdater) {
		getContent().updateButton(slot, (button) -> {
			ItemStack buttonItem = button.getItem();
			itemStackUpdater.accept(buttonItem);
			button.setItem(buttonItem);
		});
	}
	
	/**
	 * Updates all buttons that meets a certain criteria
	 *
	 * @param condition     the criteria that the buttons must meet so that they get updated
	 * @param buttonUpdater the function that updates the buttons which meet the criteria
	 */
	@Override
	public void updateAll(ButtonCondition condition, ButtonUpdater buttonUpdater) {
		getContent().forEachItem((slot, button) -> {
			if (!condition.accepts(slot, button)) return;
			this.updateButton(slot, buttonUpdater);
		});
	}
	
}
