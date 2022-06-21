package com.example.arfishing;


public class InventoryDTO {

    int item_id;
    String item_name;
    String item_explain;
    int item_amount;
    int item_price;

    InventoryDTO(int item_id, String item_name, String item_explain, int item_amount, int item_price) {
        this.item_id = item_id;
        this.item_name = item_name;
        this.item_explain = item_explain;
        this.item_amount = item_amount;
        this.item_price = item_price;
    }

}
