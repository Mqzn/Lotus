package io.github.mqzen.menus.base.serialization;

import io.github.mqzen.menus.misc.DataRegistry;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for handling input and output operations for serialized menus.
 *
 * @param <F> the file type for serialization
 */
public interface SerializedMenuIO<F> {
	
	Class<F> fileType();
	
	void write(@NotNull DataRegistry registry, @NotNull F file);
	
	@NotNull DataRegistry read(@NotNull F file);

}
