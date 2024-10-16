package io.github.mqzen.menus.base.style;

import io.github.mqzen.menus.base.Content;
import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.button.Button;
import org.jetbrains.annotations.NotNull;

/**
 * The TextLayoutPane class is responsible for applying a pattern of text layout onto a content pane.
 * This class implements the Pane interface.
 */
public final class TextLayoutPane implements Pane {

	private final String[] pattern;
	private final TextLayout layout;
	private final Capacity capacity;

	public TextLayoutPane(Capacity capacity, TextLayoutProvider provider, String... pattern) {
		this.capacity = capacity;
		this.layout = provider.provide();
		this.pattern = pattern;
	}

	public TextLayoutPane(Capacity capacity,
	                      TextLayout layout, String... pattern) {
		this.capacity = capacity;
		this.pattern = pattern;
		this.layout = layout;
	}

	/**
	 * Applies the pane on the content
	 * @param content the content to apply the pane on
	 */
	@Override
	public void applyOn(@NotNull Content content) {
		if(pattern.length > capacity.getRows()) {
			throw new IllegalArgumentException(
					  String.format("Pattern-rows(%s) exceeds that of capacity(%s)", pattern.length, capacity.getRows())
			);
		}
		for (int i = 0; i < pattern.length; i++) {
			String row = pattern[i];
			if(row.length() != 9) {
				throw new IllegalArgumentException("Row #" + (i+1)+ "is not of length 9");
			}
			replaceCharsWithButtons(i, row, content);
		}

	}

	private void replaceCharsWithButtons(int rowIndex, String row, Content content) {
		final char[] chars = row.toCharArray();
		for (int column = 0; column < chars.length; column++) {
			Button button = layout.get(chars[column]);
			if(button == null)continue;
			content.setButton(rowIndex, column, button);
		}
	}
}
