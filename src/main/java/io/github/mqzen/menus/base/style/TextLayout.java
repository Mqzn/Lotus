package io.github.mqzen.menus.base.style;

import io.github.mqzen.menus.misc.button.Button;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * The TextLayout class is a customized map that links characters to Button instances.
 * It extends HashMap and implements Iterable to allow iteration over its character-button mappings.
 */
public final class TextLayout extends HashMap<Character, Button> implements Iterable<Map.Entry<Character, Button>> {

	private TextLayout() {
		super();
	}

	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Returns an iterator over elements of type {@code T}.
	 *
	 * @return an Iterator.
	 */
	@NotNull
	@Override
	public Iterator<Entry<Character, Button>> iterator() {
		return this.entrySet().iterator();
	}

	public static class Builder {

		private final TextLayout layout = new TextLayout();

		public static final char DEFAULT_CHARACTER = '#';

		public Builder set(Character character, Button button) {
			this.layout.put(character, button);
			return this;
		}
		public Builder setDefault(Button button) {
			return set(DEFAULT_CHARACTER, button);
		}

		public TextLayout build() {
			return layout;
		}
	}

}
