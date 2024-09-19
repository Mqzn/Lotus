package io.github.mqzen.menus.base.iterator;

import io.github.mqzen.menus.misc.Slot;

import java.util.function.Function;

public enum Direction {
	UPWARDS(slot -> Slot.of(slot.getRow() - 1, slot.getColumn())) {
		@Override
		boolean isOutsideBoundarySlot(Slot slot, Slot boundary) {
			return slot.getSlot() < boundary.getSlot();
		}
	},
	
	DOWNWARDS(slot -> Slot.of(slot.getRow() + 1, slot.getColumn())) {
		@Override
		boolean isOutsideBoundarySlot(Slot slot, Slot boundary) {
			return slot.getSlot() > boundary.getSlot();
		}
	},
	
	RIGHT(slot -> Slot.of(slot.getRow(), slot.getColumn() + 1)) {
		@Override
		boolean isOutsideBoundarySlot(Slot slot, Slot boundary) {
			return slot.getSlot() > boundary.getSlot();
		}
	},
	
	LEFT(slot -> Slot.of(slot.getRow(), slot.getColumn() - 1)) {
		@Override
		boolean isOutsideBoundarySlot(Slot slot, Slot boundary) {
			return slot.getSlot() < boundary.getSlot();
		}
	},
	
	RIGHT_UPWARDS(slot -> Slot.of(slot.getRow() - 1, slot.getColumn() + 1)) {
		@Override
		boolean isOutsideBoundarySlot(Slot slot, Slot boundary) {
			return slot.getSlot() < boundary.getSlot();
		}
	},
	
	LEFT_UPWARDS(slot -> Slot.of(slot.getRow() - 1, slot.getColumn() - 1)) {
		@Override
		boolean isOutsideBoundarySlot(Slot slot, Slot boundary) {
			return slot.getSlot() < boundary.getSlot();
		}
	},
	
	RIGHT_DOWNWARDS(slot -> Slot.of(slot.getRow() + 1, slot.getColumn() + 1)) {
		@Override
		boolean isOutsideBoundarySlot(Slot slot, Slot boundary) {
			return slot.getSlot() > boundary.getSlot();
		}
	},
	
	LEFT_DOWNWARDS(slot -> Slot.of(slot.getRow() + 1, slot.getColumn() - 1)) {
		@Override
		boolean isOutsideBoundarySlot(Slot slot, Slot boundary) {
			return slot.getSlot() > boundary.getSlot();
		}
	};
	
	private final Function<Slot, Slot> slotModifier;
	
	Direction(Function<Slot, Slot> slotModifier) {
		this.slotModifier = slotModifier;
	}
	
	Slot modify(Slot input) {
		return slotModifier.apply(input);
	}
	
	abstract boolean isOutsideBoundarySlot(Slot slot, Slot boundary);
}
