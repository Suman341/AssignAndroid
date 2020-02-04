package com.softwarica.printstation.adapater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softwarica.printstation.R;
import com.softwarica.printstation.entity.OrderEntity;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


public class OrdersRecyclerAdapter extends RecyclerView.Adapter<OrdersRecyclerAdapter.Holder> {

    private List<OrderEntity> orders;

    Context context;
    private SimpleDateFormat simpleDateFormat;
    private SimpleDateFormat simpleDateFormat1;

    public OrdersRecyclerAdapter(List<OrderEntity> orders) {
        this.orders = orders;
        this.simpleDateFormat = new SimpleDateFormat("dd MMM, YYYY");
        this.simpleDateFormat1 = new SimpleDateFormat("YYYY-MM-dd");
    }

    public void updateProducts(List<OrderEntity> products) {
        this.orders = products;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        OrderEntity order = orders.get(position);

        if (order.getProduct() != null){
            holder.productName.setText(order.getProduct().getName());
            holder.price.setText("Price: " + (order.getProduct().getPrice() * order.getQuantity()));

            Picasso.with(holder.itemView.getContext())
                    .load(order.getProduct().getImage())
                    .into(holder.productImage);


        }else {
            holder.productName.setText("");
            holder.price.setText("Price: -");
        }

        holder.status.setText(order.getStatus());
        holder.quantity.setText("Quantity: " + order.getQuantity());


        try {
            Date orderDate = simpleDateFormat1.parse(order.getCreatedAt());
            holder.orderedAt.setText(simpleDateFormat.format(orderDate));
        }catch (Exception e){}




    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView productName;
        private TextView price;
        private TextView status, quantity, orderedAt;

        private Holder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            price = itemView.findViewById(R.id.price);
            status = itemView.findViewById(R.id.status);
            quantity = itemView.findViewById(R.id.quantity);
            orderedAt = itemView.findViewById(R.id.orderAt);
        }
    }
}
