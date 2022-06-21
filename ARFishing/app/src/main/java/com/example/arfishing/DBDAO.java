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
