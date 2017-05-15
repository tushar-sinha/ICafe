package com.example.rshikkal.icafe.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rshikkal.icafe.Adapters.CartAdapter;
import com.example.rshikkal.icafe.DBHandler.CartDatabase;
import com.example.rshikkal.icafe.Models.MenuItem;
import com.example.rshikkal.icafe.Models.User;
import com.example.rshikkal.icafe.Preferences.LoginPreferences;
import com.example.rshikkal.icafe.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private List<MenuItem> menuItemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CartAdapter mAdapter;
    CartDatabase databaseHandler = new CartDatabase(CartActivity.this);
    Toolbar toolbar;
    TextView toolbarTextTV, totalpriceTV;
    TextView placeOrderBT;
    ImageView emptyCartIV;
    LinearLayout placeorderLL;
    LoginPreferences loginPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTextTV = (TextView)findViewById(R.id.navigationitemname);
        emptyCartIV = (ImageView)findViewById(R.id.emptycart);
        placeorderLL = (LinearLayout)findViewById(R.id.placeorderlayout);
        placeOrderBT = (TextView) findViewById(R.id.placeorderbutton);
        totalpriceTV = (TextView)findViewById(R.id.totalprice);
        loginPreferences = new LoginPreferences(CartActivity.this);
        toolbarTextTV.setText("Cart");
        loadCart();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(CartActivity.this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                Button remove = (Button) view.findViewById(R.id.cartitemremove);
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MenuItem product = menuItemList.get(position);
                        databaseHandler.deleteProduct(product);
                        Toast.makeText(getApplicationContext(),"item Removed",Toast.LENGTH_LONG).show();
                        menuItemList.remove(product);
                        mAdapter.notifyDataSetChanged();
                        if (menuItemList.size() == 0){
                            emptyCartIV.setVisibility(View.VISIBLE);
                            placeorderLL.setVisibility(View.GONE);
                        }
                        else{
                            emptyCartIV.setVisibility(View.GONE);
                            placeorderLL.setVisibility(View.VISIBLE);
                            loadPrice();
                        }
                    }
                });
            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));

        placeOrderBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    User user = loginPreferences.getUser();
                    JSONArray itemsToOrder = new JSONArray();
                    for (MenuItem item : menuItemList) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("userid", user.getUseremail());
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }


    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }


        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }
        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }

    }



    private void loadCart() {
        menuItemList = databaseHandler.getAllProducts();
        if (menuItemList.size() == 0){
            emptyCartIV.setVisibility(View.VISIBLE);
            placeorderLL.setVisibility(View.GONE);
        }
        else {
            emptyCartIV.setVisibility(View.GONE);
            placeorderLL.setVisibility(View.VISIBLE);
            loadPrice();
        }
        mAdapter = new CartAdapter(menuItemList,CartActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }

    private void loadPrice() {
        int price = 0;
        for (MenuItem product : menuItemList){
            price += Integer.parseInt(product.getItemprice());
        }
        totalpriceTV.setText("Total - Rs. "+ String.valueOf(price));
    }
}
