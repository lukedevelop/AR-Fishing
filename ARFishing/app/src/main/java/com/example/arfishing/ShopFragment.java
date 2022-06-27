package com.example.arfishing;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class ShopFragment extends Fragment {

    MainActivity mainActivity;

    TextView tv_shop_money;

    Button btn_shop_purchase;
    Button btn_shop_sale;

    Button btn_shop_fish;
    Button btn_shop_bait;
    Button btn_shop_interior;

    String nowState = "구매";
    String nowType = "개";
    String type = "bait";
    ListView listView;

    Dialog purchaseDialog;
    Dialog saleDialog;

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

        btn_shop_fish = (Button) view.findViewById(R.id.btn_shop_fish);
        btn_shop_bait = (Button) view.findViewById(R.id.btn_shop_bait);
        btn_shop_interior = (Button) view.findViewById(R.id.btn_shop_interior);

        tv_shop_money = (TextView) view.findViewById(R.id.tv_shop_money);

        update_shop_money_db();


        listView = view.findViewById(R.id.list_shop);
        showListBait();
        nowType = "개";
        type = "bait";

        btn_shop_purchase.setBackgroundResource(R.drawable.btn_shop_top_purchase_selected);
        btn_shop_sale.setBackgroundResource(R.drawable.btn_shop_top_sale_default);

        btn_shop_fish.setBackgroundResource(R.drawable.btn_menu_fish_unlocked);
        btn_shop_bait.setBackgroundResource(R.drawable.btn_menu_bait_selected);
        btn_shop_interior.setBackgroundResource(R.drawable.btn_menu_interior_default);

        btn_shop_fish.setEnabled(false);


        btn_shop_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowState = "구매";
                nowType = "개";
                type = "bait";
                btn_shop_purchase.setEnabled(false);
                btn_shop_sale.setEnabled(true);

                btn_shop_fish.setEnabled(false);
                btn_shop_bait.setEnabled(true);
                btn_shop_interior.setEnabled(true);
                showListBait();

                btn_shop_purchase.setBackgroundResource(R.drawable.btn_shop_top_purchase_selected);
                btn_shop_sale.setBackgroundResource(R.drawable.btn_shop_top_sale_default);

                btn_shop_fish.setBackgroundResource(R.drawable.btn_menu_fish_unlocked);
                btn_shop_bait.setBackgroundResource(R.drawable.btn_menu_bait_selected);
                btn_shop_interior.setBackgroundResource(R.drawable.btn_menu_interior_default);

            }
        });

        btn_shop_sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowState = "판매";
                nowType = "마리";
                type = "fish";
                btn_shop_purchase.setEnabled(true);
                btn_shop_sale.setEnabled(false);

                btn_shop_fish.setEnabled(true);
                btn_shop_bait.setEnabled(false);
                btn_shop_interior.setEnabled(false);
                showListFish();

                btn_shop_purchase.setBackgroundResource(R.drawable.btn_shop_top_purchase_defalut);
                btn_shop_sale.setBackgroundResource(R.drawable.btn_shop_top_sale_selected);

                btn_shop_fish.setBackgroundResource(R.drawable.btn_menu_fish_selected);
                btn_shop_bait.setBackgroundResource(R.drawable.btn_menu_bait_unlocked);
                btn_shop_interior.setBackgroundResource(R.drawable.btn_menu_interior_unlocked);


            }
        });




        btn_shop_fish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListFish();
                nowType = "마리";
                type = "fish";



            }
        });

        btn_shop_bait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListBait();
                nowType = "개";
                type = "bait";

                btn_shop_fish.setBackgroundResource(R.drawable.btn_menu_fish_unlocked);
                btn_shop_bait.setBackgroundResource(R.drawable.btn_menu_bait_selected);
                btn_shop_interior.setBackgroundResource(R.drawable.btn_menu_interior_default);

            }
        });

        btn_shop_interior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListInterior();
                nowType = "개";
                type = "interior";

                btn_shop_fish.setBackgroundResource(R.drawable.btn_menu_fish_unlocked);
                btn_shop_bait.setBackgroundResource(R.drawable.btn_menu_bait_default);
                btn_shop_interior.setBackgroundResource(R.drawable.btn_menu_interior_selected);

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
            if(inventoryDTO.item_amount != 0) {
                adapter.addItem(new BaitItem(
                        inventoryDTO.item_name,
                        inventoryDTO.item_explain,
                        inventoryDTO.item_price,
                        inventoryDTO.item_amount,
                        choiceFishImg(inventoryDTO.item_id)));
            }

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
                    choiceBaitImg(inventoryDTO.item_id)));


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
                    choiceInteriorImg(inventoryDTO.item_id)));


        }

        listView.setAdapter(adapter);
    }

    void update_shop_money_db() {
        DBDAO dbDAO = new DBDAO(mainActivity);
        tv_shop_money.setText(dbDAO.select_money_MemberDB()+"원");
    }

    int get_shop_money_db() {
        DBDAO dbDAO = new DBDAO(mainActivity);
        return dbDAO.select_money_MemberDB();
    }

    void set_shop_money_db(int money) {
        DBDAO dbDAO = new DBDAO(mainActivity);
        dbDAO.update_money_MemberDB(money);
    }

    int get_possible_purchase_amount(int price) {
        DBDAO dbDAO = new DBDAO(mainActivity);
        int money = dbDAO.select_money_MemberDB();

        return money/price;
    }

    int get_possible_sale_amount(String itemName) {
        DBDAO dbDAO = new DBDAO(mainActivity);
        int amount = dbDAO.select_Inventory_amount_DB(type,itemName);

        return amount;
    }

    void set_purchase_item_amount(String type, String itemName,int amount) {
        DBDAO dbDAO = new DBDAO(mainActivity);
        dbDAO.update_Inventory_amount_DB(
                type,
                itemName,
                dbDAO.select_Inventory_amount_DB(type,itemName)+amount
        );
    }

    void set_sale_item_amount(String type, String itemName,int amount) {
        DBDAO dbDAO = new DBDAO(mainActivity);
        dbDAO.update_Inventory_amount_DB(
                type,
                itemName,
                dbDAO.select_Inventory_amount_DB(type,itemName) - amount
        );
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

            // 구매
            if (nowState.equals("구매")) {
                baitItemView.setBaitName(item.baitName);
                baitItemView.setBaitExplain(item.baitExplain);
                baitItemView.setBaitPrice(item.baitPrice);
                baitItemView.setHasBaitAmount(item.hasBaitAmount);
                baitItemView.setBaitImg(item.baitId);

                baitItemView.btn_baitPurchase.setBackgroundResource(R.drawable.btn_shop_top_purchase_selected);

                baitItemView.btn_baitPurchase.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        show_purchase_shop_Dialog(
                                item.baitName,
                                get_possible_purchase_amount(item.baitPrice),
                                item.baitPrice
                        );

                    }
                });

            // 판매
            } else {
                baitItemView.setBaitName(item.baitName);
                baitItemView.setBaitExplain(item.baitExplain);
                baitItemView.setBaitPrice(item.baitPrice);
                baitItemView.setHasBaitAmount(item.hasBaitAmount);
                baitItemView.setBaitImg(item.baitId);

                baitItemView.btn_baitPurchase.setBackgroundResource(R.drawable.btn_shop_top_sale_selected);
                baitItemView.btn_baitPurchase.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        show_sale_shop_Dialog(
                                item.baitName,
                                get_possible_sale_amount(item.baitName),
                                item.baitPrice

                        );
                    }
                });
            }


            return baitItemView;

        }

    }


    void show_purchase_shop_Dialog(String itemName, int purchaseAmount, int itemPrice ) {
        mainActivity.runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                purchaseDialog = new Dialog(mainActivity);
                purchaseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                purchaseDialog.setContentView(R.layout.dialog_shop_purchase);

                TextView dialog_shop_purchase_explain = (TextView) purchaseDialog.findViewById(R.id.dialog_shop_purchase_explain);
                TextView dialog_shop_purchase_amount = (TextView) purchaseDialog.findViewById(R.id.dialog_shop_purchase_amount);
                SeekBar seekBar_shop_purchase = (SeekBar) purchaseDialog.findViewById(R.id.seekBar_shop_purchase);

                TextView dialog_shop_purchase_ok = (TextView) purchaseDialog.findViewById(R.id.dialog_shop_purchase_ok);
                TextView dialog_shop_purchase_cancel = (TextView) purchaseDialog.findViewById(R.id.dialog_shop_purchase_cancel);

                dialog_shop_purchase_explain.setText(itemName+" 몇 "+nowType+"를 구매하시겠습니까?");

                seekBar_shop_purchase.setMax(purchaseAmount);

                seekBar_shop_purchase.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                                dialog_shop_purchase_amount.setText(progress+"");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                dialog_shop_purchase_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int amount = seekBar_shop_purchase.getProgress();
                        // 머니 업데이트
                        set_shop_money_db(
                                get_shop_money_db() - ( amount * itemPrice )

                        );
                        // 갯수 업데이트
                        set_purchase_item_amount(type,itemName, amount);

                        // 구매 후 텍스트 업데이트
                        update_shop_money_db();
                        if(type.equals("bait")) {
                            showListBait();
                        } else {
                            showListInterior();
                        }
                        purchaseDialog.dismiss();
                    }
                });

                dialog_shop_purchase_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        purchaseDialog.dismiss();
                    }
                });



                purchaseDialog.show();


            }
        });

    }

    void show_sale_shop_Dialog(String itemName, int saleAmount, int itemPrice ) {
        mainActivity.runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                saleDialog = new Dialog(mainActivity);
                saleDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                saleDialog.setContentView(R.layout.dialog_shop_sale);

                TextView dialog_shop_sale_explain = (TextView) saleDialog.findViewById(R.id.dialog_shop_sale_explain);
                TextView dialog_shop_sale_amount = (TextView) saleDialog.findViewById(R.id.dialog_shop_sale_amount);
                SeekBar seekBar_shop_sale = (SeekBar) saleDialog.findViewById(R.id.seekBar_shop_sale);

                TextView dialog_shop_sale_ok = (TextView) saleDialog.findViewById(R.id.dialog_shop_sale_ok);
                TextView dialog_shop_sale_cancel = (TextView) saleDialog.findViewById(R.id.dialog_shop_sale_cancel);

                dialog_shop_sale_explain.setText(itemName+" 몇 "+nowType+"를 판매하시겠습니까?");

                seekBar_shop_sale.setMax(saleAmount);

                seekBar_shop_sale.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        dialog_shop_sale_amount.setText(progress+"");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                dialog_shop_sale_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int amount = seekBar_shop_sale.getProgress();
                        // 머니 업데이트
                        set_shop_money_db(
                                get_shop_money_db() + ( amount * itemPrice )

                        );
                        // 갯수 업데이트
                        set_sale_item_amount(type,itemName, amount);

                        // 판매 후 텍스트 업데이트
                        update_shop_money_db();
                        if(type.equals("fish")) {
                            showListFish();
                        }
                        saleDialog.dismiss();
                    }
                });

                dialog_shop_sale_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saleDialog.dismiss();
                    }
                });



                saleDialog.show();


            }
        });

    }


    int choiceFishImg(int id) {
        int num = R.drawable.bait_gunsaewoo;
        switch(id) {
            case 1:
                num = R.drawable.img_bass;
                break;
            case 2:
                num = R.drawable.img_boosiri;
                break;
            case 3:
                num = R.drawable.img_fishbones;
                break;
            case 4:
                num = R.drawable.img_goldfish;
                break;
            case 5:
                num = R.drawable.img_jellyfish;
                break;
            case 6:
                num = R.drawable.img_nimo;
                break;
            case 7:
                num = R.drawable.img_rock;
                break;
            case 8:
                num = R.drawable.img_samsik;
                break;
            case 9:
                num = R.drawable.img_spongebob;
                break;
            case 10:
                num = R.drawable.img_turtle;
                break;
        }

        return num;
    }

    int choiceBaitImg(int id) {
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

    int choiceInteriorImg(int id) {
        int num = R.drawable.bait_gunsaewoo;
        switch(id) {
            case 1:
                num = R.drawable.interior_zogae;
                break;
        }

        return num;
    }


}



