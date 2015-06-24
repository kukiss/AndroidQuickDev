package weiy.app.basic.myclass;

import java.util.HashMap;

/** Created by kukiss on 2015/6/4. */
public class Temp {
	private static Temp                    params = new Temp();
	private        HashMap<String, Object> map    = new HashMap<>();

	private String lastKey;

	public Temp() {}

	public static Temp getInstance() {
		return params;
	}

	public void put(String key, Object obj) {
		map.put(key, obj);
	}

	public void putNoNull(String key, Object obj) {
		if (key != null && obj != null) put(key, obj);
	}

	public <T> T get(String key) {
		return get(key, null);
	}

	public <T> T get(String key, T def) {
		if (map.containsKey(key) && map.get(key) != null) {
			return (T) map.get(key);
		} else {
			return def;
		}
	}

	public <T> T pop(String key) {
		return pop(key, null);
	}

	public <T> T pop(String key, T def) {
		if (map.containsKey(key) && map.get(key) != null) {
			T t = (T) map.get(key);
			map.remove(key);
			return t;
		} else {
			return def;
		}
	}

	public void clear() {
		map.clear();
	}

	public void remove(String key) {
		if (map.containsKey(key)) map.remove(key);
	}

	public void removeLast() {
		if (map.containsKey(lastKey)) map.remove(lastKey);
	}
}
