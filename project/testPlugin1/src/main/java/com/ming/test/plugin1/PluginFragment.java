package com.ming.test.plugin1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ming.mplugin.bean.BasePluginInfo;

public class PluginFragment extends Fragment implements View.OnClickListener{
	private static final String TAG = "PluginFragment";

	private TextView tvID;
	private TextView tvVersion;

	private Button btn1;
	private Button btn2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(TAG, "onCreateView");
		View view = inflater.inflate(R.layout.fragment_plugin, container, false);
		initView(view);

		return view;
	}
	
	private void initView(View view) {
		tvID = (TextView) view.findViewById(R.id.tv_plugin_id);
		tvVersion = (TextView) view.findViewById(R.id.tv_plugin_version);
		btn1 = (Button) view.findViewById(R.id.btn_plugin_1);
		btn2 = (Button) view.findViewById(R.id.btn_plugin_2);

		btn1.setOnClickListener(this);
		btn1.setOnClickListener(this);

		tvID.setText("设备ID："+mBaseDeviceInfo.getId());
		tvVersion.setText("设备版本：" + mBaseDeviceInfo.getVersion());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_plugin_1:
			Log.i(TAG, "btn_plugin_1");
			break;
		case R.id.btn_plugin_2:
			Log.i(TAG, "btn_plugin_2");
			break;
		default:
			break;
		}
	}
	private BasePluginInfo mBaseDeviceInfo;
	public void setDeviceDriver(BasePluginInfo mBaseDeviceInfo) {
		this.mBaseDeviceInfo = mBaseDeviceInfo;
	}
}
