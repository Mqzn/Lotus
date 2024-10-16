package io.github.mqzen.menus.titles;

import net.kyori.adventure.text.Component;

/**
 * The MenuTitle interface provides a contract for representing
 * menu titles in different formats, such as components or strings.
 * Classes implementing this interface can convert the title to either
 * a Component or a String.
 */
public sealed interface MenuTitle permits LegacyTitle, ModernTitle {
	
	/**
	 * Converts the menu title to a Component representation.
	 *
	 * @return the title as a Component object
	 */
	Component asComponent();
	
	/**
	 * Converts the menu title to its String representation.
	 *
	 * @return the title as a String
	 */
	String asString();
	
}
