package io.github.mqzen.menus.misc.itembuilder;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * An abstract builder class for creating and modifying ItemStack objects with a fluent API.
 *
 * @param <T> the type of text object used for display names and lore
 * @param <B> the type of the builder itself, used for fluent method chaining
 */
@SuppressWarnings("unchecked")
public abstract class ItemBuilder<T, B extends ItemBuilder<T, B>> {

	private final ItemStack itemStack;
	protected ItemMeta itemMeta;

	protected ItemBuilder(ItemStack itemStack) {
		this.itemStack = itemStack;
		this.itemMeta = itemStack.getItemMeta();
	}

	protected ItemBuilder(Material material, int amount, short data) {
		if(amount < 0 || amount > 64) {
			throw new IllegalArgumentException("Invalid ItemStack amount '" + amount + "'");
		}
		this.itemStack = new ItemStack(material, amount, data);
		this.itemMeta = itemStack.getItemMeta();
	}

	protected ItemBuilder(Material material, int amount) {
		this(material, amount, (short) 0);
	}


	protected ItemBuilder(Material material) {
		this(material, 1);
	}

	/**
	 * Converts the given text of generic type T to its string representation.
	 *
	 * @param textType The text of generic type T that needs to be converted to a string.
	 * @return The string representation of the given text.
	 */
	protected abstract String toString(T textType);

	/**
	 * Sets the display name for the item represented by this builder.
	 *
	 * @param display The display name to be set, represented by a generic type T.
	 * @return The updated builder instance for chaining method calls.
	 */
	public B setDisplay(T display) {
		itemMeta.setDisplayName(toString(display));
		return (B) this;
	}

	/**
	 * Sets the lore of the item.
	 *
	 * @param lore the array of lore entries to set, where each entry is converted
	 *             to a string using the {@link #toString(Object)} method.
	 * @return the current instance of the builder for method chaining.
	 */
	public B setLore(T... lore) {
		itemMeta.setLore(Arrays.stream(lore).map(this::toString).collect(Collectors.toList()));
		return (B) this;
	}
	
	/**
	 * Sets the lore (description) of the item with the given list of lore entries.
	 *
	 * @param lore A list of lore entries to set for the item, where each entry represents a line in the lore.
	 * @return The current instance of the item builder for method chaining.
	 */
	public B setLore(List<T> lore) {
		itemMeta.setLore(lore.stream().map(this::toString).collect(Collectors.toList()));
		return (B) this;
	}

	/**
	 * Modifies the metadata of the item based on the provided meta class and consumer.
	 *
	 * @param <M> the type of the item metadata
	 * @param metaClass the class of the item metadata to modify
	 * @param metaConsumer the consumer that defines how the metadata should be modified
	 * @return the updated ItemBuilder instance
	 */
	public <M extends ItemMeta> B modifyMeta(Class<M> metaClass, Consumer<M> metaConsumer) {
		try {
			M meta = metaClass.cast(itemMeta);
			metaConsumer.accept(meta);
			this.itemMeta = meta;
		}catch (ClassCastException ignored) {}
		return (B) this;
	}

	/**
	 * Adds one or more {@link ItemFlag} to the item.
	 *
	 * @param flags the item flags to be added
	 * @return this {@link ItemBuilder} with the added item flags
	 */
	public B addFlags(ItemFlag... flags) {
		this.itemMeta.addItemFlags(flags);
		return (B) this;
	}

	/**
	 * Adds an enchantment to the item with the specified level.
	 *
	 * @param enchantment The enchantment to add.
	 * @param level The level of the enchantment.
	 * @return The current ItemBuilder instance with the added enchantment.
	 */
	public B enchant(Enchantment enchantment, int level) {
		this.itemMeta.addEnchant(enchantment, level, true);
		return (B) this;
	}

	/**
	 * Removes a specified enchantment from the item.
	 *
	 * @param enchantment the enchantment to remove from the item
	 * @return the current instance of {@code ItemBuilder} for chaining
	 */
	public B unEnchant(Enchantment enchantment) {
		this.itemMeta.removeEnchant(enchantment);
		return (B) this;
	}

	/**
	 * Finalizes the construction of the ItemStack by applying the itemMeta.
	 *
	 * @return The constructed ItemStack with the applied meta.
	 */
	public final ItemStack build() {
		this.itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	public static LegacyItemBuilder legacy(ItemStack itemStack) {
		return new LegacyItemBuilder(itemStack);
	}

	public static LegacyItemBuilder legacy(Material material, int amount, short data) {
		return new LegacyItemBuilder(material, amount, data);
	}

	public static LegacyItemBuilder legacy(Material material, int amount) {
		return new LegacyItemBuilder(material, amount);
	}

	public static LegacyItemBuilder legacy(Material material) {
		return new LegacyItemBuilder(material);
	}

	public static ComponentItemBuilder modern(ItemStack itemStack) {
		return new ComponentItemBuilder(itemStack);
	}

	public static ComponentItemBuilder modern(Material material, int amount, short data) {
		return new ComponentItemBuilder(material, amount, data);
	}

	public static ComponentItemBuilder modern(Material material, int amount) {
		return new ComponentItemBuilder(material, amount);
	}

	public static ComponentItemBuilder modern(Material material) {
		return new ComponentItemBuilder(material);
	}

}
