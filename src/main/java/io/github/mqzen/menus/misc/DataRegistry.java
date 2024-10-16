package io.github.mqzen.menus.misc;

import java.util.*;

/**
 * The DataRegistry class is a container for storing and retrieving key-value pairs,
 * where keys are strings, and values are objects of any type.
 */
public final class DataRegistry {
	
	private final Map<String, Object> objectMap = new HashMap<>();
	
	/**
	 * Creates a new, empty instance of the DataRegistry class.
	 *
	 * @return A new instance of DataRegistry with no key-value pairs.
	 */
	public static DataRegistry empty() {
		return new DataRegistry();
	}
	
	/**
	 * Associates the specified object with the given key in the data registry.
	 *
	 * @param key the key with which the specified object is to be associated
	 * @param object the object to be associated with the specified key
	 */
	public void setData(String key, Object object) {
		objectMap.put(key, object);
	}

	/**
	 * Retrieves a set of all keys present in the data registry.
	 *
	 * @return A set containing all keys currently stored in the data registry.
	 */
	public Set<String> keys() {
		return objectMap.keySet();
	}

	/**
	 * Retrieves a collection of all values stored in the data registry.
	 *
	 * @return A collection containing all values currently stored in the data registry.
	 */
	public Collection<Object> values() {
		return objectMap.values();
	}


	/**
	 * Retrieves data associated with the specified key from the data registry.
	 *
	 * @param <T> The type of the data to be returned.
	 * @param key The key associated with the data to be retrieved.
	 * @return The data associated with the given key, or null if no data is found.
	 */
	@SuppressWarnings("unchecked")
	public <T> T getData(String key) {
		return (T) objectMap.get(key);
	}
}
