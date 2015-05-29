package weiy.app.basic.tools;

/** Created by kukiss on 2015/1/2 0002. */
public class WYHelp {

	/** 控制替换默认值 */
	public static <T> T t(T t, T defValue) {

		return t == null ? defValue : t;
	}

	/** 替换字符串, null替换为空字符串 */
	public static String s(String str, String tar, String replace) {

		return str == null ? "" : str.equals(tar) ? replace : str;
	}

	public static boolean isInt(String str) {
		for (int i = 0, count = str.length(); i < count; i++) {
			int chr = str.charAt(i);
			if (i == 0) {
				if ((chr < 48 && chr != 45) || chr > 57) return false;
			} else if (chr < 48 || chr > 57) return false;
		}
		return true;
	}

	public static int parseInt(String str, int def) {
		if (isInt(str)) return Integer.parseInt(str);
		else return def;
	}

	public static boolean isFloat(String str) {
		int docNum = 0;
		for (int i = 0, count = str.length(); i < count; i++) {
			int chr = str.charAt(i);
			if (i == 0) {
				if (chr == 46 || chr == 45) {
					return false;
				} else if (chr < 48 || chr > 57) return false;
			} else if (chr == 46) {
				docNum++;
				if (docNum > 1) return false;
			} else if (chr < 48 || chr > 57) return false;
		}
		return true;
	}

	public static float parseFloat(String str, float def) {
		if (isFloat(str)) return Float.parseFloat(str);
		else return def;
	}

	/*
	public static String getString(String str, String[] tar, String replace) {
		if (str == null) {
			return "";
		}

		for (String s : tar)
			if (str.equals(s)) {
				return replace;
			}

		return str;
	}
	*/
}