package com.your.time.custom.listner;

import android.view.View;

public class OnItemClickListenerClass implements View.OnClickListener
    {
        private String callerIdentifier;
        private IClickListner context;

        public OnItemClickListenerClass(IClickListner context, String callerIdentifier){
            this.callerIdentifier = callerIdentifier;
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            context.onClick(v,callerIdentifier);
        }
    }