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

import java.util.regex.Pattern;

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
                if(nickname(editText_nickName.getText().toString())) {
                    Toast.makeText(mainActivity, "닉네임 -한글 2~8자리로 구성되어야 합니다.", Toast.LENGTH_SHORT).show();

                } else if(!(editText_nickName.getText().toString()).equals("")) {
                    mainActivity.mainFrameLayout.setVisibility(View.VISIBLE);
                    mainActivity.mainFrameLayout.setClickable(true);
                    mainActivity.subFrameLayout.setVisibility(View.INVISIBLE);
                    mainActivity.subFrameLayout.setClickable(false);

                    mainActivity.getSupportFragmentManager()
                            .beginTransaction()
                            .remove(mainActivity.main_fragment).commit();


                    new DBDAO(mainActivity).firstMemberDB(editText_nickName.getText().toString());

                } else {
                    Toast.makeText(mainActivity, "닉네임을 입력해주세요!", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    public boolean nickname(String nickname) {
        boolean res = true;
        String nick = ""+nickname;
        String nick_regex = "^[ㄱ-ㅎㅏ-ㅣ가-힣]{2,8}$";
        char[] concon = nick.toCharArray();
        if (concon.length > 1 && concon.length < 9 && Pattern.matches(nick_regex, nickname) ) {
            res = false;
        }
        return res;
    }



}
