package com.softwarica.printstation.ui.dashboard.home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.softwarica.printstation.R;
import com.softwarica.printstation.api.API;
import com.softwarica.printstation.api.response.ApiResponse;
import com.softwarica.printstation.entity.CategoryEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.VIBRATOR_SERVICE;

public class HomeFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private ViewPagerAdapter categoryAdapter;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipeToRefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            this.getCategories();

        });

        viewPager = view.findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(0);
        categoryAdapter = new ViewPagerAdapter(getChildFragmentManager());


        CategoryEntity categoryAll = new CategoryEntity();
        categoryAll.setCategory("All");
        categoryAdapter.setCategories(Arrays.asList(categoryAll));
        categoryAdapter.notifyDataSetChanged();
        viewPager.setAdapter(categoryAdapter);

        tabLayout = view.findViewById(R.id.categoryTabs);
        tabLayout.setupWithViewPager(viewPager);


        swipeRefreshLayout.setRefreshing(true);
        getCategories();
        return view;

    }

    private void getCategories() {
        CategoryEntity categoryAll = new CategoryEntity();
        categoryAll.setCategory("All");
        API.service().getCategories().enqueue(new Callback<ApiResponse<List<CategoryEntity>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<CategoryEntity>>> call, Response<ApiResponse<List<CategoryEntity>>> response) {
                if (response.isSuccessful()) {
                    List<CategoryEntity> categories = new ArrayList<>();
                    categories.add(categoryAll);
                    categories.addAll(response.body().getData());
                    categoryAdapter.setCategories(categories);
                    categoryAdapter.notifyDataSetChanged();
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ApiResponse<List<CategoryEntity>>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }

        });
    }
}
