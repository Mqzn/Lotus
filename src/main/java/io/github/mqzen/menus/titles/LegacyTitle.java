package io.github.mqzen.menus.titles;

import org.bukkit.ChatColor;

final class LegacyTitle implements MenuTitle {
	
	private final String text;
	
	LegacyTitle(String text) {
		this.text = text;
	}
	
	@Override
	public String asString() {
		return ChatColor.translateAlternateColorCodes('&', text);
	}
}