package weiy.app.basic.myclass;

import java.util.ArrayList;

/** Created by kukiss on 2015/6/10. */
public class WYList<E> extends ArrayList<E> {
	@Override
	public E get(int index) {
		if (index >= size()) return null;
		else return super.get(index);
	}
}
