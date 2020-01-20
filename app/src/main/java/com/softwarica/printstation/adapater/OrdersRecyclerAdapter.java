package com.softwarica.printstation.adapater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softwarica.printstation.R;
import com.softwarica.printstation.entity.OrderEntity;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class OrdersRecyclerAdapter  {

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

    }

}