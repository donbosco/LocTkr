package com.your.time.custom.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.your.time.bean.Rest;

public class CommonArrayAdapter<T extends Rest> implements ListAdapter {

        Context context;
        T[] data;
        LayoutInflater inflater;
        int[] itemIds;
        int layoutId;

        public CommonArrayAdapter(Context context,T[] data, int layoutId,int[] items) {
            this.context = context;
            this.data = data;
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.layoutId = layoutId;
            this.itemIds = items;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public int getCount() {
            return data.length;
        }

        @Override
        public Object getItem(int position) {
            return data[position];
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View layoutView = inflater.inflate(layoutId, null);
            if(data[position].getClass().equals(String.class)){

            }else {
                data[position].adapterMapper(layoutView, itemIds, position + 1, 0,data[position]);
            }
            return layoutView;
        }

        @Override
        public int getItemViewType(int position) {
            return data.length;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return data.length==0;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int position) {
            return false;
        }
    }