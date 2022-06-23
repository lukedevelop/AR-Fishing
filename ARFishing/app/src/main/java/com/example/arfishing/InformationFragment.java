package com.example.arfishing;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class InformationFragment extends Fragment {

    MainActivity mainActivity;

    TextView tv_nickName_information, tv_catchFish_information, tv_hasFish_information, tv_money_information;

    ImageView level;
    TextView thitle;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.mainActivity = (MainActivity) context;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_information,container,false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_nickName_information = (TextView) view.findViewById(R.id.tv_nickName_information);
        tv_catchFish_information = (TextView) view.findViewById(R.id.tv_catchFish_information);
        tv_hasFish_information = (TextView) view.findViewById(R.id.tv_hasFish_information);
        tv_money_information = (TextView) view.findViewById(R.id.tv_money_information);

        level = (ImageView) view.findViewById(R.id.level);
        thitle = (TextView) view.findViewById(R.id.thitle);

        updateDB_InformationFragment();

    }

    @Override
    public void onResume() {
        super.onResume();


    }

    void updateDB_InformationFragment() {
        DBDAO dbDAO = new DBDAO(mainActivity);
        InformationDTO informationDTO = dbDAO.selectMemberDB();
//        Log.d("야호",informationDTO.nickName+"");
        Toast.makeText(mainActivity, informationDTO.nickName,Toast.LENGTH_SHORT).show();
        tv_nickName_information.setText(informationDTO.nickName);
        tv_catchFish_information.setText(informationDTO.catchFish+"");
        tv_hasFish_information.setText(informationDTO.hasFish+"");
        tv_money_information.setText(informationDTO.money+"");
        setChinghoImg();
        // setText에 int면 +"" 반드시 추가

    }

    void setChinghoImg() {
        int dbcnt = new DBDAO(mainActivity).select_catchFish_MemberDB();

        if(dbcnt < 5){
            level.setImageResource(R.drawable.rank0);
            thitle.setText("강호동 닥터피쉬");
        }else if(dbcnt >=5 && dbcnt <10){
            level.setImageResource(R.drawable.rank1);
            thitle.setText("성수동 냥냥펀치");
        } else if(dbcnt >= 10 && dbcnt < 15){
            level.setImageResource(R.drawable.rank2);
            thitle.setText("봉천동 피바라기");

        }else if(dbcnt >= 15 && dbcnt < 20){
            level.setImageResource(R.drawable.rank3);

            thitle.setText("천천동 불주먹");

        } else if (dbcnt >= 20 && dbcnt < 50) {
            level.setImageResource(R.drawable.rank4);

            thitle.setText("봉담읍 탈곡기");

        } else {
            level.setImageResource(R.drawable.rank5);
            thitle.setText("서초동 캡사이신");
        }

    }


}

