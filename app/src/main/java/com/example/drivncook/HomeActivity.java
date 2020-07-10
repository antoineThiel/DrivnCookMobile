package com.example.drivncook;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("Registered")
public class HomeActivity extends AppCompatActivity {

    private List<Franchisee> lfranchisee = new ArrayList<>();
    private List<Franchisee> lfranchiseePromo = new ArrayList<>();
    private FranchiseeAdapter fAdapter;
    private FranchiseeAdapter fpAdapter;
    private TextView position;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private double latitude = 0;
    private double longitude = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        position = findViewById(R.id.posi);

        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        } else{
            getCurrentLocation();
        }

        Log.d("lat", position.getText().toString());
        Log.d("lng", Double.toString(longitude));

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerView recyclerView1 = findViewById(R.id.recyclerViewPromo);

        fAdapter = new FranchiseeAdapter(lfranchisee, HomeActivity.this);
        fpAdapter = new FranchiseeAdapter(lfranchiseePromo, HomeActivity.this);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearLayoutManager mPLayoutManager = new LinearLayoutManager(getApplicationContext());
        mPLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(fAdapter);

        recyclerView1.setLayoutManager(mPLayoutManager);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setAdapter(fpAdapter);
        //prepareFranchiseePromoData();

        final SharedPreferences shp = getSharedPreferences("order", MODE_PRIVATE);
        final String order = shp.getString("order", null);
        String basket = shp.getString("emptyBasket", null);
        if (basket == null){
            SharedPreferences.Editor edit = shp.edit();
            edit.putString("emptyBasket", "true");
            edit.apply();
        }

        if (order == null){
            final SharedPreferences shp2 = getSharedPreferences("logged", MODE_PRIVATE);
            final String id = shp2.getString("id", null);
            final String urlOrder = "http://51.210.7.226/newOrder/"+id;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, urlOrder, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                SharedPreferences.Editor edit = shp.edit();
                                String idOrder = response.get("id").toString();
                                edit.putString("idOrder", idOrder);
                                edit.putString("order", "ongoing");
                                edit.apply();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error
                        }
                    });

            //Add Request to the Queue.
            MySingleton.getInstance(HomeActivity.this).addToRequestQueue(jsonObjectRequest);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            getCurrentLocation();
        }else{
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    // Menu icons are inflated just as they were with actionbar
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_basket:
                Intent it = new Intent(HomeActivity.this, BasketActivity.class);
                startActivity(it);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void prepareFranchiseeData(Double longitude, Double latitude) {
        final String url ="http://51.210.7.226/getfranchisee/" + longitude + "/" + latitude;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++)
                            {
                                JSONObject jFranchisee = response.getJSONObject(i);
                                final Franchisee franchisee = new Franchisee(
                                        jFranchisee.get("id").toString(),
                                        jFranchisee.get("lastname").toString(),
                                        jFranchisee.get("firstname").toString(),
                                        jFranchisee.get("distance").toString(),
                                        jFranchisee.get("address").toString(),
                                        jFranchisee.get("city").toString()
                                );
                                lfranchisee.add(franchisee);
                            }
                            fAdapter.notifyDataSetChanged();
                           // updateFranchiseeData(lfranchisee);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                    }
                });

        //Add Request to the Queue.
        MySingleton.getInstance(HomeActivity.this).addToRequestQueue(jsonArrayRequest);
    }

    private void getCurrentLocation()
    {
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(HomeActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback(){
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(HomeActivity.this)
                                .removeLocationUpdates(this);
                        if (locationResult !=  null && locationResult.getLocations().size() > 0){
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            latitude =
                                    locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            longitude =
                                    locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            prepareFranchiseeData(longitude, latitude);
                            prepareFranchiseePromoData(longitude, latitude);
                        }
                    }
                },Looper.getMainLooper());
    }

    private void prepareFranchiseePromoData(Double longitude, Double latitude) {
        final String url = "http://51.210.7.226/getEvents/" + longitude + "/" + latitude;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jFranchisee = response.getJSONObject(i);
                                final Franchisee franchisee = new Franchisee(
                                        jFranchisee.get("id").toString(),
                                        jFranchisee.get("lastname").toString(),
                                        jFranchisee.get("firstname").toString(),
                                        jFranchisee.get("distance").toString(),
                                        jFranchisee.get("address").toString(),
                                        jFranchisee.get("city").toString()
                                );
                                lfranchiseePromo.add(franchisee);
                            }
                            fpAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                    }
                });

        //Add Request to the Queue.
        MySingleton.getInstance(HomeActivity.this).addToRequestQueue(jsonArrayRequest);
    }
}

