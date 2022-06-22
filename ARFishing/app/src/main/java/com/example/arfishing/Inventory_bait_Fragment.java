package com.example.arfishing;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class Inventory_bait_Fragment extends Fragment {

    MainActivity mainActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.mainActivity = (MainActivity) context;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_inventory_bait,container,false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btn_inventory_fish_baitFragment = (Button) view.findViewById(R.id.btn_inventory_fish_baitFragment);
        Button btn_inventory_bait_baitFragment = (Button) view.findViewById(R.id.btn_inventory_bait_baitFragment);
        Button btn_inventory_interior_baitFragment = (Button) view.findViewById(R.id.btn_inventory_interior_baitFragment);

        btn_inventory_fish_baitFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_menu, mainActivity.inventory_fish_fragment)
                        .commit();
            }
        });

        btn_inventory_bait_baitFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_menu, mainActivity.inventory_bait_fragment)
                        .commit();
            }
        });

        btn_inventory_interior_baitFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_menu, mainActivity.inventory_interior_fragment)
                        .commit();
            }
        });



        update_Inventory(view);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    void update_Inventory(View view) {
        ListView listView = view.findViewById(R.id.list_inventory_bait);

        BaitAdapter adapter = new BaitAdapter();

        DBDAO dbDAO = new DBDAO(mainActivity);
        ArrayList<InventoryDTO> inventoryDTO_arr = dbDAO.selectInventory("bait");
//        adapter.addItem(new BaitItem("물고기","잡기 확률 10% +", 100,0,R.drawable.gunsaewoo));
        for(InventoryDTO inventoryDTO: inventoryDTO_arr) {
            if(inventoryDTO.item_amount != 0) {
                adapter.addItem(new BaitItem(
                        inventoryDTO.item_name,
                        inventoryDTO.item_explain,
                        inventoryDTO.item_price,
                        inventoryDTO.item_amount,
                        choiceImg(inventoryDTO.item_id)));
            }

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


            baitItemView.tv_baitPrice.setVisibility(View.INVISIBLE);
            baitItemView.btn_baitPurchase.setVisibility(View.INVISIBLE);


            baitItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(mainActivity, item.baitName+"을(를) 장비하였습니다.", Toast.LENGTH_SHORT).show();
                    mainActivity.setBait = item.baitName;

                }
            });

            return baitItemView;

        }

    }

    int choiceImg(int id) {
        int num = R.drawable.bait_gunsaewoo;
        switch(id) {
            case 1:
                num = R.drawable.bait_dduckrice;
                break;
            case 2:
                num = R.drawable.bait_earthworm;
                break;
            case 3:
                num = R.drawable.bait_gunsaewoo;
                break;
            case 4:
                num = R.drawable.bait_lure;
                break;
            case 5:
                num = R.drawable.bait_kingworm;
                break;
        }

        return num;
    }


}
