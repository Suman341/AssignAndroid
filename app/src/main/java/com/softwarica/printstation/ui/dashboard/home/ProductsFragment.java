package com.softwarica.printstation.ui.dashboard.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.softwarica.printstation.PrintStationApplication;
import com.softwarica.printstation.R;
import com.softwarica.printstation.adapater.ProductRecyclerAdapter;
import com.softwarica.printstation.api.API;
import com.softwarica.printstation.api.request.ProductOrder;
import com.softwarica.printstation.api.request.ProductOrderRequest;
import com.softwarica.printstation.api.response.ApiResponse;
import com.softwarica.printstation.entity.CategoryEntity;
import com.softwarica.printstation.entity.Product;
import com.softwarica.printstation.storage.PrefManager;
import com.softwarica.printstation.ui.cart.CartActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductRecyclerAdapter adapter;
    private List<Product> products = new ArrayList<>();
    private ProgressDialog progressDialog;
    private TextView textCartItemCount;

    private String categoryId;

    public static ProductsFragment getInstance(CategoryEntity categoryEntity) {
        Bundle bundle = new Bundle();
        bundle.putString("categoryId", categoryEntity.get_id());
        ProductsFragment fragobj = new ProductsFragment();
        fragobj.setArguments(bundle);

        return fragobj;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        categoryId = getArguments().getString("categoryId");

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading...");

        recyclerView = view.findViewById(R.id.productRecyclerView);

        adapter = new ProductRecyclerAdapter(
                products,
                product -> {
                    product.setQuantity(1);
                    requestOrder(Arrays.asList(product));
                },
                product -> {
                    addProductToCart(product);
                }
        );

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        requestProducts(false);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem menuItem = menu.findItem(R.id.cart);
        View actionView = menuItem.getActionView();
        textCartItemCount = actionView.findViewById(R.id.cart_badge);
        setupBadge(PrefManager.service().getOrders().size());
        actionView.setOnClickListener(v -> onOptionsItemSelected(menuItem));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.cart:
                startActivity(new Intent(getContext(), CartActivity.class));
                return false;
            default:
                break;
        }

        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupBadge(PrefManager.service().getOrders().size());
    }

    private void addProductToCart(Product product) {
        // add to cart
        product.setQuantity(1);
        PrefManager.service().addToCart(product);
        Toast.makeText(getContext(), "Your product is added to cart", Toast.LENGTH_SHORT).show();
        setupBadge(PrefManager.service().getOrders().size());
    }

    private void setupBadge(int orders) {
        if (textCartItemCount != null) {
            if (orders == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(orders, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void requestProducts(boolean refreshing) {
        if (!refreshing) {
            progressDialog.setTitle("Loading...");
            progressDialog.show();
        }

        API.service().getProducts(categoryId).enqueue(new Callback<ApiResponse<List<Product>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<Product>>> call, @NonNull Response<ApiResponse<List<Product>>> response) {
                if (response.isSuccessful()) {
                    products = response.body().getData();
                    adapter.updateProducts(products);
                } else {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

                 progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<Product>>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Internal server error", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    private void requestOrder(List<Product> orders) {
        new AlertDialog.Builder(getContext())
                .setTitle("Order")
                .setMessage("Are you sure you want to buy this product")
                .setPositiveButton("Yes", ((dialog, which) -> {
                    dialog.dismiss();
                    progressDialog.setTitle("Requesting order...");
                    progressDialog.show();
                    API.service().orderProducts(mapOrders(orders)).enqueue(new Callback<ApiResponse<String>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                ((PrintStationApplication) getActivity().getApplication()).getNotificationUtil().showNotification("Order Completed", "Your order has been successful");
                            } else {
                                Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                            Toast.makeText(getContext(), "Internal server error", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                }))
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss()).create().show();
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

}
