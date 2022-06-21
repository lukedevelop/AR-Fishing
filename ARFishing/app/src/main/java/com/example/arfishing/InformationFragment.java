package com.example.arfishing;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class InformationFragment extends Fragment {

    MainActivity mainActivity;

    TextView tv_nickName_information, tv_catchFish_information, tv_hasFish_information, tv_money_information;

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
        // setText에 int면 +"" 반드시 추가

    }
}

