package com.ming.mplugin.bean;

import dalvik.system.DexClassLoader;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;

import com.ming.mplugin.PluginManager;

/**
 * 插件资源管理
 * @author Wenming.Huang
 * @date 2016-8-8
 * @version 1.0
 */
public class PluginInfo {
	protected static final String TAG = "PluginInfo";
	/** 插件的默认启动的类*/
	public final String mDefaultClass = PluginManager.DEFAULT_CLASS_NAME;
	/** 插件的类加载器*/
	public final DexClassLoader mDexClassLoader;
	/** 插件包信息*/
	public final PackageInfo mPackageInfo;
	/** 插件的资源管理器*/
	public final AssetManager mAssetManager;
	/** 插件的资源*/
	public final Resources mResources;
	/** 插件的主题*/
	public final Theme mTheme;
	
	public PluginInfo(DexClassLoader dexClassLoader,
                      PackageInfo packageInfo, Resources resources, Theme theme) {
		this.mDexClassLoader = dexClassLoader;
		this.mResources = resources;
		this.mPackageInfo = packageInfo;
		this.mAssetManager = resources.getAssets();
		this.mTheme = theme;
	}
	
}
