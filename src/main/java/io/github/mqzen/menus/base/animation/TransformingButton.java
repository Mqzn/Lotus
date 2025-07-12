package io.github.mqzen.menus.base.animation;

import io.github.mqzen.menus.base.MenuView;
import io.github.mqzen.menus.misc.DataRegistry;
import io.github.mqzen.menus.misc.Slot;
import io.github.mqzen.menus.misc.button.actions.ButtonClickAction;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import java.util.function.UnaryOperator;

/**
 * The TransformingButton class is an extension of Button that also implements the {@link AnimatedButton} interface.
 * It allows for a button that can transform through a series of ItemStacks.
 */
@Getter
public final class TransformingButton extends AnimatedButton {
    
    private final ItemStack[] transformingItems;
    private UnaryOperator<ItemStack> itemTransformer;
    private int current = 0;
    
    public TransformingButton(AnimationTaskData animationTaskData, UnaryOperator<ItemStack> itemTransformer, @NotNull ItemStack[] transformingItems) {
        super(animationTaskData, transformingItems.length == 0 ? null : itemTransformer.apply(transformingItems[0]));
        this.itemTransformer = itemTransformer;
        this.transformingItems = transformingItems;
    }
    
    public TransformingButton(UnaryOperator<ItemStack> itemTransformer, @NotNull ItemStack[] transformingItems) {
        this(AnimationTaskData.defaultData(), itemTransformer, transformingItems);
    }

    public TransformingButton(AnimationTaskData animationTaskData, @NotNull ItemStack[] transformingItems, DataRegistry dataRegistry) {
        this(animationTaskData, item -> item, transformingItems, dataRegistry);
    }
    
    public TransformingButton(AnimationTaskData animationTaskData, @NotNull ItemStack[] transformingItems) {
        this(animationTaskData, item -> item, transformingItems);
    }
    
    public TransformingButton(@NotNull ItemStack[] transformingItems) {
        this(item -> item, transformingItems);
    }
    
    public TransformingButton(AnimationTaskData animationTaskData, UnaryOperator<ItemStack> itemTransformer, @NotNull ItemStack[] transformingItems, DataRegistry dataRegistry) {
        super(animationTaskData, transformingItems.length == 0 ? null : itemTransformer.apply(transformingItems[0]), null, dataRegistry);
        this.itemTransformer = itemTransformer;
        this.transformingItems = transformingItems;
    }
    
    public TransformingButton itemTransformer(UnaryOperator<ItemStack> itemTransformer) {
        this.itemTransformer = itemTransformer;
        if (transformingItems.length > 0) {
            this.setItem(itemTransformer.apply(transformingItems[0]));
        }
        return this;
    }
   
    public TransformingButton click(ButtonClickAction action) {
        setAction(action);
        return this;
    }
    
    public TransformingButton click(ButtonClickAction.ActionExecutor action) {
        setAction(ButtonClickAction.plain(action));
        return this;
    }
    
    @Override
    public void animate(Slot slot, @NotNull MenuView<?> view) {
        if(current+1 >= transformingItems.length)
            current = 0;
        else
            current++;
        
        ItemStack next = transformingItems[current];
        assert next != null;
        
        this.setItem(itemTransformer.apply(next));
        view.replaceButton(slot, this);
    }
}
