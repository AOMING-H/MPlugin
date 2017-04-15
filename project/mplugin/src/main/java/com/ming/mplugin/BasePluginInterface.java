package com.ming.mplugin;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.ming.mplugin.bean.BasePluginInfo;

/**
 * 插件操作基本接口
 * Created by Wenming.Huang on 2017/4/15.
 */
public interface BasePluginInterface {

	public boolean initialize(Context mContext, Object obj);

	public boolean finish();

    public BasePluginInfo getBasePluginInfo();

	public Fragment getFragment();
}
