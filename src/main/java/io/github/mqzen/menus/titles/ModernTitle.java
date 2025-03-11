package io.github.mqzen.menus.titles;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

final class ModernTitle implements MenuTitle {
	
	private final Component component;
	
	ModernTitle(Component component) {
		this.component = component;
	}
	
	
	@Override
	public Component asComponent() {
		return component;
	}
	
	@Override
	public String asString() {
		return LegacyComponentSerializer.legacySection().serialize(component);
	}
}