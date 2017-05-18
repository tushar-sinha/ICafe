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
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rshikkal.icafe.Adapters.MenuAdapter;
import com.example.rshikkal.icafe.Adapters.MyOrdersAdapter;
import com.example.rshikkal.icafe.Models.MenuItem;
import com.example.rshikkal.icafe.Models.User;
import com.example.rshikkal.icafe.Preferences.LoginPreferences;
import com.example.rshikkal.icafe.R;
import com.example.rshikkal.icafe.ServerUtils.ServerRequests;
import com.example.rshikkal.icafe.Utils.DialogUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    private List<MenuItem> menuItemList = new ArrayList<>();
    private RecyclerView recyclerView;
    MyOrdersAdapter myOrdersAdapter;

    Toolbar toolbar;
    TextView navigationItemNameTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        //set the toolbar
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        navigationItemNameTV = (TextView)findViewById(R.id.navigationitemname);
        navigationItemNameTV.setText("Orders");
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        loadMyOrders();


        recyclerView.addOnItemTouchListener(new CartActivity.RecyclerTouchListener(OrdersActivity.this, recyclerView, new CartActivity.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                MenuItem product = menuItemList.get(position);
                LoginPreferences preferences = new LoginPreferences(OrdersActivity.this);
                User user = preferences.getUser();
                try {
                    JSONObject object = new JSONObject();
                    object.put("user", user.getUseremail());
                    object.put("itemid" , product.getItemid());
                    object.put("price", product.getItemprice());
                    object.put("status", product.getItemstatus());

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));

    }

    private void loadMyOrders() {
        new AsyncTask<Object, Object, List<com.example.rshikkal.icafe.Models.MenuItem>>(){

            DialogUtils progress =  new DialogUtils(OrdersActivity.this, DialogUtils.Type.PROGRESS_DIALOG);
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progress.showProgressDialog("Your Orders","please wait...", false);
            }

            @Override
            protected List<com.example.rshikkal.icafe.Models.MenuItem> doInBackground(Object... params) {
                return new ServerRequests().loadMyOrders(OrdersActivity.this);
            }

            @Override
            protected void onPostExecute(List<com.example.rshikkal.icafe.Models.MenuItem> response) {
                super.onPostExecute(response);
                progress.dismissProgressDialog();
                Log.e("list",response.toString());
                myOrdersAdapter = new MyOrdersAdapter(response,OrdersActivity.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(OrdersActivity.this.getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(myOrdersAdapter);
                myOrdersAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }


    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private OrdersActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final OrdersActivity.ClickListener clickListener) {
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
}
