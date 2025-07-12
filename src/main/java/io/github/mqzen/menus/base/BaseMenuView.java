package io.github.mqzen.menus.base;

import io.github.mqzen.menus.Lotus;
import io.github.mqzen.menus.base.animation.AnimatedButton;
import io.github.mqzen.menus.base.animation.ButtonAnimationTask;
import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.DataRegistry;
import io.github.mqzen.menus.misc.Slot;
import io.github.mqzen.menus.misc.ViewData;
import io.github.mqzen.menus.misc.button.Button;
import io.github.mqzen.menus.misc.button.ButtonCondition;
import io.github.mqzen.menus.misc.button.ButtonUpdater;
import io.github.mqzen.menus.titles.MenuTitle;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Base class for representing a menu view in the application.
 * This view binds a {@link Menu} and handles the interaction and rendering of the menu contents.
 *
 * @param <M> the type of menu the view will handle
 */
public class BaseMenuView<M extends Menu> implements MenuView<M> {
	
	/**
	 * Holds the menu instance associated with this view.
	 * It is used to create the menu and fetch its data.
	 * This field is immutable and is initialized via constructor.
	 */
	protected final M menu;
	/**
	 * A protected final instance of DataRegistry used for storing and retrieving supplementary data
	 * relevant to the menu view within the BaseMenuView class.
	 */
	protected final DataRegistry dataRegistry;
	
	/**
	 * API instance of type Lotus used within the BaseMenuView.
	 * This instance is intended to be final and protected,
	 * ensuring it is only accessible within subclasses and cannot be modified.
	 */
	protected final Lotus api;
	
	/**
	 * Holds the data of the currently opened menu view.
	 * <p>
	 * This variable contains the instance of {@link ViewData} representing the current state of the menu
	 * for a specific player when the menu is open. If the menu is not open, this value may be null.
	 * This data typically includes the menu's title, capacity, and content, and is used to render
	 * the menu view properly.
	 * </p>
	 */
	protected ViewData currentOpenedData = null;
	/**
	 * Represents the currently open inventory in the menu view.
	 * It may be null if no inventory is currently open.
	 */
	protected Inventory currentOpenInventory = null;

	/**
	 * The current player opening this menu view.
	 * This may be null if the menu-view is not yet open.
	 */
	protected Player currentOpener = null;

	
	protected final Map<AnimatedButton, ButtonAnimationTask> animationTasks = new HashMap<>();
	
	/**
	 * Constructs a BaseMenuView with the specified API instance and menu, and initializes it with an empty DataRegistry.
	 *
	 * @param api  the API instance
	 * @param menu the menu instance
	 */
	public BaseMenuView(Lotus api, M menu) {
		this(api, menu, DataRegistry.empty());
	}

	/**
	 * Constructs a new BaseMenuView with the specified API, menu, and data registry.
	 *
	 * @param api The API instance.
	 * @param menu The menu instance to be used to create the menu view.
	 * @param registry The data registry holding extra data for this menu view.
	 */
	public BaseMenuView(Lotus api, M menu,DataRegistry registry) {
		this.api = api;
		this.menu = menu;
		this.dataRegistry = registry;
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
	 * @return The open inventory
	 */
	@Override
	public @Nullable Inventory getInventory() {
		return currentOpenInventory;
	}
	
	/**
	 * Fetches the button in the contents of the view that corresponds to the slot parameter
	 * then executes this button's cached actions regarding InventoryClickEvent
	 *
	 * @param slot  the slot clicked
	 * @param event the click event
	 */
	@Override
	public void onClickedSlot(int slot, InventoryClickEvent event) {
		if (!isOpen()) {
			event.getWhoClicked().sendMessage(ChatColor.RED + "Current Menu View's state (is closed and) doesn't allow for executing button actions");
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
		
		MenuTitle title = menu.getTitle(dataRegistry, player);
		Capacity capacity = menu.getCapacity(dataRegistry, player);
		Content content = menu.getContent(dataRegistry, player, capacity);
		
		currentOpenedData = new ViewData(title, capacity, content);
	}
	
	/**
	 * @return whether the menu view been already open
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
		//after we open, we start all animation tasks
		for(Map.Entry<Slot, Button> entry : currentOpenedData.content().getButtonMap().entrySet()) {
			Slot slot = entry.getKey();
			Button button = entry.getValue();
			
			if(!(button instanceof AnimatedButton)) continue;
			AnimatedButton animatedButton = (AnimatedButton) button;
			ButtonAnimationTask task = ButtonAnimationTask.of(
				slot,
				animatedButton,
				this
			);
			animationTasks.put(animatedButton, task);
			task.start(api.getPlugin());
		}
		
	}
	
	/**
	 * What occurs/is executed on closing of this menu view
	 *
	 * @param event the close event for the view's internal inventory
	 */
	@Override
	public void onClose(InventoryCloseEvent event) {
		MenuView.super.onClose(event);
		for(Map.Entry<Slot, Button> entry : currentOpenedData.content().getButtonMap().entrySet()) {
			Button button = entry.getValue();
			
			if(!(button instanceof AnimatedButton)) continue;
			AnimatedButton animatedButton = (AnimatedButton) button;
			ButtonAnimationTask task = animationTasks.get(animatedButton);
			if(task != null) {
				task.cancel();
				animationTasks.remove(animatedButton);
			}
		}
		animationTasks.clear();
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
		if(newButton != null) {
			this.currentOpenInventory.setItem(slot.getSlot(), newButton.getItem());
			this.currentOpener.updateInventory();
		}
	}
	
	/**
	 * Updates a button at a specific position
	 *
	 * @param slot          the position of the button to update
	 * @param buttonUpdater the updater of the button
	 */
	@Override
	public void updateButton(Slot slot, ButtonUpdater buttonUpdater) {
		Button butt = getContent().getButton(slot).orElse(null);
		if(butt == null) return;
		buttonUpdater.update(butt);
		updateButtonAtWith(slot, butt);
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
		updateButtonAt(slot);
	}
	
	private void updateButtonAt(Slot slot) {
		getContent().getButton(slot).ifPresent((updatedButton)-> {
			currentOpenInventory.setItem(slot.getSlot(), updatedButton.getItem());
			currentOpener.updateInventory();
		});
	}
	
	private void updateButtonAtWith(Slot slot, Button button) {
		Button copy = button.copy();
		getContent().setButton(slot, copy);
		currentOpenInventory.setItem(slot.getSlot(), copy.getItem());
		currentOpener.updateInventory();
	}
	
	/**
	 * Updates all buttons that meet a certain criteria
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

	/**
	 * Refreshes the current state of the object.
	 * <p>
	 * This method is used to reinitialize or reset the object's state,
	 * ensuring that it is up-to-date with any changes or updates that
	 * may have occurred. Implementing this method may involve tasks
	 * such as clearing caches, reloading configuration settings, or
	 * reinitializing internal variables to their default states.
	 */
	@Override
	public void refresh() {
		initialize(menu, currentOpener);
		currentOpenedData.content().forEachItem((slot, button) ->
			currentOpenInventory.setItem(slot.getSlot(), button.getItem()));
	}

}
