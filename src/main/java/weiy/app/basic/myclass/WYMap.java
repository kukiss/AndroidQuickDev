package weiy.app.basic.myclass;

import java.util.HashMap;

/** Created by kukiss on 2015/6/10. */
public class WYMap<K, V> extends HashMap<K, V> {
	public V getOrDef(K key, V def) {
		V v = get(key);
		return v == null ? def : v;
	}

	public V pop(K key) {
		V v = get(key);
		remove(key);
		return v;
	}
}
