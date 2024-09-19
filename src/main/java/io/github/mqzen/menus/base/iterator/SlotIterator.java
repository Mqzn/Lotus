package io.github.mqzen.menus.base.iterator;

import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.Slot;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

@Getter
public final class SlotIterator {
	
	private final Capacity capacity;
	private final Direction direction;
	private final Slot endSlot;
	private @NotNull Slot current;
	
	SlotIterator(@NotNull Slot startingSlot, Slot endSlot, Capacity capacity, Direction direction) {
		this.current = startingSlot;
		this.endSlot = endSlot;
		this.capacity = capacity;
		this.direction = direction;
	}
	
	SlotIterator(Capacity capacity, Direction direction) {
		this.current = Slot.of(0);
		this.endSlot = Slot.of(capacity.getTotalSize() - 1);
		this.capacity = capacity;
		this.direction = direction;
	}
	
	public static SlotIterator create(Capacity capacity, Direction direction) {
		return new SlotIterator(capacity, direction);
	}
	
	public static SlotIterator create(Slot start, Capacity capacity, Direction direction) {
		return new SlotIterator(start, Slot.of(capacity.getTotalSize() - 1), capacity, direction);
	}
	
	public static SlotIterator create(Slot start, Slot end, Capacity capacity, Direction direction) {
		return new SlotIterator(start, end, capacity, direction);
	}
	
	/**
	 * Returns {@code true} if the iteration has more elements.
	 * (In other words, returns {@code true} if {@link SlotIterator#current()} would
	 * return an element rather than throwing an exception.)
	 *
	 * @return {@code true} if the iteration has more elements
	 */
	public boolean canContinue() {
		return current.getSlot() >= 0 && !direction.isOutsideBoundarySlot(current, endSlot)
			&& verify(current.getRow(), capacity.getRows())
			&& verify(current.getColumn(), capacity.getColumns());
	}

	private boolean verify(int value, int max) {
		return value >= 0 && value < max;
	}

	/**
	 * Returns the current element in the iteration.
	 *
	 * @return the current element in the iteration
	 * @throws NoSuchElementException if the iteration has no more elements
	 */
	public Slot current() {
		if(!this.canContinue()) {
			throw new NoSuchElementException();
		}
		return current.copy();
	}
	
	/**
	 * Shifts the current slot to the next target slot
	 * depending on the {@link Direction} of this iterator
	 */
	public void shift() {
		current = direction.modify(current);
	}
	
}
