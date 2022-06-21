package com.example.arfishing;


public class InventoryDTO {

    int item_id;
    String item_name;
    String item_explain;
    int item_amount;

    InventoryDTO(int item_id, String item_name, String item_explain, int item_amount) {
        this.item_id = item_id;
        this.item_name = item_name;
        this.item_explain = item_explain;
        this.item_amount = item_amount;

    }

}
