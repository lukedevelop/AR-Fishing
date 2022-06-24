package com.example.arfishing;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MainFragment extends Fragment {

    MainActivity mainActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.mainActivity = (MainActivity) context;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main,container,false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText editText_nickName = (EditText) view.findViewById(R.id.editText_nickName);


        Button btn_goGameStart = (Button) view.findViewById(R.id.btn_goGameStart);
        btn_goGameStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(editText_nickName.getText().toString()).equals("")) {
                    mainActivity.mainFrameLayout.setVisibility(View.VISIBLE);
                    mainActivity.mainFrameLayout.setClickable(true);
                    mainActivity.subFrameLayout.setVisibility(View.INVISIBLE);
                    mainActivity.subFrameLayout.setClickable(false);

                    mainActivity.getSupportFragmentManager()
                            .beginTransaction()
                            .remove(mainActivity.main_fragment).commit();


                    // TODO 추가 DB에 member nickname 입력 로직

                    new DBDAO(mainActivity).firstMemberDB(editText_nickName.getText().toString());
                } else {
                    Toast.makeText(mainActivity, "닉네임을 입력해주세요!", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }






}
