package weiy.app.basic.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import weiy.app.basic.R;

/** Created by kukiss on 2015/6/16. */
public class WYAction extends RelativeLayout {

	private TextView  vTitle;
	private ImageView vBack;
	private Context   context;

	public WYAction(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public WYAction(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.WYAction);

		String title = ta.getString(R.styleable.WYAction_wy_action_title);
		int color = ta.getColor(R.styleable.WYAction_wy_action_title_color, Color.WHITE);
		boolean _visible = ta.getBoolean(R.styleable.WYAction_wy_action_left_visible, false);

		ta.recycle();

		if (!isInEditMode()){
			vTitle.setTextColor(color);
			vTitle.setText(title);

			if (!_visible) {
				vBack.setVisibility(GONE);
				LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				lp.addRule(CENTER_IN_PARENT);
				vTitle.setLayoutParams(lp);
			}
		}
	}

	private void init() {
		LayoutInflater.from(context).inflate(R.layout.weiy_action_bar, this);
		findViews();
	}

	private void findViews() {
		vTitle = (TextView) findViewById(R.id.title);
		vBack = (ImageView) findViewById(R.id.back);
	}

	public void setTitle(String text) {
		vTitle.setText(text);
	}

	public void setLeft(int visible, OnClickListener listener) {
		vBack.setVisibility(visible);
		vBack.setOnClickListener(listener);
	}
}
