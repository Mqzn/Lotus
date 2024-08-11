package io.github.mqzen.menus.misc.itembuilder;

import io.github.mqzen.menus.titles.MenuTitles;
import net.kyori.adventure.text.Component;
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
		return MenuTitles.SECTION_SERIALIZER.serialize(component);
	}
}
