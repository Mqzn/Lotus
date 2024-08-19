package io.github.mqzen.menus.base;

import com.google.common.collect.Lists;
import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.Slot;
import io.github.mqzen.menus.misc.Slots;
import io.github.mqzen.menus.misc.button.Button;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class MenuContentImpl implements Content {
	
	private final Capacity capacity;
	private final ConcurrentHashMap<Slot, Button> map = new ConcurrentHashMap<>();
	
	public MenuContentImpl(Capacity capacity) {
		this.capacity = capacity;
	}
	
	@Override
	public Capacity capacity() {
		return capacity;
	}
	
	@Override
	public Optional<Button> getButton(Slot slot) {
		return Optional.ofNullable(map.get(slot));
	}
	
	@Override
	public int nextEmptySlot() {
		for (int slot = 0; slot < capacity.getTotalSize(); slot++) {
			if (getButton(slot).isEmpty()) return slot;
		}
		
		return -1;
	}

	@Override
	public ConcurrentHashMap<Slot, Button> getButtonMap() {
		return map;
	}

	@Override
	public void setButton(Slot slot, Button item) {
		map.put(slot, item);
	}
	
	@Override
	public void removeButton(Slot slot) {
		map.remove(slot);
	}
	
	@Override
	public Slots getItemSlots(ItemStack item) {
		List<Slot> slots = Lists.newArrayList();
		for (var buttonEntry : map.entrySet()) {
			Slot slot = buttonEntry.getKey();
			Button button = buttonEntry.getValue();
			
			if (item.isSimilar(button.getItem())) {
				slots.add(slot);
			}
		}
		
		return Slots.of(slots.toArray(new Slot[0]));
	}
	
	@Override
	public void fill(Button button) {
		for (int slot = 0; slot < capacity.getTotalSize(); slot++)
			setButton(slot, button);
	}
	
	@Override
	public void fillRow(int row, Button button) {
		fillRow(row, capacity.getColumns()-1, button);
	}
	
	@Override
	public void fillRow(int row, int endColumn, Button button) {
		for (int column = 0; column <= endColumn; column++)
			setButton(row, column, button);
	}
	
	@Override
	public void fillRow(final int row, Button button, List<Integer> exceptColumns) {
		for (int column = 0; column < capacity.getColumns(); column++) {
			if (exceptColumns.contains(column)) continue;
			setButton(row, column, button);
		}
	}
	
	@Override
	public void fillRowRepeatedly(final int row, Button... buttons) {
		if (buttons.length > capacity.getColumns())
			throw new IllegalStateException("Couldn't repeat " + buttons.length +
				" buttons as it's greater than the column slots (" + capacity.getColumns() + ")");
		
		int column = 0;
		int buttonIndex = 0;
		
		while (column < capacity.getColumns()) {
			
			if (buttonIndex >= buttons.length)
				//repeating
				buttonIndex = 0;
			
			setButton(row, column, buttons[buttonIndex]);
			
			column++;
			buttonIndex++;
		}
		
	}
	
	@Override
	public void fillColumn(final int column, final int endRow, Button button) {
		for (int row = 0; row <= endRow; row++)
			setButton(row, column, button);
	}
	
	@Override
	public void fillColumn(final int column, Button button) {
		fillColumn(column, capacity.getRows()-1, button);
	}
	
	@Override
	public void fillColumn(int column, Button button, List<Integer> exceptRows) {
		for (int row = 0; row < capacity.getRows(); row++) {
			if (exceptRows.contains(row)) continue;
			setButton(row, column, button);
		}
	}
	
	@Override
	public void fillColumnRepeatedly(final int column, Button... buttons) {
		if (buttons.length > capacity.getRows())
			throw new IllegalStateException("Couldn't repeat " + buttons.length + " buttons as it's greater than the row slots (" + capacity.getRows() + ")");
		
		int row = 0;
		int buttonIndex = 0;
		while (row < capacity.getRows()) {
			
			if (buttonIndex >= buttons.length)
				//repeating
				buttonIndex = 0;
			
			setButton(row, column, buttons[buttonIndex]);
			
			row++;
			buttonIndex++;
		}
	}
	
	@Override
	public void fillBorder(Button button) {
		fillRow(0, button);
		fillColumn(0, button);
		fillRow(capacity.getRows() - 1, button);
		fillColumn(capacity.getColumns() - 1, button);
	}
	
	@Override
	public void fillRectangle(Slot pos1, Slot pos2, Slot pos3, Slot pos4, Button button) {
		fillRow(pos1.getRow(), pos2.getColumn(), button);
		fillColumn(pos1.getColumn(), pos3.getRow(), button);
		
		fillRow(pos3.getRow(), pos4.getColumn(), button);
		fillColumn(pos2.getColumn(), pos4.getRow(), button);
	}
	
	@Override
	public void fillBorderRepeatedly(Button... buttons) {
		fillRowRepeatedly(0, buttons);
		fillColumnRepeatedly(0, buttons);
		fillRowRepeatedly(capacity.getRows() - 1, buttons);
		fillColumnRepeatedly(capacity.getColumns() - 1, buttons);
	}
	
	@Override
	public void forEachItem(final BiConsumer<Slot, Button> consumer) {
		map.forEach(consumer);
	}
	
	@Override
	public @NotNull Collection<? extends Button> getAllButtons() {
		return map.values();
	}
	
	@Override
	public Content mergeWith(Content other) {
		map.putAll(((MenuContentImpl) other).map);
		return this;
	}
	
	@Override
	public void updateButton(Slot slot, Consumer<Button> buttonUpdate) {
		map.compute(slot, (s, oldButton) -> {
			buttonUpdate.accept(oldButton);
			return oldButton;
		});
	}
	
	@Override
	public void trim(int maxButtonsCount) {
		LinkedList<Slot> slots = new LinkedList<>(map.keySet());
		for (int i = 0; i < maxButtonsCount; i++) {
			Slot removed = slots.removeLast();
			map.remove(removed);
		}
	}
	
	@Override
	public Stream<Map.Entry<Slot, Button>> stream() {
		return map.entrySet().stream();
	}


}