package com.example.serbisyofinder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ProviderMap extends Fragment implements OnMapReadyCallback {
    private GoogleMap googleMap;

    // Keys for the arguments
    private static final String ARG_LATITUDE = "latitude";
    private static final String ARG_LONGITUDE = "longitude";

    private double latitude;
    private double longitude;

    public ProviderMap() {
        // Required empty public constructor
    }

    public static ProviderMap newInstance(double latitude, double longitude) {
        ProviderMap fragment = new ProviderMap();
        Bundle args = new Bundle();
        args.putDouble(ARG_LATITUDE, latitude);
        args.putDouble(ARG_LONGITUDE, longitude);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_provider_map, container, false);

        // Extract latitude and longitude values from the arguments
        Bundle args = getArguments();
        if (args != null) {
            latitude = args.getDouble(ARG_LATITUDE);
            longitude = args.getDouble(ARG_LONGITUDE);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapFragment);

        mapFragment.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Add a marker at the specified latitude and longitude
        LatLng location = new LatLng(latitude, longitude);

        // Clear any existing markers before adding a new one
        googleMap.clear();

        // Customize the marker
        MarkerOptions markerOptions = new MarkerOptions()
                .position(location)
                .title("Client Location");

        // Add the marker to the map
        googleMap.addMarker(markerOptions);

        // Move the camera to the marker's position
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16)); // Adjust the zoom level as needed (16 in this case)
    }
}
