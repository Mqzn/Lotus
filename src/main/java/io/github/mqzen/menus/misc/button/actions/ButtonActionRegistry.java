package io.github.mqzen.menus.misc.button.actions;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * The ButtonActionRegistry class manages the registration and retrieval of button click actions.
 * This singleton class ensures that only one instance of the registry exists throughout the application.
 */
public final class ButtonActionRegistry {

	private static ButtonActionRegistry registry;
	
	/**
	 * Returns the singleton instance of the ButtonActionRegistry class.
	 *
	 * @return The singleton instance of ButtonActionRegistry.
	 */
	public static ButtonActionRegistry getInstance() {
		if(registry == null) {
			registry = new ButtonActionRegistry();
		}
		return registry;
	}
	
	private final Map<String, ButtonClickAction> actions = new HashMap<>();
	private ButtonActionRegistry() {
		registerAction(ButtonClickActions.CLOSE_MENU);
	}
	
	/**
	 * Registers a new button click action.
	 *
	 * @param action The ButtonClickAction to register. The action is identified by its unique tag.
	 */
	public void registerAction(ButtonClickAction action) {
		actions.put(action.tag(), action);
	}
	
	/**
	 * Retrieves the ButtonClickAction associated with the specified tag.
	 * If no action is found with the given tag, this method returns null.
	 *
	 * @param tag The tag identifying the ButtonClickAction to retrieve.
	 * @return The ButtonClickAction associated with the specified tag, or null if no matching action is found.
	 */
	public @Nullable ButtonClickAction getAction(String tag) {
		return actions.get(tag);
	}
	
}
