package io.github.mqzen.menus.misc.itembuilder;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

/**
 * @Author <a href="https://github.com/Cobeine">Cobeine</a>
 */

public record AttributeEntry(Attribute attribute, AttributeModifier modifier) {

    public static AttributeEntry of(Attribute attribute, AttributeModifier modifier) {
        return new AttributeEntry(attribute, modifier);
    }
}
