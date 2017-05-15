package com.example.rshikkal.icafe.DBHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.Menu;

import com.example.rshikkal.icafe.Models.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by rshikkal on 5/14/2017.
 */

public class CartDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "itemmanager";
    private static final String TABLE_ITEMS= "items";

    // menu item Table Columns names
    private static final String KEY_ID = "itemid";
    private static final String KEY_NAME = "itemname";
    private static final String KEY_DESCRIPTION = "itemdescription";
    private static final String KEY_ITEMTYPE = "itemtype";
    private static final String KEY_PRICE = "itemprice";
    private static final String KEY_ITEMSTATUS = "itemstatus";
    private static final String KEY_RATINGS = "itemratings";

    public CartDatabase(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS + "("
                + KEY_ID + " TEXT PRIMARY KEY," + KEY_NAME + " TEXT,"+ KEY_DESCRIPTION + " TEXT,"+ KEY_ITEMTYPE + " TEXT,"
                + KEY_PRICE + " TEXT," + KEY_ITEMSTATUS + " TEXT," + KEY_RATINGS + " TEXT" + ")";
        db.execSQL(CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }

    public void addProduct(MenuItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, item.getItemid());
        values.put(KEY_NAME, item.getItemname());
        values.put(KEY_DESCRIPTION, item.getItemdescription());
        values.put(KEY_ITEMTYPE, item.getItemtype());
        values.put(KEY_PRICE, item.getItemprice());
        values.put(KEY_ITEMSTATUS, item.getItemstatus());
        values.put(KEY_RATINGS,item.getItemrating());
        db.insert(TABLE_ITEMS, null, values);
        db.close();
    }


    public List<MenuItem> getAllProducts() {
        List<MenuItem> menuItemList = new ArrayList<MenuItem>();
        String selectQuery = "SELECT  * FROM " + TABLE_ITEMS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                MenuItem menuItem = new MenuItem();
                menuItem.setItemid(cursor.getString(0));
                menuItem.setItemname(cursor.getString(1));
                menuItem.setItemdescription(cursor.getString(2));
                menuItem.setItemtype(cursor.getString(3));
                menuItem.setItemprice(cursor.getString(4));
                menuItem.setItemstatus(cursor.getString(5));
                menuItem.setItemrating(cursor.getString(6));
                menuItemList.add(menuItem);
            } while (cursor.moveToNext());
        }
        return menuItemList;
    }


    public void deleteProduct(MenuItem menuItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, KEY_ID + " = ?",
                new String[] { String.valueOf(menuItem.getItemid()) });
        db.close();
    }

    public int getProductCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ITEMS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }


}
