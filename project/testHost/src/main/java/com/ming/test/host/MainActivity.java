package com.ming.test.host;

import android.os.Bundle;

import com.ming.mplugin.proxy.ProxyActivity;

public class MainActivity extends ProxyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
