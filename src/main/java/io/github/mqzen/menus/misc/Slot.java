package io.github.mqzen.menus.misc;

import com.google.common.base.Objects;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;


/**
 * Represents a Slot in a grid-like structure with a fixed width.
 * <p>
 * This class provides methods to create and manipulate slots using their slot numbers or
 * their row and column coordinates.
 * </p>
 */
@Getter
public final class Slot implements Comparable<Slot> {
	
	private final static int WIDTH = 9;
	
	private final int slot, row, column;
	
	private Slot(int slot) {
		this.slot = slot;
		this.row = (int) Math.floor((float) (slot / WIDTH));
		this.column = slot % WIDTH;
	}
	
	private Slot(int row, int column) {
		this.slot = row * WIDTH + column;
		this.row = row;
		this.column = column;
	}
	
	public static Slot of(int slot) {
		return new Slot(slot);
	}
	
	public static Slot of(int row, int column) {
		return new Slot(row, column);
	}
	
	public static Slot last(Capacity capacity) {
		return new Slot(capacity.getTotalSize() - 1);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Slot slot1)) return false;
		return slot == slot1.slot;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(slot);
	}
	
	public Slot subtractBy(int num) {
		return Slot.of(slot - num);
	}
	
	@Override
	public int compareTo(@NotNull Slot o) {
		return this.slot - o.slot;
	}

	public Slot copy() {
		return new Slot(slot);
	}

	@Override
	public String toString() {
		return "Slot{" +
				  "slot=" + slot +
				  ", row=" + row +
				  ", column=" + column +
				  '}';
	}
}
