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

		if (!isInEditMode()) {
			TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.WYAction);

			int titleId = ta.getResourceId(R.styleable.WYAction_wy_action_title, -1);
			String title = titleId > 0 ? (String) ta.getResources().getText(titleId) : ta.getString(R.styleable.WYAction_wy_action_title);
			int color = ta.getColor(R.styleable.WYAction_wy_action_title_color, -1);
			boolean _visible = ta.getBoolean(R.styleable.WYAction_wy_action_left_visible, true);
			String theme = ta.getString(R.styleable.WYAction_wy_action_theme);

			if (theme != null) {
				if (theme.equals("1")) {
					vTitle.setTextColor(context.getResources().getColor(android.R.color.background_dark));
					setBackgroundColor(Color.parseColor("#F0EFEE"));
					vBack.setImageResource(R.mipmap.ic_arrow_back_grey600_24dp);
				} else {
					vTitle.setTextColor(context.getResources().getColor(android.R.color.background_light));
					setBackgroundColor(context.getResources().getColor(android.R.color.background_dark));
				}
			}

			if (color != -1) vTitle.setTextColor(color);
			vTitle.setText(title);

			if (!_visible) {
				vBack.setVisibility(GONE);
				LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				lp.addRule(CENTER_IN_PARENT);
				vTitle.setLayoutParams(lp);
			}

			ta.recycle();
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

	public void setViewLeft(int resId) {
		vBack.setImageResource(resId);
	}

	public void setViewLeft(int resId, OnClickListener listener) {
		vBack.setImageResource(resId);
		vBack.setOnClickListener(listener);
	}

}
