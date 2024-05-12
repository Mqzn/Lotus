package io.github.mqzen.menus.titles;

import net.kyori.adventure.text.Component;

public sealed interface MenuTitle permits LegacyTitle, ModernTitle {
	
	Component asComponent();
	
	String asString();
	
}
