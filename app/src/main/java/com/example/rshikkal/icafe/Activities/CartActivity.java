package com.example.rshikkal.icafe.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.example.rshikkal.icafe.ServerUtils.ServerRequests;
import com.example.rshikkal.icafe.Utils.DialogUtils;

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
                    String itemids = "";
                    int price = 0;
                    itemids = menuItemList.get(0).getItemid();
                    price = Integer.parseInt(menuItemList.get(0).getItemprice());
                    for(int i = 1; i < menuItemList.size(); i++){
                        MenuItem newitem = menuItemList.get(i);
                        itemids = itemids + (";" + newitem.getItemid());
                        price = price + Integer.parseInt(newitem.getItemprice());
                    }

                    placeOrder(user, itemids, price);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void placeOrder(final User user, final String itemids, final int price) {
        new AsyncTask<Object, Object, JSONObject>(){

            DialogUtils progress = new DialogUtils(CartActivity.this, DialogUtils.Type.PROGRESS_DIALOG);
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progress.showProgressDialog("Placing Order.", "please wait...", false);
            }

            @Override
            protected JSONObject doInBackground(Object... params) {
                return new ServerRequests().placeOrder(user, itemids,price);
            }

            @Override
            protected void onPostExecute(JSONObject response) {
                super.onPostExecute(response);
                progress.dismissProgressDialog();
                try {
                    if (response != null) {
                        if (response.getString("status").equals("success")) {
                            Toast.makeText(CartActivity.this, "Order placed", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(CartActivity.this, OrderSuccessActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(CartActivity.this, "Failed", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(CartActivity.this, "Something went wrong!!", Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

        }.execute();
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
