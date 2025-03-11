package io.github.mqzen.menus.titles;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public final class MenuTitles {
	
	/**
	 * Creates a legacy menu title based on the provided string.
	 *
	 * @param title the title text to be used. It may contain legacy color codes
	 * @return a MenuTitle object that represents the legacy title.
	 */
	public static MenuTitle createLegacy(String title) {
		return new LegacyTitle(title);
	}
	
	/**
	 * Creates a modern menu title using the provided Component.
	 *
	 * @param component the Component to be used as the modern title
	 * @return an instance of ModernTitle containing the provided Component
	 */
	public static MenuTitle createModern(Component component) {
		return new ModernTitle(component);
	}
	
	/**
	 * Creates a modern MenuTitle using the specified MiniMessage API object and a MiniMessage string.
	 *
	 * @param apiObject the MiniMessage API object used for deserialization
	 * @param miniMessage the MiniMessage string to be deserialized and converted to a MenuTitle
	 * @return a MenuTitle object representing the deserialized MiniMessage string
	 */
	public static MenuTitle createModern(MiniMessage apiObject, String miniMessage) {
		return createModern(apiObject.deserialize(miniMessage));
	}
	
	/**
	 * Creates a modern MenuTitle using a mini message string.
	 *
	 * @param miniMessage the mini message string to be deserialized into a Component
	 * @return a MenuTitle object representing the deserialized Component
	 */
	public static MenuTitle createModern(String miniMessage) {
		return createModern(MiniMessage.miniMessage(), miniMessage);
	}
}
