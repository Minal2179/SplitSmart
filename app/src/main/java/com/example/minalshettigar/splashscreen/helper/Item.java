package com.example.minalshettigar.splashscreen.helper;

public class Item {

    private String item_name;
    private String item_price;


   public Item(String name,String price)
    {
       this.item_name=name;
       this.item_price=price;
    }
    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }
}
