package com.example.arfishing;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class BaitItemView extends LinearLayout {

    ImageView img_bait;
    TextView tv_baitName, tv_baitExplain;
    TextView tv_baitPrice, tv_hasBaitAmount;

    Button btn_baitPurchase;

    public BaitItemView(Context context) {
        super(context);
        init(context);

    }

    public BaitItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    private void init(Context context) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_bait, this, true);

        tv_baitName = (TextView) findViewById(R.id.tv_baitName);
        tv_baitExplain = (TextView) findViewById(R.id.tv_baitExplain);
        tv_baitPrice = (TextView) findViewById(R.id.tv_baitPrice);
        tv_hasBaitAmount = (TextView) findViewById(R.id.tv_hasBaitAmount);
        img_bait = (ImageView) findViewById(R.id.img_bait);
        btn_baitPurchase = (Button) findViewById(R.id.btn_baitPurchase);



    }

    public void setBaitName(String baitName) {
        tv_baitName.setText(baitName);
    }

    public void setBaitExplain(String baitExplain) {
        tv_baitExplain.setText(baitExplain);
    }

    public void setBaitPrice(int baitPrice) {
       tv_baitPrice.setText( "가격: "+baitPrice+"");
    }

    public void setHasBaitAmount(int hasBaitAmount) {
        tv_hasBaitAmount.setText( "보유: "+hasBaitAmount+"");
    }

    public void setBaitImg(int imgBait) {
        img_bait.setImageResource(imgBait);
    }




}

