package com.example.rshikkal.icafe.Parsers;

import com.example.rshikkal.icafe.Models.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rshikkal on 5/14/2017.
 */

public class MenuParser {

    public List<MenuItem> parse(JSONArray jsonArray){
        List<MenuItem> itemList = new ArrayList<>();
        for (int i = 0;i < jsonArray.length(); i++){
            try {
                itemList.add(parse(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return itemList;
    }


    public MenuItem parse(JSONObject object){
        MenuItem item = null;
        String itemid = "", itemname = "", itemdescription = "", itemtype = "", itemprice =  "",itemstatus = "",itemrating = "", imageurl = "";

        try {
            if (object.has("itemid")) {
                itemid = object.getString("itemid");
            }
            if (object.has("itemname")) {
                itemname = object.getString("itemname");
            }
            if (object.has("itemdescription")) {
                itemdescription = object.getString("itemdescription");
            }
            if (object.has("itemtype")) {
                itemtype = object.getString("itemtype");
            }
            if (object.has("itemprice")) {
                itemprice = object.getString("itemprice");
            }
            if (object.has("status")) {
                itemstatus = object.getString("status");
            }
            if (object.has("rating")) {
                itemrating = object.getString("rating");
            }
            if (object.has("image")) {
                imageurl = object.getString("image");
            }
            item = new MenuItem(itemid,itemname, itemdescription, itemtype, itemprice, itemstatus, itemrating, imageurl);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return item;

    }
}
