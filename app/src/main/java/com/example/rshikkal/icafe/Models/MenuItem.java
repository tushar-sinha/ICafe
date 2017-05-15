package com.example.rshikkal.icafe.Models;

/**
 * Created by rshikkal on 5/14/2017.
 */

public class MenuItem {
    String itemid, itemname, itemdescription, itemtype, itemprice,itemstatus,itemrating, imageurl;

    public MenuItem(String itemid, String itemname, String itemdescription, String itemtype, String itemprice, String itemstatus, String itemrating, String imageurl) {
        this.itemid = itemid;
        this.itemname = itemname;
        this.itemdescription = itemdescription;
        this.itemtype = itemtype;
        this.itemprice = itemprice;
        this.itemstatus = itemstatus;
        this.itemrating = itemrating;
        this.imageurl = imageurl;
    }

    public MenuItem(){

    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemdescription() {
        return itemdescription;
    }

    public void setItemdescription(String itemdescription) {
        this.itemdescription = itemdescription;
    }

    public String getItemtype() {
        return itemtype;
    }

    public void setItemtype(String itemtype) {
        this.itemtype = itemtype;
    }

    public String getItemprice() {
        return itemprice;
    }

    public void setItemprice(String itemprice) {
        this.itemprice = itemprice;
    }

    public String getItemstatus() {
        return itemstatus;
    }

    public void setItemstatus(String itemstatus) {
        this.itemstatus = itemstatus;
    }

    public String getItemrating() {
        return itemrating;
    }

    public void setItemrating(String itemrating) {
        this.itemrating = itemrating;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
