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
		for (Button button : buttons) {
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
	 * Sets the specified button at the given slot index.
	 *
	 * @param slot the index of the slot where the button should be placed
	 * @param button the button to be placed in the specified slot
	 */
	default void setButton(int slot, Button button) {
		setButton(Slot.of(slot), button);
	}
	
	/**
	 * Sets a button at the specified row and column in the grid.
	 *
	 * @param row the row number where the button should be placed
	 * @param column the column number where the button should be placed
	 * @param button the button to be placed at the specified row and column
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
	 * Fills a specific column repeatedly with the given buttons.
	 * The buttons will be placed in the column in the order they are received,
	 * cycling through the buttons if there are more rows than buttons.
	 *
	 * @param column the column index to fill.
	 * @param buttons the buttons to place in the column.
	 */
	void fillColumnRepeatedly(int column, Button... buttons);
	
	/**
	 * Fills the border of the content with the*/
	void fillBorder(Button button);
	
	/**
	 * Fills a rectangular area defined by four corner slots with a specified button.
	 *
	 * @param pos1 the first corner slot of the rectangle
	 * @param pos2 the second corner slot of the rectangle
	 * @param pos3 the third corner slot of the rectangle
	 * @param pos4 the fourth corner slot of the rectangle
	 * @param button the button to fill the rectangle with
	 */
	void fillRectangle(Slot pos1, Slot pos2, Slot pos3, Slot pos4, Button button);
	
	/**
	 * Fills the border of the content repeatedly with the given buttons.
	 * The buttons will be placed in the border positions, cycling through
	 * the buttons if more border positions are available than the number
	 * of buttons provided.
	 *
	 * @param buttons the buttons to be used for filling the border repeatedly
	 */
	void fillBorderRepeatedly(Button... buttons);
	
	/**
	 * Applies the given {@link BiConsumer} to each item in the content.
	 *
	 * @param consumer the {@link BiConsumer} to be applied to each {@link Slot} and {@link Button} pair
	 */
	void forEachItem(BiConsumer<Slot, Button> consumer);
	
	/**
	 * Retrieves all the buttons contained within the content.
	 *
	 * @return A collection of all the buttons present. The returned collection is never null.
	 */
	@NotNull Collection<? extends Button> getAllButtons();
	
	/**
	 * Merges this content instance with the specified other content instance.
	 *
	 * @param other the other Content instance to merge with this instance
	 * @return a new Content instance that is the result of merging this instance with the specified other instance
	 */
	Content mergeWith(Content other);
	
	/**
	 * Updates the button at the specified slot using the provided update operation.
	 *
	 * @param slot the slot where the button to be updated is located
	 * @param buttonUpdate the operation to be applied to the button
	 */
	void updateButton(Slot slot, Consumer<Button> buttonUpdate);
	
	/**
	 * Returns the number of buttons contained within the content.
	 *
	 * @return the total count of buttons.
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
		
		/**
		 * Sets a button at the specified slot.
		 *
		 * @param slot the slot where the button will be placed
		 * @param button the button to be set at the slot
		 * @return the current Builder instance for method chaining
		 */
		public Builder setButton(Slot slot, Button button) {
			impl.setButton(slot, button);
			return this;
		}
		
		/**
		 * Sets a button at the specified slot.
		 *
		 * @param slot the slot index where the button will be placed
		 * @param button the Button instance to set at the specified slot
		 * @return the Builder instance for method chaining
		 */
		public Builder setButton(int slot, Button button) {
			impl.setButton(slot, button);
			return this;
		}
		
		/**
		 * Sets a button at the specified row and column in the builder's internal structure.
		 *
		 * @param row The row index where the button should be placed.
		 * @param column The column index where the button should be placed.
		 * @param button The button to be placed at the specified row and column.
		 * @return The current Builder instance for chaining.
		 */
		public Builder setButton(int row, int column, Button button) {
			impl.setButton(row, column, button);
			return this;
		}
		
		/**
		 * Sets a button in the specified slot with the given item and click action.
		 *
		 * @param slot the slot number where the button will be placed
		 * @param buttonItem the ItemStack to be used as the button
		 * @param buttonClickAction the action to be executed when the button is clicked
		 * @return the Builder instance for method chaining
		 */
		public Builder setButton(int slot, ItemStack buttonItem, ButtonClickAction buttonClickAction) {
			impl.setButton(slot, Button.clickable(buttonItem, buttonClickAction));
			return this;
		}
		
		/**
		 * Sets the button item in the specified slot.
		 *
		 * @param slot the slot index where the item should be placed
		 * @param buttonItem the ItemStack to be set as the button item
		 * @return the Builder instance for method chaining
		 */
		public Builder setButton(int slot, ItemStack buttonItem) {
			impl.setButton(slot, Button.empty(buttonItem));
			return this;
		}
		
		/**
		 * Assigns the specified button to the given set of slots repeatedly.
		 *
		 * @param slots the slots where the button will be repeated
		 * @param button the button instance to be assigned
		 * @return the builder instance with updated button assignments
		 */
		public Builder repeatButton(Slots slots, Button button) {
			impl.setButton(slots, button);
			return this;
		}
		
		/**
		 * Repeats the given button in the specified slot range (inclusive).
		 *
		 * @param startSlot the starting slot index, must be non-negative and not greater than endSlot
		 * @param endSlot the ending slot index, must be non-negative and not less than startSlot
		 * @param button the Button to be set in the specified slot range
		 * @return the current instance of Builder for chaining
		 * @throws IllegalStateException if startSlot or endSlot is negative, if startSlot is greater than endSlot,
		 *                               or if endSlot exceeds the total size of the menu
		 */
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
		
		/**
		 * Repeats the given button between the specified start and end slots.
		 *
		 * @param start the start slot
		 * @param endSlot the end slot
		 * @param button the button to be repeated
		 * @return the current Builder instance
		 */
		public Builder repeatButton(Slot start, Slot endSlot, Button button) {
			return repeatButton(start.getSlot(), endSlot.getSlot(), button);
		}
		
		/**
		 * Iterates through slots in the inventory based on the provided iterator
		 * and applies the given consumer to each slot.
		 *
		 * @param iterator the iterator that determines the sequence of slots to be iterated over
		 * @param consumer the operation to be performed on each slot
		 * @return the builder instance with the applied operations
		 */
		public Builder iterate(SlotIterator iterator, BiConsumer<Content, Slot> consumer) {
			while(iterator.canContinue()) {
				Slot current = iterator.current();
				consumer.accept(impl, current);
				iterator.shift();
			}
			return this;
		}
		
		/**
		 * Iterates through a series of slots from a starting slot to an ending slot in the specified direction,
		 * applying the given consumer to each content-slot pair encountered during the traversal.
		 *
		 * @param startSlot   the initial slot to start iterating from.
		 * @param endSlot     the final slot to end the iteration at.
		 * @param direction   the direction in which to iterate through the slots.
		 * @param consumer    a BiConsumer to process the content and the current slot during the iteration.
		 * @return the Builder instance for method chaining.
		 */
		public Builder iterate(Slot startSlot, Slot endSlot, Direction direction, BiConsumer<Content, Slot> consumer) {
			SlotIterator iterator = SlotIterator.create(startSlot, endSlot, capacity, direction);
			return iterate(iterator, consumer);
		}
		
		/**
		 * Iterates over slots starting from a given slot in a specific direction using a provided consumer to perform actions on each slot.
		 *
		 * @param startSlot the starting slot for the iteration
		 * @param direction the direction in which to iterate over the slots
		 * @param consumer the consumer that performs actions on the content and slot during iteration
		 * @return the builder instance with the applied iteration
		 */
		public Builder iterate(Slot startSlot, Direction direction, BiConsumer<Content, Slot> consumer) {
			SlotIterator iterator = SlotIterator.create(startSlot, capacity, direction);
			return iterate(iterator, consumer);
		}
		
		
		/**
		 * Iterates over the slots in a given direction, starting from the initial slot,
		 * applying the provided consumer action to each slot's content.
		 *
		 * @param direction The direction to iterate over the slots.
		 * @param consumer The action to be performed on each slot's content.
		 * @return The builder instance with the applied changes.
		 */
		public Builder iterate(Direction direction, BiConsumer<Content, Slot> consumer) {
			return iterate(Slot.of(0), direction, consumer);
		}
		
		/**
		 * Draws a button between the specified start and end slots following a given direction.
		 *
		 * @param startSlot the starting slot of the drawing operation
		 * @param endSlot the ending slot of the drawing operation
		 * @param direction the direction in which to draw the button
		 * @param button the button to be drawn
		 * @return the original Builder instance with the button drawn as specified
		 */
		public Builder draw(Slot startSlot, Slot endSlot, Direction direction, Button button) {
			return iterate(startSlot, endSlot, direction, (content, slot) -> content.setButton(slot, button));
		}
		
		/**
		 * Draws contents from the startSlot to the endSlot in the indicated direction with the given ItemStack.
		 *
		 * @param startSlot the starting slot where the drawing begins
		 * @param endSlot the ending slot where the drawing stops
		 * @param direction the direction in which to draw
		 * @param itemStack the item stack to be placed in the slots
		 * @return the builder instance with the drawn slots configured
		 */
		public Builder draw(Slot startSlot, Slot endSlot, Direction direction, ItemStack itemStack) {
			return draw(startSlot, endSlot, direction, Button.empty(itemStack));
		}
		
		/**
		 * Draws a button in the specified direction starting from the given slot.
		 *
		 * @param startSlot the starting slot for the button to be drawn
		 * @param direction the direction in which the button will be drawn
		 * @param button the button to be drawn
		 * @return the Builder instance for method chaining
		 */
		public Builder draw(Slot startSlot, Direction direction, Button button) {
			return iterate(startSlot, direction, (content, slot) -> content.setButton(slot, button));
		}
		
		/**
		 * Draws an ItemStack in a specified direction starting from a given slot.
		 *
		 * @param startSlot   the slot to start drawing from
		 * @param direction   the direction in which to draw
		 * @param itemStack   the item stack to be drawn
		 * @return the builder instance with the drawn item stack
		 */
		public Builder draw(Slot startSlot, Direction direction, ItemStack itemStack) {
			return iterate(startSlot, direction, (content, slot) -> content.setButton(slot, Button.empty(itemStack)));
		}
		
		
		/**
		 * Draws a path of buttons between the specified start and end slots in a given direction.
		 *
		 * @param start The starting slot index
		 * @param end The ending slot index
		 * @param direction The direction in which to draw the buttons
		 * @param button The button to be drawn
		 * @return The current Builder instance with the updated button path
		 */
		//---------
		public Builder draw(int start, int end, Direction direction, Button button) {
			return draw(Slot.of(start), Slot.of(end), direction, button);
		}
		
		/**
		 * Draws a sequence of ItemStacks from a starting slot to an ending slot in a given direction.
		 *
		 * @param start The starting slot index.
		 * @param end The ending slot index.
		 * @param direction The direction in which to draw the ItemStacks.
		 * @param itemStack The ItemStack to be used in each slot.
		 * @return The Builder instance for method chaining.
		 */
		public Builder draw(int start, int end, Direction direction, ItemStack itemStack) {
			return draw(Slot.of(start), Slot.of(end), direction, Button.empty(itemStack));
		}
		
		/**
		 * Draws a button starting from a specified slot in a given direction.
		 *
		 * @param start the starting slot number
		 * @param direction the direction in which to draw the button
		 * @param button the Button to draw
		 * @return the Builder instance for method chaining
		 */
		public Builder draw(int start, Direction direction, Button button) {
			return draw(Slot.of(start), direction, button);
		}
		
		/**
		 * Draws an item stack starting from a specific slot and moving in a specified direction.
		 *
		 * @param startSlot   The starting slot for drawing the item stack.
		 * @param direction   The direction in which to move while drawing the item stack.
		 * @param itemStack   The item stack to draw.
		 * @return The builder instance for method chaining.
		 */
		public Builder draw(int startSlot, Direction direction, ItemStack itemStack) {
			return draw(Slot.of(startSlot), direction, itemStack);
		}
		
		/**
		 * Draws the specified button in a particular direction across the slots.
		 *
		 * @param direction The direction in which to draw the button.
		 * @param button The button to be drawn.
		 * @return The Builder instance for method chaining.
		 */
		public Builder draw(Direction direction, Button button) {
			return iterate(direction, (content, slot) -> content.setButton(slot, button));
		}
		
		/**
		 * Draws a sequence of buttons in a specified direction starting from the initial slot,
		 * filling each slot with an empty button created from the provided ItemStack.
		 *
		 * @param direction the direction of the slots to be filled
		 * @param itemStack the ItemStack used to create the empty button
		 * @return the Builder instance for chained calls
		 */
		public Builder draw(Direction direction, ItemStack itemStack) {
			return iterate(direction, (content, slot) -> content.setButton(slot, Button.empty(itemStack)));
		}

		/**
		 * Applies the given contentConsumer to manipulate the internal state of the Builder.
		 *
		 * @param contentConsumer the Consumer that defines the operations to be performed on the Builder's Content.
		 * @return the current instance of the Builder for method chaining.
		 */
		public Builder apply(Consumer<Content> contentConsumer) {
			contentConsumer.accept(impl);
			return this;
		}

		/**
		 * Applies the specified pane to the builder's content implementation.
		 *
		 * @param pane the pane to be applied to the builder's content
		 * @return the Builder instance with the applied pane for method chaining
		 */
		public Builder applyPane(Pane pane) {
			pane.applyOn(impl);
			return this;
		}
		
		/**
		 * Constructs a Content object using the current state of the Builder.
		 *
		 * @return The built Content object.
		 */
		public Content build() {
			return impl;
		}

		/**
		 * Sets multiple buttons in their respective slots.
		 *
		 * @param loadedButtons a map where the keys are the slots and the values are the buttons to be set in those slots.
		 * @return the Builder instance to allow for method chaining.
		 */
		public Builder buttons(Map<Slot, Button> loadedButtons) {
			for (Slot slot : loadedButtons.keySet()) {
				setButton(slot, loadedButtons.get(slot));
			}
			return this;
		}
	}
}
