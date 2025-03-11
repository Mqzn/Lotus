package io.github.mqzen.menus.misc.itembuilder;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class ComponentItemBuilder extends ItemBuilder<Component, ComponentItemBuilder> {

	ComponentItemBuilder(ItemStack itemStack) {
		super(itemStack);
	}
	ComponentItemBuilder(Material material,
	                             int amount, short data) {
		super(material, amount, data);
	}


	ComponentItemBuilder(Material material,
	                     int amount) {
		super(material, amount);
	}


	ComponentItemBuilder(Material material) {
		super(material);
	}


	@Override
	protected String toString(Component component) {
		return LegacyComponentSerializer.legacySection().serialize(component);
	}
}
