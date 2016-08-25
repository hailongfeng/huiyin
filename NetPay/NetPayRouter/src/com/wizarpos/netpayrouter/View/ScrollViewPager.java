/**
 * 
 */
package com.wizarpos.netpayrouter.View;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author harlen
 * @date 2015年11月19日 下午1:33:55
 */
public class ScrollViewPager extends ViewPager {
	private boolean isCanScroll = false;
	/**
	 * @param context
	 * @param attrs
	 */
	public ScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScrollViewPager(Context context) {
		super(context);
	}
	
	public void setScanScroll(boolean isCanScroll) {
		this.isCanScroll = isCanScroll;
	}

	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(x, y);
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		if (isCanScroll) {
			return super.onTouchEvent(arg0);
		} else {
			return false;
		}

	}

	@Override
	public void setCurrentItem(int item, boolean smoothScroll) {
		super.setCurrentItem(item, smoothScroll);
	}

	@Override
	public void setCurrentItem(int item) {
		super.setCurrentItem(item);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if (isCanScroll) {
			return super.onInterceptTouchEvent(arg0);
		} else {
			return false;
		}

	}
	

}
