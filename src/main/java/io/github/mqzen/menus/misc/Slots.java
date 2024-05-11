package io.github.mqzen.menus.misc;

import lombok.Getter;

public final class Slots {
	
	@Getter
	private final Slot[] slots;
	
	private Slots(Slot[] slots) {
		this.slots = slots;
	}
	
	private Slots(int[] slots) {
		this.slots = new Slot[slots.length];
		for (int i = 0; i < slots.length; i++) {
			this.slots[i] = Slot.of(slots[i]);
		}
	}
	
	public static Slots of(int... slots) {
		return new Slots(slots);
	}
	
	public static Slots of(Slot... slots) {
		return new Slots(slots);
	}
	
	public static Slots ofRows(int[] rows) {
		Slot[] slots = new Slot[rows.length*9];
		for (int i = 0; i < rows.length; i++) {
			int row = rows[i];
			for (int column = 0; column < 9; column++) {
				slots[i+column] = Slot.of(row, column);
			}
		}
		return new Slots(slots);
	}
	
}
