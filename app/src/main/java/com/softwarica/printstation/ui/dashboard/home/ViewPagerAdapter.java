package com.softwarica.printstation.ui.dashboard.home;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.softwarica.printstation.entity.CategoryEntity;

import java.util.ArrayList;
import java.util.List;


public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<CategoryEntity> categories = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    public void setCategories(List<CategoryEntity> categories) {
        this.categories = categories;
    }

    @Override
    public Fragment getItem(int position) {
        return ProductsFragment.getInstance(categories.get(position));
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return categories.get(position).getCategory();
    }
}