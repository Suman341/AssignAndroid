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


public class WishlistRecyclerAdapter extends RecyclerView.Adapter<WishlistRecyclerAdapter.Holder> {

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
        notifyDataSetChanged();
    }

    public List<Product> getOrders() {
        return this.orders;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wish_list_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Product product = orders.get(position);

        holder.productName.setText(product.getName());
        holder.quantityEdt.setText(product.getQuantity() + "");

        holder.addQuantity.setOnClickListener(v -> {
            product.setQuantity(product.getQuantity() + 1);
            holder.quantityEdt.setText(product.getQuantity() + "");
            holder.price.setText("Price: " + (product.getPrice() * product.getQuantity()));
            this.onOrderChanged.onChanged(this.orders);
        });
        holder.removeQuantity.setOnClickListener(v -> {
            if (product.getQuantity() <= 1) return;

            product.setQuantity(product.getQuantity() - 1);
            holder.quantityEdt.setText(product.getQuantity() + "");
            holder.price.setText("Price: " + (product.getPrice() * product.getQuantity()));
            this.onOrderChanged.onChanged(this.orders);
        });

        holder.price.setText("Price: " + (product.getPrice() * product.getQuantity()));

        Picasso.with(holder.itemView.getContext())
                .load(product.getImage())
                .into(holder.productImage);

        holder.btnDelete.setOnClickListener(v -> {
            onProductDelete.onClicked(product);
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView productName;
        private TextView price;
        private MaterialButton btnDelete;
        private TextView addQuantity, removeQuantity;
        private EditText quantityEdt;

        private Holder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            price = itemView.findViewById(R.id.price);
            btnDelete = itemView.findViewById(R.id.btDelete);
            addQuantity = itemView.findViewById(R.id.addQuantity);
            removeQuantity = itemView.findViewById(R.id.removeQuantity);
            quantityEdt = itemView.findViewById(R.id.orderQuantityEditText);
        }
    }
}
