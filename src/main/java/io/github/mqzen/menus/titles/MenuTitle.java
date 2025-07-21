package io.github.mqzen.menus.titles;

/**
 * The MenuTitle interface provides a contract for representing
 * menu titles in different formats, such as components or strings.
 * Classes implementing this interface can convert the title to either
 * a Component or a String.
 */
public interface MenuTitle {
	
	/**
	 * Converts the menu title to its String representation.
	 *
	 * @return the title as a String
	 */
	String asString();
	
}
