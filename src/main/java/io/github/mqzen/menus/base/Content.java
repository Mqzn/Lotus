package io.github.mqzen.menus.base;

import io.github.mqzen.menus.base.iterator.Direction;
import io.github.mqzen.menus.base.iterator.SlotIterator;
import io.github.mqzen.menus.base.style.Pane;
import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.Slot;
import io.github.mqzen.menus.misc.Slots;
import io.github.mqzen.menus.misc.button.Button;
import io.github.mqzen.menus.misc.button.ButtonClickAction;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface Content {
	
	static Builder builder(Capacity capacity) {
		return new Builder(capacity);
	}
	
	static Content empty(Capacity capacity) {
		return Content.builder(capacity).build();
	}
	
	Capacity capacity();
	
	Optional<Button> getButton(Slot slot);
	
	default Optional<Button> getButton(int slot) {
		return getButton(Slot.of(slot));
	}
	
	default Optional<Button> getButton(int row, int column) {
		return getButton(Slot.of(row, column));
	}
	
	int nextEmptySlot();
	
	default void addButton(Button... buttons) {
		for (var button : buttons) {
			int nextSlot = nextEmptySlot();
			if (nextSlot == -1) break;
			setButton(nextSlot, button);
		}
	}
	
	void setButton(Slot slot, Button button);
	
	default void setButton(Slots slots, Button button) {
		for (Slot slot : slots.getSlots())
			setButton(slot, button);
	}
	
	default void setButton(int slot, Button button) {
		setButton(Slot.of(slot), button);
	}
	
	default void setButton(int row, int column, Button button) {
		setButton(Slot.of(row, column), button);
	}
	
	void removeButton(Slot slot);
	
	default void removeButton(int slot) {
		removeButton(Slot.of(slot));
	}
	
	default void removeButton(int row, int column) {
		removeButton(Slot.of(row, column));
	}
	
	Slots getItemSlots(ItemStack item);
	
	void fill(Button button);
	
	void fillRow(int row, Button button);
	
	void fillRow(int row, int endColumn, Button button);
	
	void fillRow(int row, Button button, List<Integer> exceptColumns);
	
	void fillRowRepeatedly(int row, Button... buttons);
	
	void fillColumn(int column, int endRow, Button button);
	
	void fillColumn(int column, Button button);
	
	void fillColumn(int column, Button button, List<Integer> exceptRows);
	
	void fillColumnRepeatedly(int column, Button... buttons);
	
	void fillBorder(Button button);
	
	void fillRectangle(Slot pos1, Slot pos2, Slot pos3, Slot pos4, Button button);
	
	void fillBorderRepeatedly(Button... buttons);
	
	void forEachItem(BiConsumer<Slot, Button> consumer);
	
	@NotNull Collection<? extends Button> getAllButtons();
	
	Content mergeWith(Content other);
	
	void updateButton(Slot slot, Consumer<Button> buttonUpdate);
	
	default int size() {
		return getAllButtons().size();
	}
	
	void trim(int maxButtonsCount);
	
	Stream<Map.Entry<Slot, Button>> stream();


	class Builder {
		private final MenuContentImpl impl;
		private final Capacity capacity;
		
		Builder(Capacity capacity) {
			this.capacity = capacity;
			impl = new MenuContentImpl(capacity);
		}
		
		public Builder setButton(Slot slot, Button button) {
			impl.setButton(slot, button);
			return this;
		}
		
		public Builder setButton(int slot, Button button) {
			impl.setButton(slot, button);
			return this;
		}
		
		public Builder setButton(int row, int column, Button button) {
			impl.setButton(row, column, button);
			return this;
		}
		
		public Builder setButton(int slot, ItemStack buttonItem, ButtonClickAction buttonClickAction) {
			impl.setButton(slot, Button.clickable(buttonItem, buttonClickAction));
			return this;
		}
		
		public Builder setButton(int slot, ItemStack buttonItem) {
			impl.setButton(slot, Button.empty(buttonItem));
			return this;
		}
		
		public Builder repeatButton(Slots slots, Button button) {
			impl.setButton(slots, button);
			return this;
		}
		
		public Builder repeatButton(int startSlot, int endSlot, Button button) {
			if (startSlot < 0 || endSlot < 0) throw new IllegalStateException("start and end slot must be positive");
			if (startSlot > endSlot) throw new IllegalStateException("Start is larger than end");
			if (endSlot >= capacity.getTotalSize())
				throw new IllegalStateException("End slot is larger than the total size of menu");
			
			for (int start = startSlot; start <= endSlot; start++) {
				impl.setButton(start, button);
			}
			return this;
		}
		
		public Builder repeatButton(Slot start, Slot endSlot, Button button) {
			return repeatButton(start.getSlot(), endSlot.getSlot(), button);
		}
		
		public Builder iterate(SlotIterator iterator, BiConsumer<Content, Slot> consumer) {
			while (iterator.hasNext()) {
				Slot slot = iterator.next();
				System.out.println("NEXT=" + slot.toString());
				consumer.accept(impl, slot);
			}
			return this;
		}
		
		public Builder iterate(Slot startSlot, Slot endSlot, Direction direction, BiConsumer<Content, Slot> consumer) {
			SlotIterator iterator = SlotIterator.create(startSlot, endSlot, capacity, direction);
			return iterate(iterator, consumer);
		}
		
		public Builder iterate(Slot startSlot, Direction direction, BiConsumer<Content, Slot> consumer) {
			SlotIterator iterator = SlotIterator.create(startSlot, capacity, direction);
			return iterate(iterator, consumer);
		}
		
		
		public Builder iterate(Direction direction, BiConsumer<Content, Slot> consumer) {
			return iterate(Slot.of(0), direction, consumer);
		}
		
		public Builder draw(Slot startSlot, Slot endSlot, Direction direction, Button button) {
			return iterate(startSlot, endSlot, direction, (content, slot) -> content.setButton(slot, button));
		}
		
		public Builder draw(Slot startSlot, Slot endSlot, Direction direction, ItemStack itemStack) {
			return draw(startSlot, endSlot, direction, Button.empty(itemStack));
		}
		
		public Builder draw(Slot startSlot, Direction direction, Button button) {
			return iterate(startSlot, direction, (content, slot) -> content.setButton(slot, button));
		}
		
		public Builder draw(Slot startSlot, Direction direction, ItemStack itemStack) {
			return iterate(startSlot, direction, (content, slot) -> content.setButton(slot, Button.empty(itemStack)));
		}
		
		
		//---------
		public Builder draw(int start, int end, Direction direction, Button button) {
			return draw(Slot.of(start), Slot.of(end), direction, button);
		}
		
		public Builder draw(int start, int end, Direction direction, ItemStack itemStack) {
			return draw(Slot.of(start), Slot.of(end), direction, Button.empty(itemStack));
		}
		
		public Builder draw(int start, Direction direction, Button button) {
			return draw(Slot.of(start), direction, button);
		}
		
		public Builder draw(int startSlot, Direction direction, ItemStack itemStack) {
			return draw(Slot.of(startSlot), direction, itemStack);
		}
		
		public Builder draw(Direction direction, Button button) {
			return iterate(direction, (content, slot) -> content.setButton(slot, button));
		}
		
		public Builder draw(Direction direction, ItemStack itemStack) {
			return iterate(direction, (content, slot) -> content.setButton(slot, Button.empty(itemStack)));
		}

		public Builder apply(Consumer<Content> contentConsumer) {
			contentConsumer.accept(impl);
			return this;
		}

		public Builder applyPane(Pane pane) {
			pane.applyOn(impl);
			return this;
		}
		
		public Content build() {
			return impl;
		}
	}
}
