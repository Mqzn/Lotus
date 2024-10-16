package io.github.mqzen.menus.base.animation;

import io.github.mqzen.menus.base.MenuView;
import io.github.mqzen.menus.misc.Slot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Interface representing an animated button within a menu interface.
 */
public interface AnimatedButton {
    
    /**
     * Retrieves the current item stack being displayed by the animated button.
     *
     * @return the current item stack
     */
    ItemStack getCurrentItem();
    
    /**
     * Animates the button inside a menu view
     * @param slot the slot
     * @param view the menu view
     */
    void animate(Slot slot, @NotNull MenuView<?> view);
}
