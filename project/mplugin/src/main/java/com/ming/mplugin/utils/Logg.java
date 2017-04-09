package com.ming.mplugin.utils;

import android.util.Log;

public class Logg {
	private static final boolean showDebug = true;
	private static final boolean showInfo = true;
	private static final boolean showWarn = true;
	private static final boolean showErr = true;
	
	public static void d(String tag, String msg) {
		if(showDebug){
			Log.d(tag, msg);
		}
	}

	public static void i(String tag, int msg) {
		if(showInfo){
			Log.i(tag, msg + "");
		}
	}
	
	public static void i(String tag, String msg) {
		if(showInfo){
			Log.i(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if(showWarn){
			Log.w(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if(showErr){
			Log.e(tag, msg);
		}
	}
}
