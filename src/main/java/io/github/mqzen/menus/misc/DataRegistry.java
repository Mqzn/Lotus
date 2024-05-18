package io.github.mqzen.menus.misc;

import java.util.HashMap;
import java.util.Map;

public final class DataRegistry {
	
	private final Map<String, Object> objectMap = new HashMap<>();
	
	public static DataRegistry empty() {
		return new DataRegistry();
	}
	
	public void setData(String key, Object object) {
		objectMap.put(key, object);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getData(String key) {
		return (T) objectMap.get(key);
	}
}
