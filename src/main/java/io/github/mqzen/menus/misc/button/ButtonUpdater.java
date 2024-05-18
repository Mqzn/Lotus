package io.github.mqzen.menus.misc.button;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ButtonUpdater {
	
	void update(@NotNull Button button);
	
}
