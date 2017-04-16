package com.ming.test.host;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.ming.mplugin.BasePluginInterface;
import com.ming.mplugin.PluginManager;
import com.ming.mplugin.bean.PluginInfo;
import com.ming.mplugin.proxy.ProxyActivity;
import com.ming.test.host.adapter.PluginAdapter;

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

    // 要申请的权限
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        initView();
        checkPermission();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                showDialogTipUserRequestPermission();
            } else {
                initTest();
            }
        }
    }
    // 提示用户该请求权限的弹出框
    private void showDialogTipUserRequestPermission() {

        new AlertDialog.Builder(this)
                .setTitle("存储权限不可用")
                .setMessage("存储权限不可用")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startRequestPermission();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
    }

    // 开始提交请求权限
    private void startRequestPermission() {
        ActivityCompat.requestPermissions(this, permissions, 321);
    }

    // 用户权限 申请 的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    finish();
                } else {
                    initTest();
                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void initTest() {
        PluginInfo pluginInfo = null;
        BasePluginInterface plugin = null;
        try {
            pluginInfo = PluginManager.getInstance().loadApk("/sdcard/tmp/plugin1.apk");
            if(pluginInfo != null){
                plugin = PluginManager.getInstance().loadPlugin(pluginInfo);
                plugins.add(plugin);
                pluginAdapter.notifyDataSetChanged();
            } else {
                Log.e(TAG,"not found /sdcard/tmp/plugin1.apk");
            }
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
    }

    private void initView() {
        lvDevices = (ListView) findViewById(R.id.lv_plugin);
        pluginAdapter = new PluginAdapter(context, plugins);
        lvDevices.setAdapter(pluginAdapter);
    }

    @Override
    protected void onDestroy() {
        System.exit(0);
        super.onDestroy();
    }

}
