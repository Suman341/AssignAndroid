package com.softwarica.printstation.adapater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.softwarica.printstation.R;
import com.softwarica.printstation.entity.Product;
import com.squareup.picasso.Picasso;

import java.util.List;


public class WishlistRecyclerAdapter {

    private final ProductRecyclerAdapter.OnOrderChanged onOrderChanged;
    private List<Product> orders;
    private ProductRecyclerAdapter.OnProductAction onProductDelete;


    public WishlistRecyclerAdapter(List<Product> orders, ProductRecyclerAdapter.OnProductAction onProductDelete, ProductRecyclerAdapter.OnOrderChanged onOrderChanged) {
        this.orders = orders;
        this.onProductDelete = onProductDelete;
        this.onOrderChanged = onOrderChanged;
    }

    public void updateProducts(List<Product> products) {
        this.orders = products;

    }
}

