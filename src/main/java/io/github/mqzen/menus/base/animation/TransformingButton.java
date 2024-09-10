package io.github.mqzen.menus.base.animation;

import io.github.mqzen.menus.base.MenuView;
import io.github.mqzen.menus.misc.Slot;
import io.github.mqzen.menus.misc.button.Button;
import io.github.mqzen.menus.misc.button.actions.ButtonClickAction;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import java.time.Duration;

@Getter
public final class TransformingButton extends Button implements AnimatedButton {
    
   
    private final Duration delay;
    private final ItemStack[] transformingItems;
    private int current = 0;
    
    private TransformingButton(Duration delay, @NotNull ItemStack[] transformingItems) {
        super(transformingItems.length == 0 ? null : transformingItems[0]);
        this.delay = delay;
        this.transformingItems = transformingItems;
    }
    
    public static TransformingButton of(Duration delayBetweenTransformations, ItemStack[] transformingItems) {
        return new TransformingButton(delayBetweenTransformations, transformingItems);
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
    
}
