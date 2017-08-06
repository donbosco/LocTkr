package com.your.time.bean;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.your.time.util.ReflectionUtil;
import com.your.time.util.YourTimeUtil;

/**
 * Created by Boscosiva on 25-06-2017.
 */

public abstract class Rest {
    public View adapterMapper(View layoutView,int[] itemIds,int sno,int resourceId,Rest rest){
        for (int i=0;i < itemIds.length;i++) {
            TextView text = (TextView) layoutView.findViewById(itemIds[i]);
            String viewId = layoutView.getResources().getResourceName(text.getId());
            viewId = viewId.substring(viewId.lastIndexOf("_")+1,viewId.length());
            if(viewId.equals("action")){
                if(resourceId != 0){
                    text.setGravity(Gravity.CENTER);
                }
            }else if(viewId.equals("sno")) {
                text.setText(""+sno);
                text.setGravity(Gravity.LEFT);
            }else if(viewId.equals("waitTime")){
                String string = (String) ReflectionUtil.getValue(rest, viewId);
                string = string == null?"":string.trim();
                //text.setText(string);
                YourTimeUtil.getTimer(text,string).start();
                text.setGravity(Gravity.LEFT);
            }
            text.setTextColor(Color.WHITE);
        }
        return layoutView;
    };
}
