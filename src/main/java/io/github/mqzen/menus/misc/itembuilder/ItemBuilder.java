package io.github.mqzen.menus.misc.itembuilder;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.function.Consumer;

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

	protected abstract String toString(T textType);

	public B setDisplay(T display) {
		itemMeta.setDisplayName(toString(display));
		return (B) this;
	}

	public B setLore(T... lore) {
		itemMeta.setLore(Arrays.stream(lore).map(this::toString).toList());
		return (B) this;
	}

	public <M extends ItemMeta> B modifyMeta(Class<M> metaClass, Consumer<M> metaConsumer) {
		try {
			M meta = metaClass.cast(itemMeta);
			metaConsumer.accept(meta);
			this.itemMeta = meta;
		}catch (ClassCastException ignored) {}
		return (B) this;
	}

	public B addFlags(ItemFlag... flags) {
		this.itemMeta.addItemFlags(flags);
		return (B) this;
	}

	public B enchant(Enchantment enchantment, int level) {
		this.itemMeta.addEnchant(enchantment, level, true);
		return (B) this;
	}

	public B unEnchant(Enchantment enchantment) {
		this.itemMeta.removeEnchant(enchantment);
		return (B) this;
	}

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
