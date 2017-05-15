package com.example.rshikkal.icafe.Utils;

import android.content.Context;
import android.widget.Toast;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.zip.CheckedOutputStream;

/**
 * Created by rshikkal on 5/13/2017.
 */

public class DisplayToast {

    Context context;
    String message;
    int length;

    public DisplayToast(Context context, String message, int length){
       this.context = context;
        this.message = message;
        this.length = length;
    }

    public void toast(){
        if (length == 0) {
              Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
        else{
              Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

}
