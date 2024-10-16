package io.github.mqzen.menus.base.pagination;

import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.Slot;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.*;

/**
 * FillRange is a utility class used to define a range of slots to be filled in a paginated context.
 * It helps in setting ranges and excluding certain slots.
 */
@Getter
public final class FillRange {

    private final Capacity capacity;
    private final @NotNull Slot start;
    private @Nullable Slot end = null;

    private final Set<Slot> forbiddenSlots = new HashSet<>();

    private FillRange(Capacity capacity, @NotNull Slot start) {
        this.capacity = capacity;
        this.start = start;
    }

    /**
     * Initializes a FillRange object with the given capacity and a starting slot of zero.
     *
     * @param capacity the capacity defining the total size for the range.
     * @return a new FillRange object starting from slot 0 with the specified capacity.
     */
    public static FillRange start(Capacity capacity) {
        return start(capacity, Slot.of(0));
    }

    /**
     * Creates a new instance of FillRange starting from the specified slot.
     *
     * @param capacity the capacity defining the total size and structure.
     * @param start the starting slot for this range.
     * @return a new instance of FillRange initialized with the given capacity and starting slot.
     */
    public static FillRange start(Capacity capacity, Slot start) {
        return new FillRange(capacity, start);
    }

    /**
     * Sets the end slot for the range. Ensures the end slot is greater than the start slot.
     *
     * @param end the slot to set as the end of the range; can be null.
     * @return the FillRange instance with the updated end slot.
     * @throws IllegalStateException if the provided end slot is less than or equal to the start slot.
     */
    public FillRange end(@Nullable Slot end) {
        if(end != null && end.getSlot() <= start.getSlot()) {
            throw new IllegalStateException("End slot '" + end.getSlot() + "' is less than the start slot");
        }
        this.end = end;
        return this;
    }

    /**
     * Excludes the specified slots from being filled.
     *
     * @param slots the slots to be excluded from the fill range
     * @return the updated FillRange instance with the specified slots excluded
     */
    public FillRange except(Slot... slots) {
        forbiddenSlots.addAll(Arrays.asList(slots));
        return this;
    }

    /**
     * Adds a collection of forbidden slots to the current list of forbidden slots.
     *
     * @param slots a collection of slots to be marked as forbidden
     * @return the current FillRange instance with updated forbidden slots
     */
    public FillRange except(Collection<Slot> slots) {
        forbiddenSlots.addAll(slots);
        return this;
    }

    /**
     * Checks if a given slot is marked as forbidden.
     *
     * @param slot the slot to check for being forbidden
     * @return true if the slot is forbidden, false otherwise
     */
    public boolean isForbiddenSlot(Slot slot) {
        return forbiddenSlots.contains(slot);
    }

    /**
     * Calculates the count of available slots within the specified range that are not forbidden.
     * The range is defined from the starting slot to the ending slot. If the ending slot
     * is not set, it defaults to the last slot within the given capacity.
     *
     * @return the count of available slots excluding the forbidden slots within the specified range.
     * @throws IllegalStateException if the count of forbidden slots is greater than or equal to the total range.
     */
    public int getCount() {
        if(end == null) {
            end = Slot.last(capacity);
        }
        int range = end.getSlot()-start.getSlot();
        int diff = range-forbiddenSlots.size();
        if(diff <= 0) {
            throw new IllegalStateException("Forbidden slots count is greater than or equal to the range provided");
        }
        return diff;
    }
}
