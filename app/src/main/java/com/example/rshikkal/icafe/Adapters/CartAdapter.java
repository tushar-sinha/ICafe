package com.example.rshikkal.icafe.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rshikkal.icafe.DBHandler.CartDatabase;
import com.example.rshikkal.icafe.Models.MenuItem;
import com.example.rshikkal.icafe.R;

import java.util.List;

/**
 * Created by rshikkal on 5/14/2017.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    private List<MenuItem> itemList;
    Context context;
    CartDatabase cartDatabase;

    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemcart, parent, false);

        return new CartAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartAdapter.MyViewHolder holder, int position) {
        final MenuItem item = itemList.get(position);
        holder.itemname.setText(item.getItemname());
        holder.itemprice.setText(item.getItemprice());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView itemname,itemprice,itemremove;
        public MyViewHolder(View view) {
            super(view);
            itemname = (TextView) view.findViewById(R.id.cartitemname);
            itemprice = (TextView) view.findViewById(R.id.cartitemprice);
            itemremove = (TextView)view.findViewById(R.id.cartitemremove);
        }
    }


    public CartAdapter(List<MenuItem> List, Context context) {
        this.itemList = List;
        this.context = context;
        cartDatabase = new CartDatabase(context);
    }

}
