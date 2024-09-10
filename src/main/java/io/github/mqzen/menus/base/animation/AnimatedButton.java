package io.github.mqzen.menus.base.animation;

import io.github.mqzen.menus.base.MenuView;
import io.github.mqzen.menus.misc.Slot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface AnimatedButton {
    
    ItemStack getCurrentItem();
    
    /**
     * Animates the button inside a menu view
     * @param slot the slot
     * @param view the menu view
     */
    void animate(Slot slot, @NotNull MenuView<?> view);
}
