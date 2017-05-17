package com.example.rshikkal.icafe.ServerUtils;

import android.content.Context;

import com.example.rshikkal.icafe.Models.MenuItem;
import com.example.rshikkal.icafe.Models.User;
import com.example.rshikkal.icafe.Parsers.MenuParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rshikkal on 5/13/2017.
 */

public class ServerRequests {

    HttpClientWrapper clientWrapper;
    public ServerRequests(){
        clientWrapper = new HttpClientWrapper();
    }

    public JSONObject loginUser(String email, String password) {

        JSONObject responseObject = new JSONObject();
        try {
            JSONObject request = new JSONObject();
            request.put("email", email);
            request.put("password", password);
            String response = clientWrapper.doPostRequest(Urls.BASEURL+Urls.USER_LOGIN,request.toString());
            responseObject = new JSONObject(response);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return responseObject;

    }

    public List<MenuItem> loadMenu(Context context) {
            List<MenuItem> kernalRates  = new ArrayList<>();
            JSONObject responseObject = null;
            try {
                String response = clientWrapper.doGetRequest(context, Urls.BASEURL + Urls.MENU_ITEM);
                responseObject = new JSONObject(response);
                if (responseObject != null) {
                    if (responseObject.getString("status").equals("success")) {
                        kernalRates = new MenuParser().parse(responseObject.getJSONArray("items"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return kernalRates;
    }

    public JSONObject placeOrder(User user, String itemids, int price) {
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject request = new JSONObject();
            request.put("useremail", user.getUseremail());
            request.put("itemid", itemids);
            request.put("price", price);
            String response = clientWrapper.doPostRequest(Urls.BASEURL+Urls.MAKE_ORDER,request.toString());
            responseObject = new JSONObject(response);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return responseObject;
    }
}
