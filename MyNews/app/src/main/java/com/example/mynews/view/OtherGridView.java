package com.example.mynews.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;


public class OtherGridView extends GridView {

    public OtherGridView(Context context) {
        super(context);
    }

    public OtherGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OtherGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
