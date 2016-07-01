package com.md.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by SECONDHEAVEN on 2016/1/1.
 */
public class MyGridLayoutManager extends GridLayoutManager {

    private int spanCount;
    private int parentWidth;
    private int width = 0;
    private int height = 0;

    public MyGridLayoutManager(Context context, int spanCount, int parentWidth) {
        super(context, spanCount);
        this.parentWidth = parentWidth;
        this.spanCount = spanCount;
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        final int widthMode = View.MeasureSpec.getMode(widthSpec);
        final int heightMode = View.MeasureSpec.getMode(heightSpec);
        final int widthSize = View.MeasureSpec.getSize(widthSpec);
        final int heightSize = View.MeasureSpec.getSize(heightSpec);


        int itemCount = getItemCount();
        for (int i = 0; i < itemCount; i++) {
            width = parentWidth / spanCount;
            if (itemCount % spanCount == 0) {
                height = (itemCount / spanCount) * width;
            } else {
                height = (itemCount / spanCount + 1) * width;
            }

        }


        width = widthSize;


        setMeasuredDimension(width, height);
    }
}
