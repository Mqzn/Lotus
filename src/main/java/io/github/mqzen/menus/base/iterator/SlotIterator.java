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
	private @NotNull Slot current;
	private final Slot endSlot;
	
	SlotIterator(@NotNull Slot startingSlot, Slot endSlot, Capacity capacity, Direction direction) {
		this.current = startingSlot;
		this.endSlot = endSlot;
		this.capacity = capacity;
		this.direction = direction;
	}

	SlotIterator(Capacity capacity, Direction direction) {
		this.current = Slot.of(0);
		this.endSlot = Slot.of(capacity.getTotalSize()-1);
		this.capacity = capacity;
		this.direction = direction;
	}
	
	public static SlotIterator create(Capacity capacity, Direction direction) {
		return new SlotIterator(capacity, direction);
	}
	
	public static SlotIterator create(Slot start, Capacity capacity, Direction direction) {
		return new SlotIterator(start, Slot.of(capacity.getTotalSize()-1), capacity, direction);
	}
	
	public static SlotIterator create(Slot start, Slot end, Capacity capacity, Direction direction) {
		return new SlotIterator(start, end, capacity, direction);
	}
	
	/**
	 * Returns {@code true} if the iteration has more elements.
	 * (In other words, returns {@code true} if {@link #next} would
	 * return an element rather than throwing an exception.)
	 *
	 * @return {@code true} if the iteration has more elements
	 */
	@Override
	public boolean hasNext() {
		return current.getSlot() >= 0 && current.getSlot() <= endSlot.getSlot();
	}
	
	/**
	 * Returns the next element in the iteration.
	 *
	 * @return the next element in the iteration
	 * @throws NoSuchElementException if the iteration has no more elements
	 */
	@Override
	public Slot next() {
		current = direction.modify(current);
		return current;
	}
	
	
	public @Nullable Slot next(Predicate<Slot> slotPredicate) {
		
		boolean found = false;
		while (hasNext()) {
			if(slotPredicate.test(current)) {
				found = true;
				break;
			}
			current = direction.modify(current);
			
		}
		return found ? current : null;
	}
	
}
