package com.ming.test.host.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ming.mplugin.BasePluginInterface;
import com.ming.mplugin.bean.BasePluginInfo;
import com.ming.mplugin.proxy.ProxyActivity;
import com.ming.mplugin.utils.ViewTools;
import com.ming.test.host.R;

import java.util.List;

/**
 * Created by Wenming.Huang on 2017/4/15.
 */

public class PluginAdapter extends BaseAdapter {
    private static final String TAG = "PluginAdapter";
    private ProxyActivity context;
    private List<BasePluginInterface> plugins;
    private SparseArray<View> mapPlugins = new SparseArray<>();

    public PluginAdapter(ProxyActivity context,List<BasePluginInterface> plugins) {
        this.context = context;
        this.plugins = plugins;
    }

    public void setData(List<BasePluginInterface> plugins) {
        this.plugins.clear();
        this.plugins.addAll(plugins);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return plugins.size();
    }

    @Override
    public BasePluginInterface getItem(int i) {
        return plugins.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (mapPlugins.get(i) == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_plugin_layout, viewGroup, false);
            holder = new ViewHolder();
            holder.llRoot = (LinearLayout) view.findViewById(R.id.item_plugin_root_layout);
            holder.txPluginName = (TextView) view.findViewById(R.id.tx_plugin_id);
            holder.flPluginFragment = new FrameLayout(context);
            holder.flPluginFragment.setId(ViewTools.generateViewId());
            holder.llRoot.addView(holder.flPluginFragment);

            if (plugins.get(i).getFragment() != null) {
                FragmentManager fm = context.getSupportFragmentManager();
                // 开启Fragment事务
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(holder.flPluginFragment.getId(), plugins.get(i).getFragment());
                transaction.commit();
            }
            view.setTag(holder);
            mapPlugins.put(i, view);
        } else {
            view = mapPlugins.get(i);
            holder = (ViewHolder) view.getTag();
        }

        BasePluginInfo device = plugins.get(i).getBasePluginInfo();
        holder.txPluginName.setText(device.getId());
        return view;
    }

    private class ViewHolder {
        private LinearLayout llRoot;
        private TextView txPluginName;
        private FrameLayout flPluginFragment;
    }
}
