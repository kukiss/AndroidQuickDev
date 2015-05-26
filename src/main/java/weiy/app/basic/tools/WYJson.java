package weiy.app.basic.tools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class WYJson {

	/** 解析json数组, 输出全部为字符串 */
	public static ArrayList<HashMap<String, String>> jsonToList(String json) {

		ArrayList<HashMap<String, String>> list = new ArrayList<>();
		try {
			JSONArray array = new JSONArray(json);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<>();
				Iterator<String> it = obj.keys();
				while (it.hasNext()) {
					String key = it.next();
					map.put(key, obj.getString(key));
				}
				list.add(map);
			}
		} catch (Exception e) {
			list = null;
		}
		return list;
	}

	/** 解析json数组, 输出全部为字符串 */
	public static HashMap<String, String> jsonToMap(String json) {

		HashMap<String, String> map = new HashMap<>();
		try {
			JSONObject obj = new JSONObject(json);

			Iterator<String> it = obj.keys();
			while (it.hasNext()) {
				String key = it.next();
				map.put(key, obj.getString(key));
			}
		} catch (Exception e) {
			return null;
		}
		return map;
	}
}
