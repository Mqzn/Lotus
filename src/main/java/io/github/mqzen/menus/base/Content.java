package io.github.mqzen.menus.base;

import io.github.mqzen.menus.base.iterator.Direction;
import io.github.mqzen.menus.base.iterator.SlotIterator;
import io.github.mqzen.menus.base.style.Pane;
import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.Slot;
import io.github.mqzen.menus.misc.Slots;
import io.github.mqzen.menus.misc.button.Button;
import io.github.mqzen.menus.misc.button.ButtonCondition;
import io.github.mqzen.menus.misc.button.actions.ButtonClickAction;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Interface representing a menu content with various utility methods
 * for managing and manipulating buttons within a given {@link Capacity}.
 */
public interface Content {
	
	/**
	 * Creates a new Builder instance with the given capacity.
	 *
	 * @param capacity the capacity of the content to be built
	 * @return a new Builder instance configured with the provided capacity
	 */
	static Builder builder(Capacity capacity) {
		return new Builder(capacity);
	}
	
	/**
	 */
	static Content empty(Capacity capacity) {
		return Content.builder(capacity).build();
	}
	
	/**
	 * Retrieves the capacity of the content.
	 *
	 * @return the capacity of the content.
	 */
	Capacity capacity();
	
	/**
	 * Retrieves the Button associated with the given Slot.
	 *
	 * @param slot the Slot for which*/
	Optional<Button> getButton(Slot slot);
	
	/**
	 * Retrieves an {@link Optional} containing a {@link Button} that satisfies the given {@link ButtonCondition}.
	 * If no such button exists, an empty {@link Optional} is returned.
	 *
	 * @param condition the {@link ButtonCondition} that the button must satisfy*/
	Optional<Button> getConditionalButton(ButtonCondition condition);
	
	/**
	 * Retrieves the Button associated with the given slot index.
	 *
	 * @param slot the index of the slot for which the Button is to be retrieved.
	 * @return an {@link Optional} containing the Button if it exists, or an empty {@link Optional} if there is no Button at the specified slot index.
	 */
	default Optional<Button> getButton(int slot) {
		return getButton(Slot.of(slot));
	}
	
	/**
	 * Retrieves the Button associated with the specified row and column in the grid.
	 *
	 * @param row the row number from which to get the button
	 * @param column the column number from which to get the button
	 * @return an Optional containing the Button if found, otherwise an empty Optional
	 */
	default Optional<Button> getButton(int row, int column) {
		return getButton(Slot.of(row, column));
	}
	
	/**
	 * Finds the next empty slot in the content starting from the specified slot index.
	 *
	 * @param start the index from which to start searching for the next empty slot
	 * @return the index of the next empty slot, or -1 if no empty slot is found*/
	int nextEmptySlot(int start);
	
	/**
	 * Adds one or more buttons to the next available empty slots in the inventory content.
	 *
	 * @param buttons the buttons to be added
	 */
	default void addButton(Button... buttons) {
		for (var button : buttons) {
			int nextSlot = nextEmptySlot(0);
			if (nextSlot == -1) break;
			setButton(nextSlot, button);
		}
	}

	/**
	 * Retrieves the map of buttons associated with their corresponding slots.
	 * @return a Map where each key is a Slot and
	 * each value is a Button present at that slot.
	 */
	Map<Slot, Button> getButtonMap();
	
	/**
	 * Places the specified button in the given slot.
	 *
	 * @param slot the slot where the button should be placed
	 * @param button the button to be placed in the specified slot
	 */
	void setButton(Slot slot, Button button);
	
	/**
	 * Sets a given button to all slots provided by the Slots object.
	 *
	 * @param slots the Slots object containing the slots where the button should be set
	 * @param button the Button to be set in the specified slots
	 */
	default void setButton(Slots slots, Button button) {
		for (Slot slot : slots.getSlots())
			setButton(slot, button);
	}
	
	/**
	 *
	 */
	default void setButton(int slot, Button button) {
		setButton(Slot.of(slot), button);
	}
	
	/**
	 *
	 */
	default void setButton(int row, int column, Button button) {
		setButton(Slot.of(row, column), button);
	}
	
