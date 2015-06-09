package weiy.app.basic.tools;

import android.content.Context;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/** Created by kukiss on 2015/6/9. */
public class WYObject {
	/** save object to file */
	public static boolean saveObject(Context context, Object obj, String name) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(context.openFileOutput(name, Context.MODE_PRIVATE));
			oos.writeObject(obj);
			oos.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/** get object from file */
	public static <T> T getObject(Context context, String name) {
		try {
			ObjectInputStream oos = new ObjectInputStream(context.openFileInput(name));
			Object object = oos.readObject();
			oos.close();
			return (T) object;
		} catch (Exception e) {
			return null;
		}
	}

	/** remove object that in file */
	public static boolean removeObject(Context context, String name) {
		return context.deleteFile(name);
	}
}
