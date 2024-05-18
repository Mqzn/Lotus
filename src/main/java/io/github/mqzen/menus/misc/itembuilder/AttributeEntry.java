package io.github.mqzen.menus.misc.itembuilder;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public record AttributeEntry(Attribute attribute, AttributeModifier modifier) {
	
	public static AttributeEntry of(Attribute attribute, AttributeModifier modifier) {
		return new AttributeEntry(attribute, modifier);
	}
}
