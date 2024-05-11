package io.github.mqzen.menus.titles;

import net.kyori.adventure.text.Component;
import static io.github.mqzen.menus.titles.MenuTitles.SECTION_SERIALIZER;

public final class ModernTitle implements MenuTitle {
		
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
			return SECTION_SERIALIZER.serialize(component);
		}
	}