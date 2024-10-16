package io.github.mqzen.menus.misc.button;

import io.github.mqzen.menus.base.MenuView;
import io.github.mqzen.menus.misc.DataRegistry;
import io.github.mqzen.menus.misc.button.actions.ButtonClickAction;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

/**
 * Represents a button that can be displayed in a menu view. A button can have an item associated with it and
 * can perform an action when clicked.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Button {
	
	private @Nullable ItemStack item;
	private @Nullable ButtonClickAction action = null;
	
	private final DataRegistry data = new DataRegistry();
	
	protected Button(@Nullable ItemStack item) {
		this.item = item;
	}
	
	//TODO add animation
	
	/**
	 * Creates a button with the provided item and no action.
	 *
	 * @param item the ItemStack associated with the button
	 * @return a new Button instance with the specified item
	 */
	public static Button empty(ItemStack item) {
		return new Button(item);
	}

	/**
	 * Creates a clickable Button with the provided ItemStack and a ButtonClickAction.
	 * This method facilitates the creation of a Button that performs a specific action when clicked in a menu view.
	 *
	 * @param item The ItemStack to be associated with the Button. It represents the visual representation of the Button in the menu.
	 * @param action The ButtonClickAction to be executed when the Button is clicked. It can be null if no action is required.
	 * @return A new Button configured with the specified ItemStack and ButtonClickAction.
	 */
	public static Button clickable(ItemStack item, @Nullable ButtonClickAction action) {
		return new Button(item, action);
	}

	/**
	 * Creates a new transformer button.
	 * The transformer button changes its behavior or properties based on user interactions.
	 *
	 * @param item the initial ItemStack represented by this button
	 * @param transformer a BiFunction that takes in a MenuView and an InventoryClickEvent, and returns a new Button
	 * @return the newly created transformer button
	 */
	public static Button transformerButton(ItemStack item, BiFunction<MenuView<?>, InventoryClickEvent, Button> transformer) {
		return new Button(item, ButtonClickAction.plain((menu, click) -> menu.replaceClickedButton(click, transformer.apply(menu, click))));
	}

	
	/**
	 * Creates a new Button instance with a specified item and a transformer function. The transformer function
	 * allows dynamic modification of the ItemStack when the button is clicked.
	 *
	 * @param item the ItemStack associated with the Button.
	 * @param transformer a BiFunction that takes a MenuView and an InventoryClickEvent and returns an
	 *                    ItemStack. This function defines the transformation to be applied to the ItemStack
	 *                    when the button is clicked.
	 * @return a new Button instance configured with the specified item and transformer function.
	 */
	public static Button transformerItem(ItemStack item,
	                                     BiFunction<MenuView<?>, InventoryClickEvent, ItemStack> transformer) {
		return new Button(item, ButtonClickAction.plain((view, click) ->
				view.replaceClickedItemStack(click, transformer.apply(view, click), false)));
	}
	
	/**
	 * Determines if the button is clickable by checking if an action is associated with it.
	 *
	 * @return {@code true} if the button has an associated action and can be clicked,
	 *         {@code false} otherwise.
	 */
	public boolean isClickable() {
		return action != null;
	}
	
	
	/**
	 * Sets the action to be performed when the button is clicked.
	 *
	 * @param action the action to be performed on button click; can be {@code null} if no action is needed
	 * @return the current instance of the Button for method chaining
	 */
	public Button setAction(@Nullable ButtonClickAction action) {
		this.action = action;
		return this;
	}
	
	/**
	 * Sets the item associated with the button.
	 *
	 * @param item the {@link ItemStack} to set; can be null to unset the item
	 * @return the current instance of {@link Button} for chaining
	 */
	public Button setItem(@Nullable ItemStack item) {
		this.item = item;
		return this;
	}
	
	/**
	 * Executes the click action associated with this button, if any.
	 *
	 * @param menu the menu view in which the button resides.
	 * @param event the inventory click event triggered when the button is clicked.
	 */
	public void executeOnClick(MenuView<?> menu, InventoryClickEvent event) {
		//if (action != null) action.execute(menu, event);
		if (action != null) action.execute(menu, event);
	}
	
	/**
	 * Creates a copy of the current Button instance. The copied Button retains the same item and action as the original.
	 *
	 * @return A new Button instance with identical item and action properties as this Button.
	 */
	public Button copy() {
		return new Button(this.item, this.action);
	}
	
	/**
	 * Sets a named piece of data associated with this button.
	 *
	 * @param name the name of the data to be set
	 * @param data the data to associate with the given name
	 * @return the current instance of the Button, allowing for chaining of method calls
	 */
	public Button setNamedData(String name, Object data) {
		this.data.setData(name, data);
		return this;
	}
	
	/**
	 * Retrieves the data associated with the specified name.
	 *
	 * @param <T> The type of the data to be returned.
	 * @param name The name of the data to retrieve.
	 * @return The data associated with the given name, or null if no data is found.
	 */
	public <T> T getNamedData(String name) {
		return data.getData(name);
	}
	
	/**
	 * Retrieves the data registry associated with this button.
	 *
	 * @return the data registry containing data tied to this button
	 */
	public DataRegistry getDataRegistry() {
		return data;
	}
	
}
