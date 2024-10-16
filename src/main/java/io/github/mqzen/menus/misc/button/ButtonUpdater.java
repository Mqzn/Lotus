package io.github.mqzen.menus.misc.button;

import org.jetbrains.annotations.NotNull;

/**
 * Functional interface for updating a {@link Button}.
 * This can be used to modify properties of a button in a menu view.
 */
@FunctionalInterface
public interface ButtonUpdater {
	
	/**
	 * Updates the given button by modifying its properties.
	 *
	 * @param button the button to be updated, must not be null
	 */
	void update(@NotNull Button button);
	
}
