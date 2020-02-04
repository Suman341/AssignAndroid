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
        progressDialog.setTitle("Loading...");

        wishList = PrefManager.service().getOrders();

        totalPrice = findViewById(R.id.totalPrice);

        wishListRecyclerView = findViewById(R.id.wishListRecyclerView);
        requestOrderButton = findViewById(R.id.requestOrderButton);
        requestOrderButton.setOnClickListener(v -> {
            if (wishList == null || wishList.size() <= 0) {
                Toast.makeText(this, "You has no product to order", Toast.LENGTH_SHORT).show();
                calculateTotalPrice(wishList);
                //VIBRATION IN USE
                vibrator.vibrate(50);

                return;
            }
            requestOrder(wishList);
        });

        wishlistRecyclerAdapter = new WishlistRecyclerAdapter(
                wishList,
                product -> {
                    this.onOrderDelete(product);

                },
                orders -> {
                    this.onOrderChanged(orders);
                });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        wishListRecyclerView.setLayoutManager(layoutManager);
        wishListRecyclerView.setAdapter(wishlistRecyclerAdapter);

        calculateTotalPrice(this.wishList);


        ordersRecyclerAdapter = new OrdersRecyclerAdapter(orders);
        ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this);
        ordersRecyclerView.setLayoutManager(layoutManager2);
        ordersRecyclerView.setAdapter(ordersRecyclerAdapter);

        getOrders();

        swipeRefreshLayout = findViewById(R.id.swipeToRefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> this.getOrders());

    }

    private void onOrderChanged(List<Product> orders) {
        PrefManager.service().updateOrders(orders);
        calculateTotalPrice(orders);
    }

    private void onOrderDelete(Product product) {
        this.wishList.remove(product);
        PrefManager.service().removeFromCart(product);
        this.wishlistRecyclerAdapter.updateProducts(wishList);
        calculateTotalPrice(wishList);
        Toast.makeText(this, "Product removed from cart", Toast.LENGTH_SHORT).show();
        //VIBRATION IN USE
        vibrator.vibrate(50);

    }

    private void getOrders() {
        API.service().getMyOrders().enqueue(new Callback<ApiResponse<List<OrderEntity>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<OrderEntity>>> call, Response<ApiResponse<List<OrderEntity>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ordersRecyclerAdapter.updateProducts(response.body().getData());
                }

                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ApiResponse<List<OrderEntity>>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void requestOrder(List<Product> orders) {
        API.service().orderProducts(mapOrders(orders)).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CartActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    wishList = new ArrayList<>();
                    wishlistRecyclerAdapter.updateProducts(wishList);
                    PrefManager.service().resetOrders();
                    calculateTotalPrice(wishList);
                    getOrders();
                    ((PrintStationApplication) getApplication()).getNotificationUtil().showNotification("Order Completed", "Your order has been successful");
                } else {
                    Toast.makeText(CartActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Internal server error", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private ProductOrderRequest mapOrders(List<Product> wishList) {
        ProductOrderRequest orderRequest = new ProductOrderRequest();
        Map<String, ProductOrder> productOrders = new HashMap<>();
        for (Product product : wishList) {
            ProductOrder order = productOrders.get(product.get_id());
            if (order == null) {
                productOrders.put(product.get_id(), new ProductOrder(product.getQuantity(), product.get_id()));
            } else {
                order.setQuantity(order.getQuantity() + 1);
                productOrders.put(order.getProductId(), order);
            }
        }
        orderRequest.setOrders(new ArrayList<>(productOrders.values()));
        return orderRequest;
    }

    private long calculateTotalPrice(List<Product> orders) {
        long totalPrice = 0;
        for (Product product : orders) {
            totalPrice = totalPrice + product.getPrice() * product.getQuantity();
        }
        this.totalPrice.setText("NRs. " + totalPrice);
        return totalPrice;
    }
}
