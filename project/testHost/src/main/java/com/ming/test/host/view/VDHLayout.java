package com.ming.test.host.view;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;

/**
 * Created by zhy on 15/6/3.
 */
public class VDHLayout extends GridView {
	private ViewDragHelper mDragger;

	public VDHLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mDragger = ViewDragHelper.create(this, 1.0f,
				new ViewDragHelper.Callback() {
					@Override
					public boolean tryCaptureView(View child, int pointerId) {
						
						return true;
					}
					
					@Override
					public void onViewCaptured(View capturedChild,
							int activePointerId) {
						super.onViewCaptured(capturedChild, activePointerId);
					}

					@Override
					public int clampViewPositionHorizontal(View child,
							int left, int dx) {
						return left;
					}

					@Override
					public int clampViewPositionVertical(View child, int top,
							int dy) {
						return top;

					}

					@Override
					public int getViewHorizontalDragRange(View child) {
						return getMeasuredWidth() - child.getMeasuredWidth();
					}

					@Override
					public int getViewVerticalDragRange(View child) {
						return getMeasuredHeight() - child.getMeasuredHeight();
					}

				});

	}

    
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		return mDragger.shouldInterceptTouchEvent(event);
	}

    @Override
    public void computeScroll() {
        if(mDragger.continueSettling(true))
        	invalidate();
    }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mDragger.processTouchEvent(event);
		return true;
	}
}
