package io.github.mqzen.menus.base.serialization;

import io.github.mqzen.menus.misc.DataRegistry;
import org.jetbrains.annotations.NotNull;

public interface SerializedMenuIO<F> {
	
	Class<F> fileType();
	
	void write(@NotNull DataRegistry registry, @NotNull F file);
	
	@NotNull DataRegistry read(@NotNull F file);
	
}
