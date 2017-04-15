package com.ming.test.host;

import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ListView;

import com.ming.mplugin.BasePluginInterface;
import com.ming.mplugin.PluginManager;
import com.ming.mplugin.bean.PluginInfo;
import com.ming.mplugin.proxy.ProxyActivity;
import com.ming.test.host.adapter.PluginAdapter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ProxyActivity {
    private static final String TAG = "MainActivity";
    private MainActivity context;

    /** 设备列表 */
    private ListView lvDevices;
    /** 已连接的设备数据 */
    private List<BasePluginInterface> plugins = new ArrayList<>();
    /** 设备列表适配器 */
    private PluginAdapter pluginAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        initView();
        initTest();
    }

    private void initTest() {
        PluginInfo pluginInfo = null;
        BasePluginInterface plugin = null;
        try {
            pluginInfo = PluginManager.getInstance().loadApk("/sdcard/tmp/plugin1.apk");
            plugin = PluginManager.getInstance().loadPlugin(pluginInfo);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        plugins.add(plugin);
        pluginAdapter.notifyDataSetChanged();
    }

    private void initView() {
        lvDevices = (ListView) findViewById(R.id.lv_plugin);
        pluginAdapter = new PluginAdapter(context);
        pluginAdapter.setData(plugins);
        lvDevices.setAdapter(pluginAdapter);
    }

    @Override
    protected void onDestroy() {
        System.exit(0);
        super.onDestroy();
    }

}
