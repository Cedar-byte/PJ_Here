package com.yuntong.here.view;

import android.R.attr;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;


public class GalleryFlow extends Gallery {
	private ViewPager mViewPager;
	private Camera mCamera;
	private int mPaddingLeft;
	private int offsetX = 0;

	public GalleryFlow(Context context) {
		super(context);
		mCamera = new Camera();
		this.setStaticTransformationsEnabled(true);
	}

	public GalleryFlow(Context context, AttributeSet attrs) {
		super(context, attrs);
		mCamera = new Camera();
		setAttributesValue(context, attrs);
		this.setStaticTransformationsEnabled(true);
	}

	public GalleryFlow(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mCamera = new Camera();
		setAttributesValue(context, attrs);
		this.setStaticTransformationsEnabled(true);
	}

	private void setAttributesValue(Context context, AttributeSet attrs) {
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				new int[] { attr.paddingLeft });
		mPaddingLeft = typedArray.getDimensionPixelSize(0, 0);
		typedArray.recycle();
	}

	// int jianju = 5;
	int jianju = 0;

	@Override
	protected boolean getChildStaticTransformation(View child, Transformation t) {
		t.clear();
		t.setTransformationType(Transformation.TYPE_MATRIX);
		mCamera.save();
		final Matrix imageMatrix = t.getMatrix();
//		if (0 == offsetX) {
//			offsetX = getChildAt(0).getLeft() - jianju;
//		}
		mCamera.translate(offsetX, 0f, 0f);
		mCamera.getMatrix(imageMatrix);
		mCamera.restore();
		return true;
	}

	public ViewPager getmViewPager() {
		return mViewPager;
	}

	public void setmViewPager(ViewPager mViewPager) {
		this.mViewPager = mViewPager;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

//		mViewPager.requestDisallowInterceptTouchEvent(true);
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

//		mViewPager.requestDisallowInterceptTouchEvent(true);
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		event.offsetLocation(-offsetX, 0);

//		mViewPager.requestDisallowInterceptTouchEvent(true);
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		int keyCode;
		if (isScrollingLeft(e1, e2)) {
			keyCode = KeyEvent.KEYCODE_DPAD_LEFT;
		} else {
			keyCode = KeyEvent.KEYCODE_DPAD_RIGHT;
		}
		onKeyDown(keyCode, null);
		return true;
	}
	
	private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
		return e2.getX() > e1.getX();
	}

//	@Override
//	protected void onLayout(boolean changed, int l, int t, int r, int b) {
//		if (getAdapter() != null)
//			super.onLayout(changed, l, t, r, b);
//	}

}
