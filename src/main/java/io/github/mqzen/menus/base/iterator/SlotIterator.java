package io.github.mqzen.menus.base.iterator;

import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.Slot;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

@Getter
public final class SlotIterator implements Iterator<Slot> {
	
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
	 * (In other words, returns {@code true} if {@link SlotIterator#next()} would
	 * return an element rather than throwing an exception.)
	 *
	 * @return {@code true} if the iteration has more elements
	 */
	@Override
	public boolean hasNext() {
		Slot next = current;
		return verify(next.getRow(), capacity.getRows())
				  && verify(next.getColumn(), capacity.getColumns());
	}

	private boolean verify(int value, int max) {
		return value >= 0 && value <= (max-1);
	}

	/**
	 * Returns the next element in the iteration.
	 *
	 * @return the next element in the iteration
	 * @throws NoSuchElementException if the iteration has no more elements
	 */
	@Override
	public Slot next() {
		Slot curr = current.copy();
		current = direction.modify(current);
		return curr;
	}
	
	
	public @Nullable Slot next(Predicate<Slot> slotPredicate) {
		
		boolean found = false;
		while (hasNext()) {
			if (slotPredicate.test(current)) {
				found = true;
				break;
			}
			current = direction.modify(current);
			
		}
		return found ? current : null;
	}
	
}
