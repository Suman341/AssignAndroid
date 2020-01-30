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


public class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductRecyclerAdapter.Holder> {

    public interface OnProductAction {
        void onClicked(Product product);
    }

    public interface OnOrderChanged{
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
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Product product = products.get(position);

        holder.productName.setText(product.getName());
        holder.price.setText("Price: "+product.getPrice());

        Picasso.with(holder.itemView.getContext())
                .load(product.getImage())
                .into(holder.productImage);

        holder.buyProduct.setOnClickListener(v -> {
            if (null != this.onProductBuy) onProductBuy.onClicked(product);
        });
        holder.addToCart.setOnClickListener(v ->{
            if (null != this.addToCart) addToCart.onClicked(product);
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView productName;
        private TextView price;
        private Button addToCart;
        private Button buyProduct;

        private Holder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            price = itemView.findViewById(R.id.price);
            addToCart = itemView.findViewById(R.id.addToCart);
            buyProduct = itemView.findViewById(R.id.buyProduct);
        }
    }
}
