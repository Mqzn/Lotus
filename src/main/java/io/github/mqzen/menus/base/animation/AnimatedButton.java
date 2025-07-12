package io.github.mqzen.menus.base.animation;

import io.github.mqzen.menus.base.MenuView;
import io.github.mqzen.menus.misc.DataRegistry;
import io.github.mqzen.menus.misc.Slot;
import io.github.mqzen.menus.misc.button.Button;
import io.github.mqzen.menus.misc.button.actions.ButtonClickAction;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface representing an animated button within a menu interface.
 * @see AnimationTaskData
 */
public abstract class AnimatedButton extends Button {
    
    @Getter
    private final AnimationTaskData animationTaskData;
    
    protected AnimatedButton(
            AnimationTaskData animationTaskData,
            @Nullable ItemStack item,
            ButtonClickAction clickAction
    ) {
        super(item, clickAction);
        this.animationTaskData = animationTaskData;
    }
    
    protected AnimatedButton(
            AnimationTaskData animationTaskData,
            @Nullable ItemStack item
    ) {
        super(item);
        this.animationTaskData = animationTaskData;
    }
    
    protected AnimatedButton(
            @Nullable ItemStack item,
            ButtonClickAction clickAction
    ) {
        this(AnimationTaskData.defaultData(), item, clickAction);
    }
    
    protected AnimatedButton(
            @Nullable ItemStack item
    ) {
        this(AnimationTaskData.defaultData(), item);
    }
    
    protected AnimatedButton(
            AnimationTaskData animationTaskData,
            @Nullable ItemStack item,
            @Nullable ButtonClickAction clickAction,
            @NotNull DataRegistry data
    ) {
        super(item, clickAction, data);
        this.animationTaskData = animationTaskData;
    }
    
    /**
     * Animates the button inside a menu view
     * @param slot the slot
     * @param view the menu view
     */
    public abstract void animate(Slot slot, @NotNull MenuView<?> view);
    
    /**
     * Creates a copy of the current Button instance. The copied Button retains the same item and action as the original.
     *
     * @return A new Button instance with identical item and action properties as this Button.
     */
    @Override
    public Button copy() {
        return this;
    }
    
    
}
