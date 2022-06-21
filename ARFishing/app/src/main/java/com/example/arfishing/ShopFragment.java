package com.example.arfishing;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class ShopFragment extends Fragment {

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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_shop,container,false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = view.findViewById(R.id.list_bait);

        BaitAdapter adapter = new BaitAdapter();



        adapter.addItem(new BaitItem("갯지렁이","잡기 확률 10% +", 100,0,R.drawable.bait_earthworm));
        adapter.addItem(new BaitItem("왕꿈틀이","잡기 확률 20% +", 300, 0, R.drawable.bait_kingworm));
        adapter.addItem(new BaitItem("루어","잡기 확률 30% +", 500, 0, R.drawable.bait_lure));
        adapter.addItem(new BaitItem("건새우","잡기 확률 50% +", 1000, 0, R.drawable.bait_gunsaewoo));

        listView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    class BaitAdapter extends BaseAdapter {

        ArrayList<BaitItem> items = new ArrayList<>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(BaitItem item) {
            items.add(item);
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            BaitItemView baitItemView = null;
            if(convertView == null) {
                baitItemView = new BaitItemView(mainActivity);
            } else {
                baitItemView = (BaitItemView) convertView;
            }

            BaitItem item = items.get(position);


            baitItemView.setBaitName(item.baitName);
            baitItemView.setBaitExplain(item.baitExplain);
            baitItemView.setBaitPrice(item.baitPrice);
            baitItemView.setHasBaitAmount(item.hasBaitAmount);
            baitItemView.setBaitImg(item.baitId);

            baitItemView.btn_baitPurchase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // TODO - 구매 로직 짜라 찬욱아
                    Toast.makeText(mainActivity, item.baitName, Toast.LENGTH_SHORT).show();
                }
            });

            return baitItemView;

        }

    }



}



