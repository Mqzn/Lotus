package io.github.mqzen.menus.base.iterator;

import io.github.mqzen.menus.misc.Slot;

import java.util.function.Function;

/**
 * Enum representing possible movement directions on a grid.
 * Each direction is associated with a function that modifies a given Slot
 * (a position in the grid) to step in that direction.
 */
public enum Direction {
	/**
	 * Represents the upward movement direction on a grid. This direction is
	 * associated with a function that modifies a given {@code Slot} to step upwards.
	 * The upward movement subtracts 1 from the row of the slot.
	 */
	UPWARDS(slot -> Slot.of(slot.getRow() - 1, slot.getColumn())) {
		@Override
		boolean isOutsideBoundarySlot(Slot slot, Slot boundary) {
			return slot.getSlot() < boundary.getSlot();
		}
	},
	
	/**
	 * Represents the downwards direction in a grid.
	 * The associated function advances a given slot to the slot directly below it.
	 * This direction is used to move from a higher row to a lower row in the same column.
	 * <p>
	 * The `isOutsideBoundarySlot` method checks if the slot is beyond the boundary when moving downwards.
	 * </p>
	 * @see Slot
	 */
	DOWNWARDS(slot -> Slot.of(slot.getRow() + 1, slot.getColumn())) {
		@Override
		boolean isOutsideBoundarySlot(Slot slot, Slot boundary) {
			return slot.getSlot() > boundary.getSlot();
		}
	},
	
	/**
	 * Represents the direction of movement to the right on a grid.
	 * This direction increments the column value of a given slot.
	 *
	 * @see Slot#of(int, int)
	 */
	RIGHT(slot -> Slot.of(slot.getRow(), slot.getColumn() + 1)) {
		@Override
		boolean isOutsideBoundarySlot(Slot slot, Slot boundary) {
			return slot.getSlot() > boundary.getSlot();
		}
	},
	
	/**
	 * Moves the Slot one unit to the left by decrementing the column value by 1.
	 * Overrides the isOutsideBoundarySlot method to check if the Slot's current position
	 * is outside the boundary by comparing the Slot's value to the boundary Slot's value.
	 */
	LEFT(slot -> Slot.of(slot.getRow(), slot.getColumn() - 1)) {
		@Override
		boolean isOutsideBoundarySlot(Slot slot, Slot boundary) {
			return slot.getSlot() < boundary.getSlot();
		}
	},
	
	/**
	 * Enum constant representing the right-upwards direction on a grid.
	 * This direction is associated with a function that modifies a given Slot
	 * to move one step up and one step to the right.
	 *
     */
	RIGHT_UPWARDS(slot -> Slot.of(slot.getRow() - 1, slot.getColumn() + 1)) {
		@Override
		boolean isOutsideBoundarySlot(Slot slot, Slot boundary) {
			return slot.getSlot() < boundary.getSlot();
		}
	},
	
	/**
	 * Represents the LEFT_UPWARDS direction in a grid. When applied,
	 * it modifies a given Slot by decreasing both its row and column values by 1 unit.
	 * An example of the transformation would be converting a Slot with coordinates (i, j)
	 * to another Slot with (i-1, j-1).
	 * Additionally, it provides a method to determine whether a slot is outside the
	 * boundary in this direction by comparing the slot's position with a boundary slot.
	 *
     */
	LEFT_UPWARDS(slot -> Slot.of(slot.getRow() - 1, slot.getColumn() - 1)) {
		@Override
		boolean isOutsideBoundarySlot(Slot slot, Slot boundary) {
			return slot.getSlot() < boundary.getSlot();
		}
	},
	
	/**
	 * Represents the movement direction "right downwards" on a grid.
	 * When applied, it moves the given Slot one row down and one column to the right.
	 * <p>
	 * The movement is encapsulated in a functional mapping from a Slot
	 * to a new Slot with modified row and column indices.
	 * </p>
     */
	RIGHT_DOWNWARDS(slot -> Slot.of(slot.getRow() + 1, slot.getColumn() + 1)) {
		@Override
		boolean isOutsideBoundarySlot(Slot slot, Slot boundary) {
			return slot.getSlot() > boundary.getSlot();
		}
	},
	
	/**
	 * Represents the direction of movement in a grid, specifically moving towards the left and downwards.
	 * This direction changes the position of a given {@link Slot} by incrementing its row and decrementing its column.
	 *
     */
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
	
	/**
	 * Checks if the given slot is outside the boundary slot.
	 *
	 * @param slot the slot to check
	 * @param boundary the boundary against which the slot is checked
	 * @return true if the slot is outside the boundary, false otherwise
	 */
	abstract boolean isOutsideBoundarySlot(Slot slot, Slot boundary);
}
