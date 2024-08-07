package io.github.mqzen.menus.misc.itembuilder.impl;

import io.github.mqzen.menus.misc.itembuilder.AbstractItemBuilder;
import io.github.mqzen.menus.misc.itembuilder.EnchantmentEntry;
import io.github.mqzen.menus.titles.MenuTitles;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import java.util.List;

public class SimpleItemBuilder extends AbstractItemBuilder {
	
	SimpleItemBuilder(Material material, int amount) {
		super(material, amount);
	}
	
	public static SimpleItemBuilder of(Material material) {
		return new SimpleItemBuilder(material, 1);
	}
	
	public static SimpleItemBuilder of(Material material, int amount) {
		return new SimpleItemBuilder(material, amount);
	}
	
	public SimpleItemBuilder display(Component component) {
		modify(itemMeta -> itemMeta.setDisplayName(MenuTitles.SECTION_SERIALIZER.serialize(component)));
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
	
	public SimpleItemBuilder unbreakable(boolean unbreakable) {
		modify(itemMeta -> {
			itemMeta.spigot().setUnbreakable(unbreakable);
		});
		return this;
	}
	
	public SimpleItemBuilder glow() {
		enchant(EnchantmentEntry.of(Enchantment.DURABILITY, 1, true));
		flags(ItemFlag.HIDE_ENCHANTS);
		return this;
	}
	
	public SimpleItemBuilder amount(int amount) {
		getItemStack().setAmount(amount);
		return this;
	}
	
	public SimpleItemBuilder lore(List<Component> lore) {
		modify(itemMeta -> itemMeta.setLore(lore.stream()
				  .map(MenuTitles.SECTION_SERIALIZER::serialize)
				  .toList()));
		return this;
	}

	
}
