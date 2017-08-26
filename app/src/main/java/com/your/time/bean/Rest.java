package com.your.time.bean;

import android.view.View;

import com.your.time.util.Pages;

/**
 * Created by Boscosiva on 25-06-2017.
 */

public abstract class Rest {
    public abstract <T extends Rest> View adapterMapper(View layoutView, Pages page,int position);//{
        /*for (int i=0;i < itemIds.length;i++) {
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
                String string = ReflectionUtil.getValue(rest, viewId);
                string = string == null?"":string.trim();
                //text.setText(string);
                YourTimeUtil.getTimer(text,string).start();
                text.setGravity(Gravity.LEFT);
            }else if(viewId.equals("status")) {

            }else{
                String string = ReflectionUtil.getValue(rest, viewId);
                string = string == null?"nothing":string.trim();
                text.setText(string);
            }
            //text.setTextColor(Color.BLACK);
        }*/
        //return layoutView;
    //}
}
