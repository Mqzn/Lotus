package io.github.mqzen.menus.misc;

import lombok.Getter;

@Getter
public final class Capacity {
	
	private final int totalSize;
	private final int rows, columns;
	
	private Capacity(int totalSize) {
		this.totalSize = totalSize;
		this.columns = 9;
		this.rows = totalSize/columns;
	}
	
	private Capacity(int rows, int columns) {
		this.totalSize = rows*columns;
		this.rows = rows;
		this.columns = columns;
	}
	
	public static Capacity of(int size) {
		return new Capacity(size);
	}
	
	public static Capacity of(int rows, int columns) {
		return new Capacity(rows, columns);
	}
	
	public static Capacity ofRows(int rows) {
		return new Capacity(rows, 9);
	}
}
