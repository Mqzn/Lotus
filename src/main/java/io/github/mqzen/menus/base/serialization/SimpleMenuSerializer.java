package io.github.mqzen.menus.base.serialization;

import io.github.mqzen.menus.base.Content;
import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.DataRegistry;

final class SimpleMenuSerializer implements MenuSerializer {
	
	public final static String PLAYER_PLACEHOLDER = "<player>";
	
	SimpleMenuSerializer() {}
	
	@Override
	public DataRegistry serialize(SerializableMenu menu) {
		DataRegistry dataRegistry = new DataRegistry();
		dataRegistry.setData("name", menu.getName());
		
		String title = menu.titleString();
		dataRegistry.setData("title", title);
		
		Capacity capacity =  menu.capacity();
		dataRegistry.setData("capacity", capacity);
		
		Content content = menu.content();
		dataRegistry.setData("content", content);
		
		return dataRegistry;
	}
	
	@Override
	public SerializableMenu deserialize(DataRegistry dataRegistry) {
		return new SerializableMenu(dataRegistry);
	}
	
}
