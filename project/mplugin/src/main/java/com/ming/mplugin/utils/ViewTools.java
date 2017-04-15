package com.ming.mplugin.utils;

import java.util.concurrent.atomic.AtomicInteger;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;

public class ViewTools {
	private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

	/**
	 * 动态生成View ID API LEVEL 17 以上View.generateViewId()生成 API LEVEL 17 以下需要手动生成
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public static int generateViewId() {

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
			for (;;) {
				final int result = sNextGeneratedId.get();
				// aapt-generated IDs have the high byte nonzero; clamp to the
				// range under that.
				int newValue = result + 1;
				if (newValue > 0x00FFFFFF)
					newValue = 1; // Roll over to 1, not 0.
				if (sNextGeneratedId.compareAndSet(result, newValue)) {
					return result;
				}
			}
		} else {
			return View.generateViewId();
		}
	}
}
