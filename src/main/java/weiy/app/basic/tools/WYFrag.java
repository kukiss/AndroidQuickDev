package weiy.app.basic.tools;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import java.util.HashMap;

public class WYFrag {

	/** 显示一个DialogFragment, 会预先删除已显示得到DialogFragment. */
	public static void showDialog(FragmentManager manager, DialogFragment fragment) {
		dismissDialog(manager);
		fragment.show(manager, "dialog");
	}

	/** 取消显示的dialog */
	public static void dismissDialog(FragmentManager manager) {
		Fragment f = manager.findFragmentByTag("dialog");
		if (f != null) {
			FragmentTransaction t = manager.beginTransaction();
			t.remove(f);
			t.commit();
		}
	}

	/** 显示一个v4.DialogFragment, 会预先删除已显示得到DialogFragment. */
	public static void showDialog(android.support.v4.app.FragmentManager manager, android.support.v4.app.DialogFragment fragment) {
		dismissDialog(manager);
		fragment.show(manager, "dialog");
	}

	/** 取消显示的v4.dialog */
	public static void dismissDialog(android.support.v4.app.FragmentManager manager) {
		android.support.v4.app.Fragment f = manager.findFragmentByTag("dialog");
		if (f != null) {
			android.support.v4.app.FragmentTransaction t = manager.beginTransaction();
			t.remove(f);
			t.commit();
		}
	}

	/** 替换一个容器中的v4.Fragment, 设置是否加入回退栈 */
	public static void replaceFragment(android.support.v4.app.FragmentManager manager, int container, android.support.v4.app.Fragment fragment, String tag, Bundle params) {
		android.support.v4.app.FragmentTransaction t = manager.beginTransaction();
		if (params != null && !params.isEmpty()) {
			fragment.setArguments(params);
		}
		if (tag != null) {
			t.replace(container, fragment, tag);
		} else {
			t.replace(container, fragment);
		}
		if (tag != null) {
			t.addToBackStack(tag);
		}
		t.commit();
	}

	/** 增加一个fragment, 不销毁上一个fragment的视图 */
	public static void addFragment(android.support.v4.app.FragmentManager manager, int container, android.support.v4.app.Fragment current, android.support.v4.app.Fragment fragment, String back) {
		android.support.v4.app.FragmentTransaction t = manager.beginTransaction();
		t.hide(current);
		t.add(container, fragment);
		if (back != null) {
			t.addToBackStack(back);
		}
		t.commit();
	}

}
