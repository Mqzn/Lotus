package io.github.mqzen.menus.titles;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;

import static io.github.mqzen.menus.titles.MenuTitles.AMPERSAND_SERIALIZER;
import static io.github.mqzen.menus.titles.MenuTitles.SECTION_SERIALIZER;

final class LegacyTitle implements MenuTitle {
	
	private final String text;
	
	LegacyTitle(String text) {
		this.text = text;
	}
	
	@Override
	public Component asComponent() {
		return text.contains("&") ? AMPERSAND_SERIALIZER.deserialize(text)
			: SECTION_SERIALIZER.deserialize(text);
	}
	
	@Override
	public String asString() {
		return ChatColor.translateAlternateColorCodes('&', text);
	}
}