	/**
	 * Removes the button located at the specified slot.
	 *
	 * @param slot the slot from which the button will be removed
	 */
	void removeButton(Slot slot);
	
	/**
	 * Removes a button from the specified slot.
	 *
	 * @param slot the index of the slot from which the button will be removed
	 */
	default void removeButton(int slot) {
		removeButton(Slot.of(slot));
	}
	
	/**
	 * Removes a button from the specified grid position identified by the given row and column.
	 *
	 * @param row The row number where the button is located to be removed.
	 * @param column The column number where the button is located to be removed.
	 */
	default void removeButton(int row, int column) {
		removeButton(Slot.of(row, column));
	}
	
	/**
	 * Retrieves the slots that contain the specified item within a certain context*/
	Slots getItemSlots(ItemStack item);
	
	/**
	 * Fills the entire content with the specified*/
	void fill(Button button);
	
	/**
	 * Fills an entire row in a grid with a specified button.
	 *
	 * @param row The row index to be filled.
	 * @param button The button to place in each column of the row.
	 */
	void fillRow(int row, Button button);
	
	/**
	 * Fills the specified row from the first column to the specified end column with the*/
	void fillRow(int row, int endColumn, Button button);
	
	/**
	 * Fills a specific row of the content with a given button except for columns specified in the exceptColumns list.
	 *
	 * @param row The row index to be filled.
	 * @param button The button to fill the row with.
	 * @param exceptColumns A list of columns in the*/
	void fillRow(int row, Button button, List<Integer> exceptColumns);
	
	/**
	 * Fills a specified row repeatedly with the given buttons.
	 * The buttons will be placed in the row in the order they are*/
	void fillRowRepeatedly(int row, Button... buttons);
	
	/**
	 * Fills a specific column with the given button up to a specified row.
	 *
	 * @param column the column to be filled
	 * @param endRow the last row index to be filled in the column
	 * @param button the button to fill the column with
	 */
	void fillColumn(int column, int endRow, Button button);
	
	/**
	 */
	void fillColumn(int column, Button button);
	
	/**
	 * Fills a specific column with the specified button, except for the rows specified in the exceptRows list.
	 *
	 * @param column The column index to fill.
	 * @param button The button to place in the column.
	 * @param exceptRows A list of row indices to*/
	void fillColumn(int column, Button button, List<Integer> exceptRows);
	
	/**
	 * F*/
	void fillColumnRepeatedly(int column, Button... buttons);
	
	/**
	 * Fills the border of the content with the*/
	void fillBorder(Button button);
	
	/**
	 * Fills a rectangular area defined by four corner slots with a specified button.
	 *
	 * @param pos1   The first corner slot of the rectangle.
	 * @param pos2   The second corner slot of the rectangle.
	 * @param*/
	void fillRectangle(Slot pos1, Slot pos2, Slot pos3, Slot pos4, Button button);
	
	/**
	 * F*/
	void fillBorderRepeatedly(Button... buttons);
	
	/**
	 *
	 */
	void forEachItem(BiConsumer<Slot, Button> consumer);
	
	/**
	 * Retrieves all the buttons contained within the content.
	 *
	 * @return A collection of all the buttons present. The returned collection is never null.
	 */
	@NotNull Collection<? extends Button> getAllButtons();
	
	/**
	 * M*/
	Content mergeWith(Content other);
	
	/**
	 */
	void updateButton(Slot slot, Consumer<Button> buttonUpdate);
	
	/**
	 */
	default int size() {
		return getAllButtons().size();
	}
	

	/**
	 * Trims the amount of buttons in the content to the specified maximum count.
	 * @param maxButtonsCount the maximum number of buttons to retain in the content
	 */
	void trim(int maxButtonsCount);
	
	/**
	 * Provides a stream of map entries representing the slots and their associated buttons.
	 *
	 * @return a stream of map entries*/
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
			while(iterator.canContinue()) {
				Slot current = iterator.current();
				consumer.accept(impl, current);
				iterator.shift();
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

		public Builder buttons(Map<Slot, Button> loadedButtons) {
			for (Slot slot : loadedButtons.keySet()) {
				setButton(slot, loadedButtons.get(slot));
			}
			return this;
		}
	}
}
