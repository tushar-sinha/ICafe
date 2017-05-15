package com.example.rshikkal.icafe.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.ActionBar;
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
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rshikkal on 5/14/2017.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {

    private List<MenuItem> itemList;
    Context context;
    CartDatabase cartDatabase;
    ImageLoader imageLoader;
    ImageLoaderConfiguration configuration;
    DisplayImageOptions options;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemmenu, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MenuAdapter.MyViewHolder holder, int position) {
        // imageLoader = ImageLoader.getInstance();
        final MenuItem item = itemList.get(position);
        holder.itemname.setText(item.getItemname());
        holder.itemdescription.setText(item.getItemdescription().substring(0,30) + "....");
        Uri uri = Uri.parse(item.getImageurl());
        // Bitmap bitmap = imageLoader.loadImageSync(uri.toString());
        // holder.itemImage.setImageBitmap(bitmap);
        ImageLoader.getInstance().displayImage(uri.toString(), holder.itemImage, options);
        // imageLoader.displayImage(uri.toString(), holder.itemImage);
        // Picasso.with(context).load(uri).into(holder.itemImage);
        if (item.getItemtype().equals("0")){
            holder.itemtype.setText("VEG");
            holder.itemtype.setTextColor(Color.GREEN);
        }
        else{
            holder.itemtype.setText("NON VEG");
            holder.itemtype.setTextColor(Color.RED);
        }
        holder.itemrating.setText(item.getItemrating());
        holder.itemprice.setText(item.getItemprice());
        if (item.getItemstatus().equals("0")) {
            holder.itemstatus.setText("Available");
        }
        else{
            holder.itemstatus.setText("Not Available");
            holder.itemname.setTextColor(Color.GRAY);
            holder.itemdescription.setTextColor(Color.GRAY);
            holder.itemprice.setTextColor(Color.GRAY);
            holder.itemrating.setTextColor(Color.GRAY);
            holder.itemtype.setTextColor(Color.GRAY);
            holder.itemstatus.setTextColor(Color.GRAY);
            holder.itemadd.setVisibility(View.GONE);
            holder.itemImage.setAlpha(0.2f);

        }

        holder.itemadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartDatabase.addProduct(item);
                Toast.makeText(context, "Added",Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView  itemname, itemdescription, itemtype, itemprice,itemstatus,itemrating,itemadd;
        public RelativeLayout menuLayout;
        public ImageView itemImage;
        public MyViewHolder(View view) {
            super(view);
            itemname = (TextView) view.findViewById(R.id.itemname);
            itemdescription = (TextView) view.findViewById(R.id.itemdescription);
            itemtype = (TextView) view.findViewById(R.id.itemtype);
            itemprice = (TextView) view.findViewById(R.id.itemprice);
            itemstatus = (TextView) view.findViewById(R.id.itemstatus);
            itemrating = (TextView) view.findViewById(R.id.itemrating);
            itemadd = (TextView)view.findViewById(R.id.itemadd);
            menuLayout =(RelativeLayout)view.findViewById(R.id.menulayout);
            itemImage = (ImageView)view.findViewById(R.id.imageView);
        }
    }


    public MenuAdapter(List<MenuItem> List, Context context) {
        this.itemList = List;
        this.context = context;
        cartDatabase = new CartDatabase(context);

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .build();
        // imageLoader = ImageLoader.getInstance().init(configuration);
        configuration = new ImageLoaderConfiguration.Builder(this.context).build();
        ImageLoader.getInstance().init(configuration);
    }

}
