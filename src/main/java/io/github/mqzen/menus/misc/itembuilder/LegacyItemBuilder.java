package io.github.mqzen.menus.misc.itembuilder;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class LegacyItemBuilder extends ItemBuilder<String, LegacyItemBuilder> {

	LegacyItemBuilder(ItemStack itemStack) {
		super(itemStack);
	}

	LegacyItemBuilder(Material material, int amount, short data) {
		super(material, amount, data);
	}

	LegacyItemBuilder(Material material, int amount) {
		super(material, amount);
	}

	LegacyItemBuilder(Material material) {
		super(material);
	}

	@Override
	protected String toString(String str) {
		return ChatColor.translateAlternateColorCodes('&', str);
	}
}
