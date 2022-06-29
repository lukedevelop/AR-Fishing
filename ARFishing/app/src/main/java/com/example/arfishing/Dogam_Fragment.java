package com.example.arfishing;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class Dogam_Fragment extends Fragment {


    MainActivity mainActivity;
    TextView dogam_name;
    TextView dogam_explain;
    FishDTO caughtFish;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_dogam, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GridView gridView = (GridView) view.findViewById(R.id.gridView1);
        cGridAdapter gridAdapter = new cGridAdapter(mainActivity);
        gridView.setAdapter(gridAdapter);
        gridView.setAdapter(new cGridAdapter(mainActivity));
    }

    public class cGridAdapter extends BaseAdapter {

        public cGridAdapter(MainActivity context){
            mainActivity = context;
        }
        @Override
        public int getCount() {
            return mainActivity.items.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ImageView imageView = new ImageView(mainActivity);
            TextView girdtext = new TextView(mainActivity);
            imageView.setLayoutParams(new GridView.LayoutParams(250, 300));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(5,5,5,5);
            imageView.setImageResource(mainActivity.items[position]);
            final int pos = position;

            if(mainActivity.items[position] == R.drawable.nullimg ) {}

            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    View dialogView = (View) View.inflate(
                            mainActivity, R.layout.dialog_dogam, null);
                    Dialog dlg = new Dialog(mainActivity);

                    dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    ImageView dogam_img = (ImageView) dialogView.findViewById(R.id.imagview);
                    TextView dogam_name2 = (TextView) dialogView.findViewById(R.id.dogam_name);
                    TextView dogam_explain2 = (TextView) dialogView.findViewById(R.id.dogam_explain);
                    dogam_img.setImageResource(mainActivity.items[pos]);

                    switch (mainActivity.items[pos]){
                        case R.drawable.nullimg:
                            dogam_name2.setText("");
                            dogam_explain2.setText("아직 알 수 없다");
                            break;
                        case R.drawable.img_bass:
                            dogam_name2.setText("베스");
                            dogam_explain2.setText("영양만점이지만 맛은 없다.");
                            break;
                        case R.drawable.img_boosiri:
                            dogam_name2.setText("부시리");
                            dogam_explain2.setText("겨울 회에 안성맞춤");
                            break;
                        case R.drawable.img_fishbones:
                            dogam_name2.setText("물고기 뼈");
                            dogam_explain2.setText("앙상하다");
                            break;
                        case R.drawable.img_goldfish:
                            dogam_name2.setText("금붕어");
                            dogam_explain2.setText("작고 귀여운 물고기");
                            break;
                        case R.drawable.img_jellyfish:
                            dogam_name2.setText("해파리");
                            dogam_explain2.setText("보배반점에서 중국냉면을 시키면 나오는 냉채가 이 해파리입니다");
                            break;
                        case R.drawable.img_nimo:
                            dogam_name2.setText("니모");
                            dogam_explain2.setText(" 당신은 아버지가 애타게 찾고 있는 호기심이 가득한 작고 귀여운 아기물고기를 잡으셨군요");
                            break;
                        case R.drawable.img_rock:
                            dogam_name2.setText("돌");
                            dogam_explain2.setText("충분히 딱딱한 돌");
                            break;
                        case R.drawable.img_samsik:
                            dogam_name2.setText("삼식");
                            dogam_explain2.setText("하루 세끼를 먹어서 삼식이다");
                            break;
                        case R.drawable.img_spongebob:
                            dogam_name2.setText("스폰지밥");
                            dogam_explain2.setText("집게리아 진상 손님이자 직원이다.");
                            break;
                        case R.drawable.img_turtle:
                            dogam_name2.setText("거북이");
                            dogam_explain2.setText("이제 막 토끼와의 경주에서 결승선에 도달하여 기뻐할 때 잡힌 거북이다");
                            break;
                    }


                    dlg.setTitle("도감 정보");
                    Log.d("도감 이미지 번호 용", "" + mainActivity.items[pos]);
                    //     dlg.setIcon(R.drawable.nullimg);
                    dlg.setContentView(dialogView);
                    dlg.show();
                }
            });
            return imageView;
        }
    }



}


