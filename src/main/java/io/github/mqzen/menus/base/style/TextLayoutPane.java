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

	/**
	 * Holds the pattern strings that define the layout of text within the pane.
	 * Each string represents a row in the layout, and each character in the string
	 * represents a different element to be laid out.
	 * Must be initialized at construction time and is immutable thereafter.
	 */
	private final String[] pattern;
	/**
	 * Represents the layout pattern of text, mapping characters to Button instances.
	 * Used within the TextLayoutPane to format content based on a pre-defined design.
	 */
	private final TextLayout layout;
	/**
	 * The capacity configuration for the TextLayoutPane.
	 * This defines the number of rows and columns that the pane can handle.
	 */
	private final Capacity capacity;

	/**
	 * Constructs a new TextLayoutPane.
	 *
	 * @param capacity the capacity configuration of the pane, encapsulating rows and columns
	 * @param provider the provider for supplying a TextLayout mapping characters to buttons
	 * @param pattern the pattern of the text layout to be applied within the pane
	 */
	public TextLayoutPane(Capacity capacity, TextLayoutProvider provider, String... pattern) {
		this.capacity = capacity;
		this.layout = provider.provide();
		this.pattern = pattern;
	}

	/**
	 * Constructs a TextLayoutPane with a specified capacity, text layout, and pattern.
	 *
	 * @param capacity the capacity configuration for the pane
	 * @param layout the text layout that maps characters to Button instances
	 * @param pattern the pattern to be applied to the content pane
	 */
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
