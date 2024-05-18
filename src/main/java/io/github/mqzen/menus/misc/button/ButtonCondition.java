package io.github.mqzen.menus.misc.button;

import io.github.mqzen.menus.misc.Slot;

@FunctionalInterface
public interface ButtonCondition {
	
	boolean accepts(Slot slot, Button button);
	
}
