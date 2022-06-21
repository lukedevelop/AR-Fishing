package com.example.arfishing;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class QuestFragment extends Fragment {
    QuestItem singleItem;
    MainActivity mainActivity;
    Button click;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity)context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_quest,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = view.findViewById(R.id.list_quest);

        new DBDAO(mainActivity).update_quest_now_DB(0);
        new DBDAO(mainActivity).update_quest_complete_DB();

        // TODO - DB 퀘스트 불러오기

        QuestAdapter adapter = new QuestAdapter();

        DBDAO dbDAO = new DBDAO(mainActivity);
        ArrayList<QuestDTO> questDTO_arr = dbDAO.selectQuest();
//        adapter.addItem(new BaitItem("물고기","잡기 확률 10% +", 100,0,R.drawable.gunsaewoo));
        for(QuestDTO questDTO: questDTO_arr) {
            adapter.addItem(new QuestItem(
                    questDTO.questName,
                    questDTO.questCount,
                    questDTO.questNow,
                    questDTO.questComplete,
                    choiceImg(questDTO.questComplete)
                    ));
        }
        listView.setAdapter(adapter);



    }

    // 1. db quest     questId  questTitle quest

    class QuestAdapter extends BaseAdapter {

        ArrayList<QuestItem> items = new ArrayList<QuestItem>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(QuestItem item){
            items.add(item);
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            QuestItemView singleItemView = null;
            //코드를 재사용할 수 있도록
            if (convertView == null){
                singleItemView = new QuestItemView(mainActivity);
            }else{
                singleItemView = (QuestItemView)convertView;
            }
            QuestItem item = items.get(position);
            singleItemView.setName(item.getQuestName());
            singleItemView.setCount(item.getQuestNow()+" / "+item.getQuestCount());
            singleItemView.setImage(item.resld);
//            if(item.getQuestComplete() == 0) {
//                singleItemView.setImage(R.drawable.lock);
//            } else {
//                singleItemView.setImage(R.drawable.check);
//            }

            return singleItemView;

        }
    }

    int choiceImg(int id) {
        int num = R.drawable.bait_gunsaewoo;
        switch(id) {
            case 0:
                num = R.drawable.lock;
                break;
            case 1:
                num = R.drawable.check;
                break;
        }

        return num;
    }

}

