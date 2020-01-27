package com.softwarica.printstation.ui.cart;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.button.MaterialButton;
import com.softwarica.printstation.PrintStationApplication;
import com.softwarica.printstation.R;
import com.softwarica.printstation.adapater.OrdersRecyclerAdapter;
import com.softwarica.printstation.adapater.WishlistRecyclerAdapter;
import com.softwarica.printstation.api.API;
import com.softwarica.printstation.api.request.ProductOrder;
import com.softwarica.printstation.api.request.ProductOrderRequest;
import com.softwarica.printstation.api.response.ApiResponse;
import com.softwarica.printstation.entity.OrderEntity;
import com.softwarica.printstation.entity.Product;
import com.softwarica.printstation.storage.PrefManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartActivity extends AppCompatActivity {

    private RecyclerView ordersRecyclerView, wishListRecyclerView;
    private MaterialButton requestOrderButton;
    Vibrator vibrator;

    private List<Product> wishList = new ArrayList<>();
    private List<OrderEntity> orders = new ArrayList<>();

    private WishlistRecyclerAdapter wishlistRecyclerAdapter;
    private OrdersRecyclerAdapter ordersRecyclerAdapter;

    private ProgressDialog progressDialog;
    private TextView totalPrice;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initUI();
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading...");}}