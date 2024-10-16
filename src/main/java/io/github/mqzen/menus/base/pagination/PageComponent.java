package io.github.mqzen.menus.base.pagination;

import io.github.mqzen.menus.misc.button.Button;
import io.github.mqzen.menus.misc.button.actions.ButtonClickAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a component in a paginated view.
 * This interface provides methods to transform the component to an {@link ItemStack} and handle
 * click events on this component.
 */
public interface PageComponent {
	
	/**
	 * Converts the PageComponent into an ItemStack for use in the paginated view.
	 *
	 * @return the ItemStack representation of this PageComponent
	 */
	ItemStack toItem();
	
	/**
	 * Handles the click event for a component in a paginated view.
	 *
	 * @param pageView The current view of the paginated menu that received the click.
	 * @param event    The inventory click event that was triggered.
	 */
	void onClick(PageView pageView, InventoryClickEvent event);
	
	/**
	 * Converts the current PageComponent to a clickable Button.
	 * The Button is created with the ItemStack obtained from the toItem() method
	 * and a ButtonClickAction that executes the onClick() method when the Button is clicked.
	 *
	 * @return a clickable Button created from the current PageComponent
	 */
	default Button toButton() {
		return Button.clickable(toItem(),
				ButtonClickAction.plain((menu, clickEvent) -> onClick((PageView) menu, clickEvent)));
	}
}
