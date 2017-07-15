package com.your.time.bean;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.your.time.util.ReflectionUtil;

/**
 * Created by Boscosiva on 25-06-2017.
 */

public abstract class Rest {
    public View adapterMapper(View layoutView,int[] itemIds,int sno,int resourceId,Rest rest){
        for (int i=0;i < itemIds.length;i++) {
            TextView text = (TextView) layoutView.findViewById(itemIds[i]);
            String viewId = layoutView.getResources().getResourceName(text.getId());
            viewId = viewId.substring(viewId.indexOf("_")+1,viewId.length());
            if(viewId.equals("action")){
                if(resourceId != 0){
                    text.setGravity(Gravity.CENTER);
                }
            }else if(viewId.equals("sno")) {
                text.setText(""+sno);
                text.setGravity(Gravity.LEFT);
            }else{
                String string = (String) ReflectionUtil.getValue(rest, viewId);
                string = string == null?"":string.trim();
                text.setText(string);
                text.setGravity(Gravity.LEFT);
            }
            text.setTextColor(Color.WHITE);
        }
        return layoutView;
    };
}
