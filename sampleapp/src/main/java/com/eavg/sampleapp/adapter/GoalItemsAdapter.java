package com.eavg.sampleapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eavg.sampleapp.R;
import com.eavg.sampleapp.model.Item;
import com.eavg.sampleapp.model.Items;

import java.util.List;

/**
 * Created by Eric on 3/25/14.
 */
public class GoalItemsAdapter extends BaseAdapter {

    private Context mContext;
    private List<Item> mItems;
    private LayoutInflater mInflater;
    private String mLanguage;

    public GoalItemsAdapter(Context context, Items items, String language) {
        mContext = context;
        mItems = items.getItems();
        mInflater = LayoutInflater.from(context);
        mLanguage = language;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            switch (getItemViewType(i)) {
                case 0:
                    view = mInflater.inflate(R.layout.goal_items_list_item_ja, viewGroup, false);

                    TextView itemTransliteration = (TextView) view.findViewById(R.id.item_transliterations);
                    viewHolder.itemTransliteration = itemTransliteration;
                    break;
                case 1:
                    view = mInflater.inflate(R.layout.goal_items_list_item, viewGroup, false);
                    break;
                default:
                    view = mInflater.inflate(R.layout.goal_items_list_item_zh, viewGroup, false);
            }
            viewHolder.itemText = (TextView) view.findViewById(R.id.item_text);
            viewHolder.itemMeaning = (TextView) view.findViewById(R.id.item_meaning);
            viewHolder.itemType = (TextView) view.findViewById(R.id.item_type);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Item item = mItems.get(i);

        viewHolder.itemText.setText(item.getText());
        viewHolder.itemType.setText(item.getPartOfSpeech());
        viewHolder.itemMeaning.setText(item.getMeaning());
        if (viewHolder.itemTransliteration != null) {
            viewHolder.itemTransliteration.setText(item.getTransliteration());
        }
        return view;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        Item item = mItems.get(position);

        if (mLanguage.equalsIgnoreCase("ja")) return 0;
        if (mLanguage.equalsIgnoreCase("en")) return 1;
        return 2;
    }

    private static class ViewHolder {
        TextView itemText;
        TextView itemType;
        TextView itemTransliteration;
        TextView itemMeaning;
    }
}
