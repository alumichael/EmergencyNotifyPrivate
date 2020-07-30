package com.mikelearning.projecttest.fragements;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mikelearning.projecttest.Constant;
import com.mikelearning.projecttest.MapsActivity;
import com.mikelearning.projecttest.R;
import com.mikelearning.projecttest.Utils.NetworkConnection;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;


public class Fragment_Locations extends Fragment implements OnMapReadyCallback, RoutingListener,
        LocationListener {

    private GoogleMap mMap;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;


    @BindView(R.id.send)
    FloatingActionButton send;

    @BindView(R.id.map_layout)
    LinearLayout map_layout;

    LocationRequest mLocationRequest;
    Location mLastLocation;
    LatLng accident_location;
    Marker mCurrLocationMarker, mMaker;
    private LatLng latLng;
    private ArrayList<Polyline> polylines;
    String mParam3_name;
    Double mParam1_lat,mParam2_lng;
    private static final int[] COLORS = new int[]{R.color.colorYellow, R.color.colorBlue, R.color.colorGreen};

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 100;

    NetworkConnection networkConnection = new NetworkConnection();

    public static Fragment_Locations newInstance(double lat,double lng, String name) {
        Bundle args = new Bundle();
        args.putDouble(Constant.ARG_LATITUDE, lat);
        args.putDouble(Constant.ARG_LONGITUDE, lng);
        args.putString(Constant.FULL_NAME, name);
        Fragment_Locations fragment = new Fragment_Locations();
        fragment.setArguments(args);
        return fragment;
    }


    public Fragment_Locations() {
        // Required empty public constructor
    }

    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1_lat = getArguments().getDouble(Constant.ARG_LATITUDE);
            mParam2_lng = getArguments().getDouble(Constant.ARG_LONGITUDE);
            mParam3_name = getArguments().getString(Constant.FULL_NAME);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        ButterKnife.bind(this, view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        polylines = new ArrayList<>();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();

        }
        mapFragment.getMapAsync(this);

        return view;
    }


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permission was granted.
                    if (ContextCompat.checkSelfPermission(getContext(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        buildGoogleApiClient();
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(getContext(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }


        }
    }


    @Override
    public void onRoutingFailure(RouteException e) {
        progressBar.setVisibility(View.GONE);

        if (e != null) {
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int i) {
        progressBar.setVisibility(View.GONE);

        CameraUpdate center = CameraUpdateFactory.newLatLng(latLng);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

        mMap.moveCamera(center);


        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int j = 0; j < route.size(); j++) {

            //In case of more than 5 alternative routes
            int colorIndex = j % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(7 + j * 3);
            polyOptions.addAll(route.get(j).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);
            // Toast.makeText(getApplicationContext(), "Route " + (i + 1) + ": distance - " + route.get(i).getDistanceValue() + ": duration - " + route.get(i).getDurationValue(), Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    public void onRoutingCancelled() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if (networkConnection.isNetworkConnected(getContext() )) {

            // Add a marker to incidence location
            if(mParam3_name==null) {

                 Snackbar.make(map_layout, "Hi Look out for alternative escape route here", Snackbar.LENGTH_LONG)
                        .show();

            }else{


                Log.i("lat", String.valueOf(mParam2_lng));

                accident_location = new LatLng(mParam1_lat, mParam2_lng);
                mMap.addMarker(new MarkerOptions().position(accident_location).title(mParam3_name));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(accident_location));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(accident_location) // Sets the center of the map to Mountain View
                        .zoom(15)                   // Sets th e zoom
                        .bearing(30)                // Sets the orientation of the camera to east
                        .tilt(45)   // Sets the tilt of the camera to 30 degrees
                        .build();
                // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }

            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(getContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    buildGoogleApiClient();
                    mMap.setMyLocationEnabled(true);
                }
            } else {


                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);

            }

            mMap.setTrafficEnabled(true);
            mMap.setIndoorEnabled(true);
            mMap.setBuildingsEnabled(true);

            if(mParam3_name!=null) {
                //route();
            }


        } else {
            Snackbar.make(map_layout, "No Internet Connection", Snackbar.LENGTH_LONG)
                    .show();
        }
    }


    protected synchronized void buildGoogleApiClient() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            getFusedLocationProviderClient(getActivity()).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            // do work here
                            onLocationChanged(locationResult.getLastLocation());
                        }
                    },
                    Looper.myLooper());
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));


    }

    @OnClick(R.id.send)
    public void sendRequest() {
        try{
            if (networkConnection.isNetworkConnected(getContext())) {
                route();
            }else{
                Snackbar.make(map_layout, "No Internet Connection", Snackbar.LENGTH_LONG)
                        .show();
            }
        }catch (Exception e){
            Snackbar.make(map_layout, "Error Occurred while routing", Snackbar.LENGTH_LONG)
                    .show();
        }

    }



    public void route() {
        try {

            Routing routing = new Routing.Builder()
                    .key("AIzaSyDWaFRIkmTCba2d06Ic8-CHI-gLWUmdexA")
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(latLng, accident_location)
                    .build();
            routing.execute();
        }catch (Exception n){
            Looper.prepare();
            Toast.makeText(getContext(), "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }




}
