package io.github.mqzen.menus.misc.builders;

import io.github.mqzen.menus.misc.Button;
import io.github.mqzen.menus.misc.ButtonAction;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Damageable;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.kyori.adventure.text.Component;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

@SuppressWarnings({ "unchecked", "unused", "deprecation" })
public abstract class ItemBuilder<B extends ItemBuilder<B>> {
    final ItemStack item;

    ItemMeta meta;

    private static final ItemFlag[] flags = ItemFlag.values();

    private final boolean hasNoItemMeta;

    ItemBuilder(Material material, int amount) {
        this(new ItemStack(material, amount));
    }

    ItemBuilder(@NotNull ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
        this.hasNoItemMeta = this.meta == null;
    }

    public static SimpleItemBuilder simple(Material material, int amount) {
        return new SimpleItemBuilder(material, amount);
    }

    public static SimpleItemBuilder simple(ItemStack item) {
        return new SimpleItemBuilder(item);
    }

    /**
     * Sets the glow effect on the item.
     *
     * @param  glow  true to add enchantment and hide it, false to remove enchantment and show it
     * @apiNote Will hide the enchantments by default.
     * @return       the builder for chaining
     */
    public B glow(boolean glow) {
        // add enchantment and hide it if "glow" is true
        if (this.hasNoItemMeta) return (B) this;
        if (!glow) {
            this.meta.removeEnchant(Enchantment.DURABILITY);
            this.meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
            return (B) this;
        }
        this.meta.addEnchant(Enchantment.DURABILITY, 1, true);
        this.meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return (B) this;
    }

    /**
     * Sets the amount of the item.
     * @param amount the amount to set
     * @return the builder for chaining
     */
    public B amount(int amount) {
        this.item.setAmount(amount);
        return (B) this;
    }

    /**
     * Sets the name of the itemStack to whatever the provided name is.
     * @param name the new name
     * @return the builder for chaining
     */
    public B setName(String name) {
        if (this.hasNoItemMeta) return (B) this;
        this.meta.setDisplayName(translateAlternateColorCodes('&', name));
        return (B) this;
    }

    /**
     * Sets the name of the itemStack to whatever the provided name is.
     * @param name the new name
     * @return the builder for chaining
     */
    public B name(Component name) {
        if (this.hasNoItemMeta) return (B) this;
        meta.displayName(name);
        return (B) this;
    }

    /**
     * Sets the lore of the itemStack to whatever the provided lore is.
     * @param lore the new lore
     * @return the builder for chaining
     */
    public B setLore(String... lore) {
        return this.setLore(List.of(lore));
    }

    /**
     * Sets the lore of the itemStack to whatever the provided lore is.
     * @param lore the new lore
     * @return the builder for chaining
     */
    public B setLore(List<String> lore) {
        if (this.hasNoItemMeta) return (B) this;
        this.meta.setLore(lore);
        return (B) this;
    }

    /**
     * Sets the lore of the itemStack to whatever the provided lore is.
     * @param lore the new lore
     * @return the builder for chaining
     */
    public B lore(Component... lore) {
        return lore(List.of(lore));
    }

    /**
     * Sets the lore of the itemStack to whatever the provided lore is.
     * @param lore the new lore
     * @return the builder for chaining
     */
    public B lore(List<Component> lore) {
        if (this.hasNoItemMeta) return (B) this;
        meta.lore(lore);
        return (B) this;
    }

    /**
     * Enchant the itemStack with the provided enchantment
     * @param enchant the enchantment to enchant the itemStack with
     * @return the builder for chaining
     */
    public B enchant(Enchantment enchant) {
        if (this.hasNoItemMeta) return (B) this;
        this.meta.addEnchant(enchant, 1, false);
        return (B) this;
    }

    /**
     * Enchant the itemStack with the provided enchantment
     * @param enchant the enchantment to enchant the itemStack with
     * @param level the level of the enchantment
     * @return the builder for chaining
     */
    public B enchant(Enchantment enchant, int level) {
        if (this.hasNoItemMeta) return (B) this;
        this.meta.addEnchant(enchant, level, false);
        return (B) this;
    }

