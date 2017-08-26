package com.your.time.custom.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.your.time.bean.Rest;
import com.your.time.util.Pages;

import java.util.List;

public class CommonArrayAdapter<T extends Rest> extends BaseAdapter {

        Context context;
        List<T> data;
        LayoutInflater inflater;
        Pages page;
        int layoutId;

        public CommonArrayAdapter(Context context,List<T> data, int layoutId,Pages page) {
            super();
            this.context = context;
            this.data = data;
            this.inflater = LayoutInflater.from(context);//LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.layoutId = layoutId;
            this.page = page;
        }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position+1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView==null) {
                convertView = inflater.inflate(layoutId, null);
            }
            data.get(position).adapterMapper(convertView, page,position);

            Log.i(this.getClass().getSimpleName(),"Position : "+position);
            return convertView;
        }
    }