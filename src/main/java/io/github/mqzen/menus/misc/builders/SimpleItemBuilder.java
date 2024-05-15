package io.github.mqzen.menus.misc.builders;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SimpleItemBuilder extends ItemBuilder<SimpleItemBuilder> {
    SimpleItemBuilder(final Material material, final int amount) {
        super(material, amount);
    }

    SimpleItemBuilder(@NotNull final ItemStack item) {
        super(item);
    }
}
