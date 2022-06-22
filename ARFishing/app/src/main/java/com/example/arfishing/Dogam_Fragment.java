package com.example.arfishing;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class Dogam_Fragment extends Fragment {


    MainActivity mainActivity;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;

        Log.d("프래그먼트 작동 여", "작동 1");
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_dogam, container, false);

        Log.d("프래그먼트 작동 여", "작동 3");
        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d("프래그먼트 작동 여", "작동 4");

        GridView gridView = (GridView) view.findViewById(R.id.gridView1);
        cGridAdapter gridAdapter = new cGridAdapter(mainActivity);
        gridView.setAdapter(gridAdapter);
        TextView gridtext = (TextView) view.findViewById(R.id.gridtext);


        gridView.setAdapter(new cGridAdapter(mainActivity));




        Log.d("프래그먼트 작동 여", "작동 5");

    }

    public class cGridAdapter extends BaseAdapter {



        String [] num = {"001", "002", "003", "004", "005", "006", "007", "008", "009", "010"};

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
                            mainActivity, R.layout.dialog, null);
                    AlertDialog.Builder dlg = new AlertDialog.Builder(mainActivity);
                    ImageView ivPoster = (ImageView) dialogView .findViewById(R.id.imagview);
                    ivPoster.setImageResource(mainActivity.items    [pos]);
                    dlg.setTitle("도감 정보");
                    //     dlg.setIcon(R.drawable.nullimg);
                    dlg.setView(dialogView);
                    dlg.setNegativeButton("닫기", null);

                    dlg.show();
                }
            });

            return imageView;
        }
    }


}


