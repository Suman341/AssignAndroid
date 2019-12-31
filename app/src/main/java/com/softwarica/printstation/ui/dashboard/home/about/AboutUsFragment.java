package com.softwarica.printstation.ui.dashboard.home.about;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.softwarica.printstation.R;

public class AboutUsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getActionBar();
        if (actionBar != null){
            actionBar.setTitle("About Us");
        }

        return view;
    }
}
