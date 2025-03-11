package io.github.mqzen.menus.titles;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;

final class LegacyTitle implements MenuTitle {
	
	private final String text;
	
	LegacyTitle(String text) {
		this.text = text;
	}
	
	@Override
	public Component asComponent() {
		return text.contains("&") ? LegacyComponentSerializer.legacyAmpersand().deserialize(text)
			: LegacyComponentSerializer.legacySection().deserialize(text);
	}
	
	@Override
	public String asString() {
		return ChatColor.translateAlternateColorCodes('&', text);
	}
}