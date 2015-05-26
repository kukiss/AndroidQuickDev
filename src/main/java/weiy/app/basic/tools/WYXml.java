package weiy.app.basic.tools;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.HashMap;

/** Created by kukiss on 2014/12/10 0010. */
public class WYXml {

	public static ArrayList<HashMap<String, String>> parseXml(XmlPullParser parser, String title) throws Exception {
		int type = parser.getEventType();

		ArrayList<HashMap<String, String>> list = null;
		HashMap<String, String> map = null;
		while (type != XmlPullParser.END_DOCUMENT) {
			switch (type) {
				case XmlPullParser.START_DOCUMENT:
					list = new ArrayList<HashMap<String, String>>();
					break;
				case XmlPullParser.START_TAG:
					if (title.equals(parser.getName())) {
						map = new HashMap<String, String>();
					} else if (map != null) {
						map.put(parser.getName(), parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					if (title.equals(parser.getName()) && list != null) {
						list.add(map);
						map = null;
					}
					break;
			}
			type = parser.next();
		}
		return list;
	}
}
