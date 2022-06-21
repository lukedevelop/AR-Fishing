package com.example.arfishing;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class ShopFragment extends Fragment {

    MainActivity mainActivity;

    Button btn_shop_purchase;
    Button btn_shop_sale;
    Button btn_shop_out;

    Button btn_shop_fish;
    Button btn_shop_bait;
    Button btn_shop_interior;



    ListView listView;

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

        btn_shop_purchase = (Button) view.findViewById(R.id.btn_shop_purchase);
        btn_shop_sale = (Button) view.findViewById(R.id.btn_shop_sale);
        btn_shop_out = (Button) view.findViewById(R.id.btn_shop_out);

        btn_shop_fish = (Button) view.findViewById(R.id.btn_shop_fish);
        btn_shop_bait = (Button) view.findViewById(R.id.btn_shop_bait);
        btn_shop_interior = (Button) view.findViewById(R.id.btn_shop_interior);

        listView = view.findViewById(R.id.list_shop);
        showListFish();

        btn_shop_purchase.setEnabled(false);

        btn_shop_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_shop_purchase.setEnabled(false);
                btn_shop_sale.setEnabled(true);

            }
        });

        btn_shop_sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_shop_purchase.setEnabled(true);
                btn_shop_sale.setEnabled(false);
            }
        });

        btn_shop_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        btn_shop_fish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListFish();
            }
        });

        btn_shop_bait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListBait();
            }
        });

        btn_shop_interior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListInterior();
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();

    }

    void showListFish() {


        BaitAdapter adapter = new BaitAdapter();
        DBDAO dbDAO = new DBDAO(mainActivity);
        ArrayList<InventoryDTO> inventoryDTO_arr = dbDAO.selectInventory("fish");
        for(InventoryDTO inventoryDTO: inventoryDTO_arr) {

            adapter.addItem(new BaitItem(
                    inventoryDTO.item_name,
                    inventoryDTO.item_explain,
                    inventoryDTO.item_price,
                    inventoryDTO.item_amount,
                    // TODO - 물고기, 미끼, 인테리어 이미지 설정 필요
                    R.drawable.bait_earthworm));


        }

        listView.setAdapter(adapter);
    }

    void showListBait() {


        BaitAdapter adapter = new BaitAdapter();
        DBDAO dbDAO = new DBDAO(mainActivity);
        ArrayList<InventoryDTO> inventoryDTO_arr = dbDAO.selectInventory("bait");
        for(InventoryDTO inventoryDTO: inventoryDTO_arr) {

            adapter.addItem(new BaitItem(
                    inventoryDTO.item_name,
                    inventoryDTO.item_explain,
                    inventoryDTO.item_price,
                    inventoryDTO.item_amount,
                    // TODO - 물고기, 미끼, 인테리어 이미지 설정 필요
                    R.drawable.bait_earthworm));


        }

        listView.setAdapter(adapter);
    }


    void showListInterior() {


        BaitAdapter adapter = new BaitAdapter();
        DBDAO dbDAO = new DBDAO(mainActivity);
        ArrayList<InventoryDTO> inventoryDTO_arr = dbDAO.selectInventory("interior");
        for(InventoryDTO inventoryDTO: inventoryDTO_arr) {

            adapter.addItem(new BaitItem(
                    inventoryDTO.item_name,
                    inventoryDTO.item_explain,
                    inventoryDTO.item_price,
                    inventoryDTO.item_amount,
                    // TODO - 물고기, 미끼, 인테리어 이미지 설정 필요
                    R.drawable.bait_earthworm));


        }

        listView.setAdapter(adapter);
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



