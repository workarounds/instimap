package com.mrane.campusmap;

import in.designlabs.instimap.R;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class ExtendedRelativeLayout extends RelativeLayout {

	private Context myContext;

	public ExtendedRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		myContext = context;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int cardHeight = (int)myContext.getResources().getDimension(R.dimen.card_height);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec + cardHeight);
	}

}
