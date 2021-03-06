package com.newsrss.Feed;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created with IntelliJ IDEA.
 * User: MediumMG
 * Date: 22.05.13
 * Time: 12:05
 * To change this template use File | Settings | File Templates.
 */
public class MapActivity extends FragmentActivity {

    static final LatLng Dublin = new LatLng(53.344060, -6.248361);
    static final LatLng Belfast = new LatLng(54.593788,-5.929491);

    private GoogleMap map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.map_layout);

        ImageButton backBtn = (ImageButton)findViewById(R.id.map_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        int mapChoose = getIntent().getIntExtra("map_choose", -1);

        if (mapChoose == 1)
            showPlace(Dublin);
        else
        if (mapChoose == 2)
            showPlace(Belfast);
    }

    private void showPlace(LatLng somePlace) {
        Marker place = map.addMarker(new MarkerOptions().position(somePlace)
                .title("Chartered Accountants Ireland"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(somePlace, 10));
        map.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);
    }
    @Override
    public void onStart() {
        super.onStart();

        EasyTracker.getInstance().activityStart(this); // Add this method.
    }
    @Override
    public void onStop() {
        super.onStop();

        EasyTracker.getInstance().activityStop(this); // Add this method.
    }

}