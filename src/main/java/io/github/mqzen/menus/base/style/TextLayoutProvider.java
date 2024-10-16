package io.github.mqzen.menus.base.style;


import org.jetbrains.annotations.NotNull;

/**
 * Provides a method to supply a TextLayout, which maps characters to Button instances.
 */
public interface TextLayoutProvider {

	/**
	 * Provides a TextLayout object which maps characters to Button instances.
	 *
	 * @return a TextLayout containing the mapping of characters to buttons.
	 */
	@NotNull TextLayout provide();

}
