package com.ming.mplugin.proxy;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.support.v7.app.AppCompatActivity;

import com.ming.mplugin.bean.PluginInfo;
import com.ming.mplugin.PluginManager;

public class ProxyActivity extends AppCompatActivity {
	protected static final String TAG = "ProxyActivity";
	
	@Override
	public AssetManager getAssets() {
		String stack = getStackTraceElement();
		PluginInfo mPluginInfo = containsName(stack, PluginManager.getInstance().getPlugsPackageList());
		if (mPluginInfo != null) {
			return mPluginInfo.mAssetManager;
		}
		return super.getAssets();
	}

	@Override
	public Resources getResources() {
		String stack = getStackTraceElement();
//		Logg.i(TAG, stack);
		
		PluginInfo mPluginInfo = containsName(stack, PluginManager.getInstance().getPlugsPackageList());
		if (mPluginInfo != null) {
			return mPluginInfo.mResources;
		}
		return super.getResources();
	}
	
	
	@Override
	public Theme getTheme() {
		String stack = getStackTraceElement();
		PluginInfo mPluginInfo = containsName(stack, PluginManager.getInstance().getPlugsPackageList());
		if (mPluginInfo != null) {
			return mPluginInfo.mTheme;
		}
		return super.getTheme();
	}
	
	@Override
	public ClassLoader getClassLoader() {
		String stack = getStackTraceElement();
		PluginInfo mPluginInfo = containsName(stack, PluginManager.getInstance().getPlugsPackageList());
		if (mPluginInfo != null) {
			return mPluginInfo.mDexClassLoader;
		}
		return super.getClassLoader();
	}
	
	private String getStackTraceElement() {
		StackTraceElement[] temp = Thread.currentThread().getStackTrace();
		
		StringBuilder buf = new StringBuilder();
		for (int i = 4; i < (temp.length-6); i++) {
			StackTraceElement stackTraceElement = temp[i];
			buf.append(stackTraceElement.toString());
			buf.append("<-");
		}
		return buf.toString();
	}
	
	private PluginInfo containsName(String stack, String[] args) {
		for (String string : args) {
			if(stack.contains(string)){
				return PluginManager.getInstance().getPlugInfo(string);
			}
		}
		return null;
	}
	
	@Override
	protected void onDestroy() {
		PluginManager.getInstance().destroy();
		super.onDestroy();
	}
}
