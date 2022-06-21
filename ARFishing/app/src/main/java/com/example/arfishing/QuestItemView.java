package com.example.arfishing;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class QuestItemView extends LinearLayout {

    TextView tv_list_questName, tv_list_questCount;
    ImageView img_quest_Complete;

    public QuestItemView(Context context) {
        super(context);
        init(context);
    }

    public QuestItemView(Context context, @Nullable AttributeSet attrs){
        super(context,attrs);

    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.item_quest, this, true);

        tv_list_questName = findViewById(R.id.tv_list_questName);
        tv_list_questCount = findViewById(R.id.tv_list_questCount);
        img_quest_Complete = findViewById(R.id.img_quest_Complete);

    }

    public void setName(String name){
        tv_list_questName.setText(name);
    }

    public void setCount(String count){
        tv_list_questCount.setText(count);
    }

    public void setImage(int resld){
        img_quest_Complete.setImageResource(resld);
    }


}
