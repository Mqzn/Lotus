package io.github.mqzen.menus.misc.itembuilder.impl;

import io.github.mqzen.menus.misc.itembuilder.AbstractItemBuilder;
import io.github.mqzen.menus.misc.itembuilder.AttributeEntry;
import io.github.mqzen.menus.misc.itembuilder.EnchantmentEntry;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @Author <a href="https://github.com/Cobeine">Cobeine</a>
 */

public class SimpleItemBuilder extends AbstractItemBuilder {

    SimpleItemBuilder(Material material,int amount) {
        super(material,amount);
    }

    public static SimpleItemBuilder of(Material material) {
        return new SimpleItemBuilder(material,1);
    }
    public static SimpleItemBuilder of(Material material,int amount) {
        return new SimpleItemBuilder(material,amount);
    }

    public SimpleItemBuilder display(Component component) {
        modify(itemMeta -> itemMeta.displayName(component));
        return this;
    }
    public SimpleItemBuilder flags(ItemFlag... flags) {
        modify(itemMeta -> itemMeta.addItemFlags(flags));
        return this;
    }
    public SimpleItemBuilder enchant(EnchantmentEntry... entries) {
        modify(itemMeta -> {
            for (EnchantmentEntry entry : entries) {
                itemMeta.addEnchant(entry.getEnchantment(), entry.getLevel(), entry.isIgnore());
            }
        });
        return this;
    }

    public SimpleItemBuilder unbreakable() {
      return unbreakable(true);
    }

    private SimpleItemBuilder unbreakable(boolean unbreakable) {
        modify(itemMeta -> {
            itemMeta.setUnbreakable(unbreakable);
        });
        return this;
    }

    public SimpleItemBuilder lore(List<Component> lore) {
        modify(itemMeta -> itemMeta.lore(lore));
        return this;
    }

    public SimpleItemBuilder attribute(AttributeEntry... entry) {
        modify(itemMeta -> {
            for (AttributeEntry attributeEntry : entry) {
                itemMeta.addAttributeModifier(attributeEntry.attribute(), attributeEntry.modifier());
            }
        });
        return this;
    }

    public void test() {

    }


}
