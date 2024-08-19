package io.github.mqzen.menus.misc.button.actions;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class ButtonActionRegistry {

	private static ButtonActionRegistry registry;
	
	public static ButtonActionRegistry getInstance() {
		if(registry == null) {
			registry = new ButtonActionRegistry();
		}
		return registry;
	}
	
	private final Map<String, ButtonClickAction> actions = new HashMap<>();
	private ButtonActionRegistry() {
		registerAction(ButtonClickActions.CLOSE_INVENTORY);
	}
	
	public void registerAction(ButtonClickAction action) {
		actions.put(action.tag(), action);
	}
	
	public @Nullable ButtonClickAction getAction(String tag) {
		return actions.get(tag);
	}
	
}
