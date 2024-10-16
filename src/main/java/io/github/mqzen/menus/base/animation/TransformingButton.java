package io.github.mqzen.menus.base.animation;

import io.github.mqzen.menus.base.MenuView;
import io.github.mqzen.menus.misc.Slot;
import io.github.mqzen.menus.misc.button.Button;
import io.github.mqzen.menus.misc.button.actions.ButtonClickAction;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * The TransformingButton class is an extension of Button that also implements the {@link AnimatedButton} interface.
 * It allows for a button that can transform through a series of ItemStacks.
 */
@Getter
public final class TransformingButton extends Button implements AnimatedButton {
    
   
    private final ItemStack[] transformingItems;
    private int current = 0;
    
    private TransformingButton(@NotNull ItemStack[] transformingItems) {
        super(transformingItems.length == 0 ? null : transformingItems[0]);
        this.transformingItems = transformingItems;
    }
    
    public static TransformingButton of(ItemStack[] transformingItems) {
        return new TransformingButton(transformingItems);
    }
    
    public TransformingButton click(ButtonClickAction action) {
        setAction(action);
        return this;
    }
    
    @Override
    public @NotNull ItemStack getCurrentItem() {
        return transformingItems[current];
    }
    
    @Override
    public void animate(Slot slot, @NotNull MenuView<?> view) {
        if(current+1 >= transformingItems.length)
            current = 0;
        else
            current++;
        
        ItemStack next = transformingItems[current];
        assert next != null;
        
        this.setItem(next);
    }
    
    @Override
    public Button copy() {
        return new TransformingButton(transformingItems);
    }
}
