package com.example.rshikkal.icafe.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rshikkal.icafe.DBHandler.CartDatabase;
import com.example.rshikkal.icafe.Models.MenuItem;
import com.example.rshikkal.icafe.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

/**
 * Created by rshikkal on 5/18/2017.
 */

public class MyOrdersAdapter  extends RecyclerView.Adapter<MyOrdersAdapter.MyViewHolder>{
    private List<MenuItem> itemList;
    Context context;
    ImageLoader imageLoader;
    ImageLoaderConfiguration configuration;
    DisplayImageOptions options;

    @Override
    public MyOrdersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemorder, parent, false);

        return new MyOrdersAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyOrdersAdapter.MyViewHolder holder, int position) {
        final MenuItem item = itemList.get(position);
        holder.itemid.setText(item.getItemid());
        holder.itemprice.setText("Price : " + item.getItemprice());
        if (item.getItemstatus().equals("1")) {
            holder.itemstatus.setText("Status : Prepared");
        }
        else{
            holder.itemstatus.setText("Status : Processing");
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView  itemid, itemprice,itemstatus;
        public RelativeLayout menuLayout;
        public ImageView itemImage;
        public MyViewHolder(View view) {
            super(view);
            itemid = (TextView)view.findViewById(R.id.itemid);
            itemprice = (TextView) view.findViewById(R.id.price);
            itemstatus = (TextView) view.findViewById(R.id.status);
        }
    }


    public MyOrdersAdapter(List<MenuItem> List, Context context) {
        this.itemList = List;
        this.context = context;
    }

}
