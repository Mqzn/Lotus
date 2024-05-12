package io.github.mqzen.menus.base.iterator;

import io.github.mqzen.menus.misc.Slot;

import java.util.function.Function;

public enum Direction {
	UPWARDS(slot -> Slot.of(slot.getRow() - 1, slot.getColumn())),
	
	DOWNWARDS(slot -> Slot.of(slot.getRow() + 1, slot.getColumn())),
	
	RIGHT(slot -> Slot.of(slot.getRow(), slot.getColumn() + 1)),
	
	LEFT(slot -> Slot.of(slot.getRow(), slot.getColumn() - 1)),
	
	RIGHT_UPWARDS(slot -> Slot.of(slot.getRow() - 1, slot.getColumn() + 1)),
	
	LEFT_UPWARDS(slot -> Slot.of(slot.getRow() - 1, slot.getColumn() - 1)),
	
	RIGHT_DOWNWARDS(slot -> Slot.of(slot.getRow() + 1, slot.getColumn() + 1)),
	
	LEFT_DOWNWARDS(slot -> Slot.of(slot.getRow() + 1, slot.getColumn() - 1));
	
	private final Function<Slot, Slot> slotModifier;
	
	Direction(Function<Slot, Slot> slotModifier) {
		this.slotModifier = slotModifier;
	}
	
	Slot modify(Slot input) {
		return slotModifier.apply(input);
	}
}
