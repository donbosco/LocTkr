package com.your.time.custom.control;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Boscosiva on 01-07-2017.
 */

public class MyExpandableListView extends ExpandableListView {

    private Context context;

    public MyExpandableListView(Context context){
        super(context);
        this.context = context;
    }

    public MyExpandableListView(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        this.context = context;
    }

    public void setOnChildClickListener() {
        OnChildClickListener onChildClickListener = new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                TextView textView = (TextView) (((LinearLayout) v).getChildAt(0));
                //display it or do something with it
                Toast.makeText(context, " Clicked on :: " +textView.getText(), Toast.LENGTH_LONG).show();
                return false;
            }
        };
        super.setOnChildClickListener(onChildClickListener);
    }

    // setOnGroupClickListener listener for group heading click
    public  void setOnGroupClickListener(){
        OnGroupClickListener onGroupClickListener = new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                TextView textView = (TextView) (((LinearLayout) v).getChildAt(0));
                //display it or do something with it
                Toast.makeText(context, " Clicked on :: " +textView.getText(), Toast.LENGTH_LONG).show();
                return false;
            }
        };
    }

    public void activateListeners(){
        setOnGroupClickListener();
        setOnChildClickListener();
    }

}