    /**
     * Enchant the itemStack with the provided enchantment
     * @param enchant the enchantment to enchant the itemStack with
     * @param level the level of the enchantment
     * @param ignore whether to ignore the enchantment restrictions
     * @return the builder for chaining
     */
    public B enchant(Enchantment enchant, int level, boolean ignore) {
        if (this.hasNoItemMeta) return (B) this;
        this.meta.addEnchant(enchant, level, ignore);
        return (B) this;
    }

    /**
     * Apply all the enchantments to the itemStack with the same level & ignore restrictions
     * @param level the level of the enchantment
     * @param ignore whether to ignore the enchantment restrictions
     * @param enchant the enchantments to apply
     * @return the builder for chaining
     */
    public B enchant(int level, boolean ignore, Enchantment... enchant) {
        if (this.hasNoItemMeta) return (B) this;
        for (Enchantment enchantment : enchant)
            this.meta.addEnchant(enchantment, level, ignore);
        return (B) this;
    }

    /**
     * Apply all the enchantments to the itemStack with the same level
     * @param level the level of the enchantment
     * @param enchant the enchantments to apply
     * @return the builder for chaining
     */
    public B enchant(int level, Enchantment... enchant) {
        if (this.hasNoItemMeta) return (B) this;
        for (Enchantment enchantment : enchant)
            this.meta.addEnchant(enchantment, level, false);
        return (B) this;
    }

    /**
     * Apply all the enchantments to the itemStack (level 1)
     * @param enchant the enchantments to apply
     * @return the builder for chaining
     */
    public B enchant(Enchantment... enchant) {
        if (this.hasNoItemMeta) return (B) this;
        for (Enchantment enchantment : enchant)
            this.meta.addEnchant(enchantment, 1, false);
        return (B) this;
    }

    /**
     * Set the itemStack to be unbreakable
     * @return the builder for chaining
     */
    public B unbreakable() {
        if (this.hasNoItemMeta) return (B) this;
        this.meta.setUnbreakable(true);
        return (B) this;
    }

    /**
     * Set the itemStack to be unbreakable or not
     * @param breakable whether the itemStack is unbreakable
     * @return the builder for chaining
     */
    public B unbreakable(boolean breakable) {
        if (this.hasNoItemMeta) return (B) this;
        this.meta.setUnbreakable(breakable);
        return (B) this;
    }

    /**
     * Add all the item flags to the item meta
     * @return the builder for chaining
     */
    public B allItemFlags() {
        if (this.hasNoItemMeta) return (B) this;
        this.meta.addItemFlags(flags);
        return (B) this;
    }

    /**
     * Adds an item flag to the item meta.
     * @param flag the flag to add
     * @return the updated item meta
     */
    public B addItemFlags(ItemFlag flag) {
        if (this.hasNoItemMeta) return (B) this;
        this.meta.addItemFlags(flag);
        return (B) this;
    }

    /**
     * Adds an attribute modifier to the item meta.
     *
     * @param  attribute  the attribute to modify
     * @param  modifier   the modifier to apply
     * @return            the updated item meta
     */
    public B attributeModifier(Attribute attribute, AttributeModifier modifier) {
        if (this.hasNoItemMeta) return (B) this;
        this.meta.addAttributeModifier(attribute, modifier);
        return (B) this;
    }

    /**
     * Set the damage to the itemStack
     * @param d the damage
     * @return the builder for chaining
     */
    public B damage(int d) {
        if (this.hasNoItemMeta || !(meta instanceof Damageable)) return (B) this;
        ((Damageable) meta).damage(d);
        return (B) this;
    }

    /**
     * Build the item into a new ItemStack.
     * @return the new ItemStack
     */
    public ItemStack build() {
        this.item.setItemMeta(meta);
        return item;
    }

    /**
     * Build the item into a new MenuItem.
     * @return the new MenuItem
     */
    public Button empty() {
        return Button.empty(build());
    }

    /**
     * Build the item into a new MenuItem with the provided Click Event.
     * @param event the event
     * @return the new MenuItem
     */
    public Button clickable(ButtonAction event) {
        return Button.clickable(build(), event);
    }
}
