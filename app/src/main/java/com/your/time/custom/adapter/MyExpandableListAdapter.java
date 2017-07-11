package com.your.time.custom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.your.time.activity.R;
import com.your.time.bean.DetailInfo;
import com.your.time.bean.HeaderInfo;

import java.util.ArrayList;
import java.util.List;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<HeaderInfo> mainList;

    public MyExpandableListAdapter(Context context, List<HeaderInfo> mainList) {
        this.context = context;
        this.mainList = mainList;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<DetailInfo> list =
        mainList.get(groupPosition).getList();
        return list.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View view, ViewGroup parent) {

        DetailInfo detailInfo = (DetailInfo) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater)
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.child_row, null);
        }

        TextView childItem = (TextView) view.findViewById(R.id.childItem);
        childItem.setText(detailInfo.getName().trim());

        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        ArrayList<DetailInfo> productList = 
        mainList.get(groupPosition).getList();
        return productList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mainList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return mainList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view,
                             ViewGroup parent) {

        HeaderInfo headerInfo = (HeaderInfo) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater inf = (LayoutInflater) 
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.group_heading, null);
        }

        TextView heading = (TextView) view.findViewById(R.id.heading);
        heading.setText(headerInfo.getName().trim());

        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}