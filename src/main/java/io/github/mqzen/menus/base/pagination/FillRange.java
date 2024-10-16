package io.github.mqzen.menus.base.pagination;

import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.Slot;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.*;

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

    public static FillRange start(Capacity capacity) {
        return start(capacity, Slot.of(0));
    }

    public static FillRange start(Capacity capacity, Slot start) {
        return new FillRange(capacity, start);
    }

    public FillRange end(@Nullable Slot end) {
        if(end != null && end.getSlot() <= start.getSlot()) {
            throw new IllegalStateException("End slot '" + end.getSlot() + "' is less than the start slot");
        }
        this.end = end;
        return this;
    }

    public FillRange except(Slot... slots) {
        forbiddenSlots.addAll(Arrays.asList(slots));
        return this;
    }

    public FillRange except(Collection<Slot> slots) {
        forbiddenSlots.addAll(slots);
        return this;
    }

    public boolean isForbiddenSlot(Slot slot) {
        return forbiddenSlots.contains(slot);
    }

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
