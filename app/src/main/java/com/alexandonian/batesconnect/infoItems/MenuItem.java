package com.alexandonian.batesconnect.infoItems;

/**
 * Simple object so that we can store the nutrition info code and string name for each dining menu
 * item in a single arrayList.
 *
 * <p>Released under GNU GPL v2 - see doc/LICENCES.txt for more info.
 *
 */
public class MenuItem {

    public static final int ITEM = 0;
    public static final int SECTION = 1;


    private String itemName,
    code;

    private int type;

//    public MenuItem(String itemName, String code) {
//        this.itemName = itemName;
//        this.code = code;
//    }

    public MenuItem(String itemName, int type) {
        this.itemName = itemName;
        this.type = type;
    }

    public MenuItem(String itemName){
        this.itemName = itemName;
    }
    public String getItemName() {
        return itemName;
    }
    public String toString(){
        return itemName;
    }
    public int getItemType(){
        return type;
    }

//    public String getCode() {
//        return code;
//    }

}

