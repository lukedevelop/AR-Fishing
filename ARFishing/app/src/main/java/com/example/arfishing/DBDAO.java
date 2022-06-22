package com.example.arfishing;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBDAO {

    MyDB myDB;
    SQLiteDatabase db;

    DBDAO(Context context ) {
        if(myDB == null) {
            myDB = new MyDB(context);
        }
    }

    void firstMemberDB(String nickName) {
        db = myDB.getWritableDatabase();
        db.execSQL("update member set " +
                "nickname= '"+ nickName + "' " +
                " where id = 1");
    }


    InformationDTO selectMemberDB() {
        db = myDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from member",null);
        InformationDTO informationDTO = new InformationDTO("ㅇㅇ",1,1,0);
        while(cursor.moveToNext()) {
            informationDTO = new InformationDTO(
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getInt(4)
            );
        }

        db.close();

        return informationDTO;
    }

    int select_money_MemberDB() {
        int money = 0;
        db = myDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("select money from member",null);
        while(cursor.moveToNext()) {
            money = cursor.getInt(0);
        }

        db.close();

        return money;
    }

    int select_catchFish_MemberDB() {
        int amount = 0;
        db = myDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("select catchfish from member",null);
        while(cursor.moveToNext()) {
            amount = cursor.getInt(0);
        }

        db.close();

        return amount;
    }

    int select_hasFish_MemberDB() {
        int amount = 0;
        db = myDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("select hasfish from member",null);
        while(cursor.moveToNext()) {
            amount = cursor.getInt(0);
        }

        db.close();

        return amount;
    }


    void update_money_MemberDB(int money) {
        db = myDB.getReadableDatabase();
        db.execSQL("update member set money = " +
                money +
                " where id = 1"
        );

        db.close();
    }

    void updateMemberDB(String which,String nickName, int catchFish, int hasFish, int money) {
        db = myDB.getWritableDatabase();
        switch (which) {
            case "All":
                db.execSQL("update member set " +
                        "nickname= '" + nickName + "' " +
                        "catchfish=" + catchFish +
                        "hasFish=" + hasFish +
                        " where id = 1"
                );
                break;
            case "nickName":
                db.execSQL("update member set " +
                        "nickname= '"+ nickName + "' " +
                        " where id = 1"
                );
                break;
            case "catchFish":
                db.execSQL("update member set " +
                        "catchfish=" + catchFish +
                        " where id = 1"
                );
                break;
            case "hasFish":
                db.execSQL("update member set " +
                        "hasfish=" + hasFish +
                        " where id = 1"
                );
                break;
            case "money":
                db.execSQL("update member set " +
                        "money=" + money +
                        " where id = 1"
                );
                break;
        }

        db.close();
    }

    int select_Inventory_amount_DB(String type, String itemName) {
        int amount = 0;
        // 지금 가지고 있는 양 가져오기
        db = myDB.getReadableDatabase();
        Cursor cursor = db.rawQuery( "select " + type + "_amount from inventory_" + type + " where " + type + "_name = '" + itemName + "'"
                ,null);
        while(cursor.moveToNext()) {
            amount = cursor.getInt(0);
        }
        return amount;
    }

    void update_Inventory_amount_DB(String type, String itemName, int itemAmount) {
        db = myDB.getReadableDatabase();
        db.execSQL("update inventory_"+ type +
                " set " + type+"_amount = " + itemAmount +
                " where " + type + "_name = '" +
                itemName + "'"
        );

        db.close();


    }

    ArrayList<InventoryDTO> selectInventory(String name) {
        ArrayList<InventoryDTO> inventoryDTO_arr = new ArrayList<>();
        db = myDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from inventory_"+name,null);
        InventoryDTO inventoryDTO = new InventoryDTO(0,"1","1",0,0);
        while(cursor.moveToNext()) {
            inventoryDTO = new InventoryDTO(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getInt(4)
            );
            inventoryDTO_arr.add(inventoryDTO);
        }

        db.close();

        return inventoryDTO_arr;


    }

    ArrayList<QuestDTO> selectQuest() {
        ArrayList<QuestDTO> questDTO_arr = new ArrayList<>();
        db = myDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from quest",null);
        QuestDTO questDTO = new QuestDTO("",0,0,0);
        while(cursor.moveToNext()) {
            questDTO = new QuestDTO(
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getInt(4)
            );
            questDTO_arr.add(questDTO);
        }

        db.close();

        return questDTO_arr;
    }

    FishDTO getFishInfo(String name){
        FishDTO fishDTO = new FishDTO();

        db = myDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from fish where fish_name = '"+name+"'", null);
        while(cursor.moveToNext()){
            fishDTO.fish_id = cursor.getInt(0);
            fishDTO.fish_name = cursor.getString(1);
            fishDTO.fish_area = cursor.getString(3);
            fishDTO.fish_scale = cursor.getString(4);
            fishDTO.fish_rotation = cursor.getString(5);
        }

        cursor = db.rawQuery("select fish_explain from inventory_fish where fish_name = '"+name+"'", null);
        while(cursor.moveToNext()){
            fishDTO.fish_explain = cursor.getString(0);
        }

        db.close();

        return fishDTO;
    }

    void plusFishInventory(String name){
        db = myDB.getWritableDatabase();
        db.execSQL("update inventory_fish set fish_amount = " +
                "(select fish_amount from inventory_fish where fish_name = '"+name+"') + 1 where fish_name = '"+name+"'");
        db.execSQL("update fish set fish_dogam =" +
                "(select fish_dogam from fish where fish_name = '"+name+"') + 1 where fish_name = '"+name+"'");
        db.close();

    }

    void minusBaitInventory(String name){
        db = myDB.getWritableDatabase();
        db.execSQL("update inventory_bait set bait_amount = " +
                "(select bait_amount from inventory_bait where bait_name = '" +name+"') - 1 where bait_name = '"+name+"'");
        db.close();
    }

    boolean is_bait_Inventory(String name) {
        db = myDB.getReadableDatabase();
        int amount = 0;
        Cursor cursor = db.rawQuery("select bait_amount from inventory_bait where bait_name = '"+name+"'", null);
        while(cursor.moveToNext()){
            amount = cursor.getInt(0);
        }
        if(amount > 0) {
            return true;
        } else {
            return false;
        }
    }

//    ArrayList<Integer> select_quest_count_DB(int id) {
//        db = myDB.getReadableDatabase();
//        Cursor cursor = db.rawQuery("select questCount from quest where questFishId = "+ id, null);
//
//        ArrayList<Integer> questCount_arr = new ArrayList<>();
//        while(cursor.moveToNext()){
//            questCount_arr.add(cursor.getInt(0));
//        }
//
//        Cursor cursorA = db.rawQuery("select questCount from quest where questFishId = "+ 0, null);
//        while(cursorA.moveToNext()){
//            questCount_arr.add(cursorA.getInt(0));
//        }
//
//        return questCount_arr;
//
//    }
//
//    ArrayList<Integer> select_quest_now_DB(int id) {
//        db = myDB.getReadableDatabase();
//        Cursor cursor = db.rawQuery("select questNow from quest where questFishId = "+ id, null);
//
//        ArrayList<Integer> questNow_arr = new ArrayList<>();
//        while(cursor.moveToNext()){
//            questNow_arr.add(cursor.getInt(0));
//        }
//
//        Cursor cursorA = db.rawQuery("select questNow from quest where questFishId = "+ 0, null);
//        while(cursorA.moveToNext()){
//            questNow_arr.add(cursorA.getInt(0));
//        }
//
//        return questNow_arr;
//    }

    int select_quest_now_DB() {
        db = myDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("select questNow from quest where questFishId = 0" , null);
        int num = 0;
        while(cursor.moveToNext()){
            num = cursor.getInt(0);
        }

        return num;
    }

    void update_quest_now_DB(int id) {
        db = myDB.getWritableDatabase();
        db.execSQL("update quest set questNow = "+
                (select_quest_now_DB()+1)+
                " where questFishId = 0");
        db.close();

    }

    void update_quest_complete_DB() {
        db = myDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("select questCount, questNow, questId from quest" , null);
        while(cursor.moveToNext()){
            if(cursor.getInt(0) <= cursor.getInt(1)) {
                db.execSQL("update quest set questComplete = 1 where questId = "+ cursor.getInt(2)) ;
            }
        }
        db.close();

    }

    ArrayList<Integer> update_dogam_fish_DB(){
        ArrayList<Integer> dogam = new ArrayList<Integer>();

        db = myDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("select fish_dogam from fish", null);
        while (cursor.moveToNext()){
            dogam.add(cursor.getInt(0));
        }
        db.close();
        return dogam;
    }





    class MyDB extends SQLiteOpenHelper {

        public MyDB(@Nullable Context context) {
            super(context, "fishingdb", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}