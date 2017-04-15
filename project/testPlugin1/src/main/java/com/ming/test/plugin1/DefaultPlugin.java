package com.ming.test.plugin1;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.ming.mplugin.BasePluginInterface;
import com.ming.mplugin.bean.BasePluginInfo;

/**
 * Created by Wenming.Huang on 2017/4/15.
 */

public class DefaultPlugin implements BasePluginInterface {
    private static final String TAG = "DefaultPlugin";
    private Context context;
    private BasePluginInfo mBaseDeviceInfo = new BasePluginInfo();

    public DefaultPlugin(){
        mBaseDeviceInfo.setId("TestPlugin1");
        mBaseDeviceInfo.setVersion(1);
    }

    @Override
    public boolean initialize(Context mContext, Object obj) {
        return false;
    }

    @Override
    public boolean finish() {
        return false;
    }

    @Override
    public BasePluginInfo getBasePluginInfo() {
        return mBaseDeviceInfo;
    }

    @Override
    public Fragment getFragment() {
        return null;
    }
}
