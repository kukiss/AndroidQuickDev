package weiy.app.basic.tools;

/** Created by kukiss on 2015/1/2 0002. */
public class Java {

	/** 控制替换默认值 */
	public static <T> T t(T t, T defValue) {

		return t == null ? defValue : t;
	}

	/** 替换字符串, null替换为空字符串 */
	public static String s(String str, String tar, String replace) {

		return str == null ? "" : str.equals(tar) ? replace : str;
	}

	public static boolean isInt(String str) {

		try {
			Integer.parseInt(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isFloat(String str) {

		try {
			Float.parseFloat(str);
			return true;
		} catch (Exception e) {
			return false;
		}
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