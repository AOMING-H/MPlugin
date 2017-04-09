package com.ming.mplugin;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map.Entry;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.Build;

import com.ming.mplugin.bean.PluginInfo;
import com.ming.mplugin.utils.Logg;

import dalvik.system.DexClassLoader;

/**
 * 插件加载管理器
 * @author Wenming.Huang
 * @date 2016-8-8
 * @version 1.0
 */
public class PluginManager {
	private static final String TAG = "PluginManager";
	/** 插件默认类名*/
	public static final String DEFAULT_CLASS_NAME = "/DefaultPlugin";
	/** App的上下文*/
	private static Context mContext;
	private static PluginManager mPluginManager;
	/** so库存放路径*/
	private String mNativeLibDir = null;
	/** dex存放路径*/
	private String dexOutputPath = null;
	/** App默认的加载器*/
	private ClassLoader localClassLoader;
	/** 插件维护map*/
	private HashMap<String, PluginInfo> mPlugMap = new HashMap<>();

//	private String filePath = getExternalFilesDir("plugin").getAbsolutePath();

	private PluginManager(Context context) {
		if(context == null){
			throw new RuntimeException("should be run initPlugManager(Context context)");
		}
        mContext = context.getApplicationContext();
        dexOutputPath = mContext.getDir("dex", Context.MODE_PRIVATE).getAbsolutePath();
        mNativeLibDir = mContext.getDir("pluginlib", Context.MODE_PRIVATE).getAbsolutePath();
        localClassLoader = mContext.getClassLoader();
	}

	public static PluginManager initPlugManager(Context context){
		if (mPluginManager == null) {
			synchronized (PluginManager.class) {
				if (mPluginManager == null) {
					mPluginManager = new PluginManager(context);
				}
			}
		}
		return mPluginManager;
	}

	public static PluginManager getInstance() {
		return initPlugManager(null);
	}

	public void destroy(){
		mPlugMap.clear();
		mPluginManager = null;
	}

	public PluginInfo loadApk(String plugPath) throws NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		return loadApk(plugPath, true);
    }

	public PluginInfo loadApk(String plugPath, boolean hasSoLib)
			throws NoSuchMethodException, InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		PackageInfo packageInfo = mContext.getPackageManager().getPackageArchiveInfo(
						plugPath, PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES);
        if (packageInfo == null) {
            return null;
        }

        PluginInfo pluginPackage = preparePluginEnv(packageInfo, plugPath);
        if (hasSoLib) {
            // TODO 添加so库支持
        }

        return pluginPackage;
    }

	/**
	 * 解析插件包信息
	 * @param packageInfo 包信息
	 * @param plugPath 插件路径
	 * @return 插件信息
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	private PluginInfo preparePluginEnv(PackageInfo packageInfo, String plugPath)
			throws NoSuchMethodException, InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		// 检查是否加载过
		PluginInfo mPluginInfo = mPlugMap.get(packageInfo.packageName);
		if(mPluginInfo != null){
			return mPluginInfo;
		}
		// 获取对应类加载器
		DexClassLoader mDexClassLoader = new DexClassLoader(plugPath, dexOutputPath, null, localClassLoader);

		// 获取对应资源管理器
        AssetManager assetManager = AssetManager.class.newInstance();
		Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
		addAssetPath.invoke(assetManager, plugPath);

		// 获取对应资源
		Resources superRes = mContext.getResources();
		Resources resources = new Resources(assetManager,
				superRes.getDisplayMetrics(), superRes.getConfiguration());

		// 获取对应主题信息
		Theme superTheme = mContext.getTheme();
		Theme theme = resources.newTheme();
		theme.setTo(superTheme);
		// 给予默认主题，解决主题异常问题
		if (packageInfo.applicationInfo.theme == 0) {
			if (Build.VERSION.SDK_INT >= 14) {
				packageInfo.applicationInfo.theme = android.R.style.Theme_DeviceDefault;
            } else {
            	packageInfo.applicationInfo.theme = android.R.style.Theme;
            }
		}
		theme.applyStyle(packageInfo.applicationInfo.theme, true);

		mPluginInfo = new PluginInfo(mDexClassLoader, packageInfo, resources, theme);
		mPlugMap.put(packageInfo.packageName, mPluginInfo);
		return mPluginInfo;
	}

	/**
	 * 获取当前加载的插件的Map
	 * @return 插件Map
	 */
	public HashMap<String, PluginInfo> getPlugMap() {
		return mPlugMap;
	}

	/**
	 * 获取某个加载插件的信息
	 * @param packageName 对应包名
	 * @return 插件信息
	 */
	public PluginInfo getPlugInfo(String packageName) {
		return mPlugMap.get(packageName);
	}

	/**
	 * 获取当前加载插件包名的列表
	 * @return 插件包名
	 */
	public String[] getPlugsPackageList() {
		String[] packageList = new String[mPlugMap.size()];
		int i = 0;
		for(Entry<String, PluginInfo> entry : mPlugMap.entrySet()){
			packageList[i++] = entry.getKey();
		}
		return packageList;
	}

	/**
	 * 从指定包中获取某个类的实例，无参
	 * @param packageName 包名
	 * @param className 类型
	 * @return 对应类实例
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	@SuppressWarnings("unchecked")
	public <T> T loadClass(String packageName, String className)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException {
		return (T) loadClass(packageName, className, (Object[])null);
	}

	/**
	 * 从指定包中获取某个类的实例
	 * @param packageName 包名
	 * @param className 类型
	 * @param args 构造函数参数
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws NoSuchMethodException
	 */
	@SuppressWarnings("unchecked")
	public <T> T loadClass(String packageName, String className, Object... args)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException {
		Logg.d(TAG, "packageName: " + packageName + ",className: " + className);
		PluginInfo mPluginInfo = getPlugInfo(packageName);
		if(mPluginInfo == null){
			return null;
		}
		Class<?> localClass = mPluginInfo.mDexClassLoader.loadClass(className);

		Class<?>[] mClasss = null;
		if(args != null){
			mClasss = new Class[args.length];
			for (int i = 0; i < args.length; i++) {
				mClasss[i] = args[i].getClass();
			}
		} else {
			mClasss = new Class[]{};
		}
		Constructor<?> localConstructor = localClass.getConstructor(mClasss);
		Object instance = localConstructor.newInstance(args);
		return (T) instance;
	}

	/**
	 * 反射调用封装
	 * @param instance 实例对象
	 * @param name 方法名称
	 * @param args 参数
	 * @return 对应返回值
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T refraction(Object instance, String name, Object... args)
			throws NoSuchMethodException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Class<?>[] mClasss = null;
		if (args != null) {
			mClasss = new Class[args.length];
			for (int i = 0; i < args.length; i++) {
				mClasss[i] = args[i].getClass();
			}
		} else {
			mClasss = new Class[]{};
		}

		Method method = instance.getClass().getDeclaredMethod(name, mClasss);
		method.setAccessible(true);
		return (T) method.invoke(instance, args);
	}
}
 