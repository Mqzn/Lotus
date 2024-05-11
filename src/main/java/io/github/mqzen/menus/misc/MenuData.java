package io.github.mqzen.menus.misc;

import java.util.HashMap;
import java.util.Map;

public final class MenuData {
	
	private final Map<String, Object> objectMap = new HashMap<>();
	
	public void setData(String key, Object object) {
		objectMap.put(key, object);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getData(String key) {
		return (T) objectMap.get(key);
	}
	
	public static MenuData empty() {
		return new MenuData();
	}
}
