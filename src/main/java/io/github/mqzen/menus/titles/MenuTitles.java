package io.github.mqzen.menus.titles;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public final class MenuTitles {
	public final static MiniMessage MINI_MESSAGE_API = MiniMessage.builder()
		.tags(TagResolver.standard())
		.build();

	public final static LegacyComponentSerializer AMPERSAND_SERIALIZER = LegacyComponentSerializer.legacyAmpersand();
	public final static LegacyComponentSerializer SECTION_SERIALIZER = LegacyComponentSerializer.legacySection();
	
	public static MenuTitle createLegacy(String title) {
		return new LegacyTitle(title);
	}
	
	public static MenuTitle createModern(Component component) {
		return new ModernTitle(component);
	}
	
	public static MenuTitle createModern(MiniMessage apiObject, String miniMessage) {
		return createModern(apiObject.deserialize(miniMessage));
	}
	
	public static MenuTitle createModern(String miniMessage) {
		return createModern(MINI_MESSAGE_API, miniMessage);
	}
}
