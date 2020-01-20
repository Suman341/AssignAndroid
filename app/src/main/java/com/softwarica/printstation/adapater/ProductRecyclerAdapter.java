package com.softwarica.printstation.adapater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softwarica.printstation.R;
import com.softwarica.printstation.entity.Product;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ProductRecyclerAdapter {

    public interface OnProductAction {
        void onClicked(Product product);
    }

    public interface OnOrderChanged {
        void onChanged(List<Product> orders);
    }

    private List<Product> products;
    private OnProductAction onProductBuy;
    private OnProductAction addToCart;

    public ProductRecyclerAdapter(List<Product> products, OnProductAction onProductBuy, OnProductAction addToCart) {
        this.products = products;
        this.onProductBuy = onProductBuy;
        this.addToCart = addToCart;
    }

    public void updateProducts(List<Product> products) {
        this.products = products;

    }
}