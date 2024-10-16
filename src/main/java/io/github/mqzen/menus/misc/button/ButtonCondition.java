package io.github.mqzen.menus.misc.button;

import io.github.mqzen.menus.misc.Slot;

/**
 * A functional interface used to define whether a given {@link Button} in a specific {@link Slot}
 * satisfies certain conditions. Implementations of this interface provide the logic to determine
 * if a {@code Slot} and {@code Button} combination meets the desired criteria.
 */
@FunctionalInterface
public interface ButtonCondition {
	
	/**
	 * Determines whether a given {@link Button} in a specific {@link Slot} satisfies certain conditions.
	 *
	 * @param slot the slot in which the button is located
	 * @param button the button to be evaluated
	 * @return {@code true} if the button in the specified slot meets the conditions, otherwise {@code false}
	 */
	boolean accepts(Slot slot, Button button);
	
}
