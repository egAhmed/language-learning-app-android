package com.eavg.sampleapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eavg.sampleapp.R;
import com.eavg.sampleapp.model.DrawerItem;

import java.util.ArrayList;

/**
 * Created by Eric on 3/18/14.
 */
public class DrawerListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<DrawerItem> mDrawerItems;

    public DrawerListAdapter(Context context, ArrayList<DrawerItem> drawerItems) {
        mContext = context;
        mDrawerItems = drawerItems;
    }

    @Override
    public int getCount() {
        return mDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mDrawerItems.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        boolean isListItem = this.getItemViewType(i) == 0;
        if (view == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (isListItem)
                view = mInflater.inflate(R.layout.drawer_list_item, viewGroup, false);
            else {
                view = mInflater.inflate(R.layout.drawer_separator, viewGroup, false);
                return view;
            }
        }

        if (!isListItem)
            return view;

        ImageView imageView = (ImageView)view.findViewById(R.id.drawer_item_icon);
        TextView textView = (TextView)view.findViewById(R.id.drawer_item_text);

        DrawerItem drawerItem = mDrawerItems.get(i);
        imageView.setImageResource(drawerItem.getIcon());
        textView.setText(drawerItem.getTitle());

        return view;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0 || position == 4) ? 1 : 0;
    }
}
