package io.github.mqzen.menus.misc;

import lombok.Getter;

/**
 * Represents a capacity configuration characterized by rows and columns.
 * The class provides multiple factory methods to create instances either from the total size or
 * by specifying rows and columns directly.
 */
@Getter
public final class Capacity {
	
	private final int totalSize;
	private final int rows, columns;
	
	private Capacity(int totalSize) {
		this.totalSize = totalSize;
		this.columns = 9;
		this.rows = totalSize / columns;
	}
	
	private Capacity(int rows, int columns) {
		this.totalSize = rows * columns;
		this.rows = rows;
		this.columns = columns;
	}
	
	/**
	 * Factory method to create a Capacity instance based on the given total size.
	 *
	 * @param size the total size to set for the capacity
	 * @return a new Capacity instance with the specified total size
	 */
	public static Capacity of(int size) {
		return new Capacity(size);
	}
	
	/**
	 * Creates an instance of Capacity based on the specified number of rows and columns.
	 *
	 * @param rows the number of rows in the Capacity
	 * @param columns the number of columns in the Capacity
	 * @return a new Capacity instance defined by the provided rows and columns
	 */
	public static Capacity of(int rows, int columns) {
		return new Capacity(rows, columns);
	}
	
	/**
	 * Creates a {@link Capacity} instance with the specified number of rows
	 * and a fixed number of columns set to 9.
	 *
	 * @param rows the number of rows to set for the capacity
	 * @return a new {@link Capacity} instance with the specified number of rows and 9 columns
	 */
	public static Capacity ofRows(int rows) {
		return new Capacity(rows, 9);
	}

	public static Capacity flexible(final int numberOfButtons, final int maxSize) {
		// Calculate required size, rounding up to the nearest multiple of 9
		int requiredSize = (numberOfButtons + 8) / 9 * 9;

		// Limit to the maximum size that is also a multiple of 9
		int limitedSize = Math.min(requiredSize, maxSize);

		// Ensure that we return a valid GUI size (must be a multiple of 9)
		return Capacity.of(limitedSize - (limitedSize % 9));
	}
}
