package com.your.time.custom.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.your.time.custom.listner.IClickListner;
import com.your.time.custom.listner.OnItemClickListenerClass;

public class StringArrayAdapter implements ListAdapter {

    Context context;
    String[] data;
    LayoutInflater inflater;
    int[] itemIds;
    int layoutId;
    String callerIdentifier;

        public StringArrayAdapter(Context context, String[] data,String callerIdentifier, int layoutId) {
            this.context = context;
            this.data = data;
            this.layoutId = layoutId;
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.callerIdentifier = callerIdentifier;
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

            View indexItemView = inflater.inflate(layoutId, null);
            TextView text = (TextView) indexItemView.findViewById(layoutId);
            text.setText(data[position]);
            text.setGravity(Gravity.CENTER);
            text.setTextColor(Color.WHITE);
            text.setOnClickListener(new OnItemClickListenerClass((IClickListner)context,callerIdentifier));
            return text;
        }

        @Override
        public int getItemViewType(int position) {
            return data[position].length();
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