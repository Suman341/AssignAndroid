package com.softwarica.printstation.ui.dashboard.contact;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.softwarica.printstation.R;

import java.util.List;

public class ContactUsFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private LatLng printStationPosition = new LatLng(27.732386,85.3172426);
    private Marker printStationNepalMarker;

    public static Intent getOpenFacebookIntent(Context context, String pageId, String userName) {
        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + pageId));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + userName));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);

        ImageButton fbBtn = view.findViewById(R.id.fbBtn);
        ImageButton instaBtn = view.findViewById(R.id.instsaBtn);


        fbBtn.setOnClickListener(v -> {
            startActivity(getOpenFacebookIntent(getContext(), "368711697049577", "printstationnepal"));
        });
        instaBtn.setOnClickListener(v -> {
            startActivity(getOpenInstagramIntent(getContext(), "printstationnepal"));
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    public Intent getOpenInstagramIntent(Context context, String userName) {
        try {
            context.getPackageManager().getPackageInfo("com.instagram.android", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/" + userName));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/" + userName));
        }
    }

    private boolean isIntentAvailable(Context ctx, Intent intent) {
        final PackageManager packageManager = ctx.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        drawMarker();
    }

    private void drawMarker() {
        if (mMap == null) return;

        if (printStationNepalMarker == null) {
            printStationNepalMarker = mMap.addMarker(new MarkerOptions().position(printStationPosition).title("PrintStation Nepal"));
        }else{
            printStationNepalMarker.setPosition(printStationPosition);
        }
        CameraUpdate center = CameraUpdateFactory.newLatLng(printStationPosition);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
    }
}
