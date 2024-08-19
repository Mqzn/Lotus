package io.github.mqzen.menus.base.serialization;

import io.github.mqzen.menus.misc.DataRegistry;

public interface MenuSerializer {
	
	DataRegistry serialize(SerializableMenu menu);
	
	SerializableMenu deserialize(DataRegistry dataRegistry);
	
	static MenuSerializer newDefaultSerializer() {
		return new SimpleMenuSerializer();
	}
}
