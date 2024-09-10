package io.github.mqzen.menus.base.animation;

import io.github.mqzen.menus.base.MenuView;
import io.github.mqzen.menus.misc.Slot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public interface AnimatedButton {
    
    @NotNull Duration getDelay();
    
    ItemStack getCurrentItem();
    
    default long getDelayInTicks() {
        return getDelay().toMillis()/50L;
    }

    
    /**
     * Animates the button inside a menu view
     * @param slot the slot
     * @param view the menu view
     */
    void animate(Slot slot, @NotNull MenuView<?> view);
}
