package com.example.drivncook;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
        prepareFranchiseeData();

        recyclerView1.setLayoutManager(mPLayoutManager);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setAdapter(fpAdapter);
        prepareFranchiseePromoData();


    }
    private void prepareFranchiseeData() {
        final String url ="http://10.0.2.2/getfranchisee";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++)
                            {
                                JSONObject jFranchisee = response.getJSONObject(i);
                                Franchisee franchisee = new Franchisee(
                                        jFranchisee.get("id").toString(),
                                        jFranchisee.get("lastname").toString(),
                                        jFranchisee.get("firstname").toString()
                                );
                                lfranchisee.add(franchisee);
                            }
                            fAdapter.notifyDataSetChanged();
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

    private void prepareFranchiseePromoData() {
        final String url ="http://10.0.2.2/getfranchisee";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++)
                            {
                                JSONObject jFranchisee = response.getJSONObject(i);
                                Franchisee franchisee = new Franchisee(
                                        jFranchisee.get("id").toString(),
                                        jFranchisee.get("lastName").toString(),
                                        jFranchisee.get("firstName").toString()
                                );
                                lfranchiseePromo.add(franchisee);
                            }
                            fpAdapter.notifyDataSetChanged();
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



















//    private ListView lv;
//    private TextView tv;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home);
//        lv = findViewById(R.id.list);
//        tv = findViewById(R.id.textView);
//        final List<Franchisee> result = new ArrayList<>();
//        final String url ="http://10.0.2.2/getfranchisee";
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
//                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        try {
//                            for (int i = 0; i < response.length(); i++)
//                            {
//                                JSONObject jFranchisee = response.getJSONObject(i);
//                                Franchisee franchisee = new Franchisee(
//                                        jFranchisee.get("id").toString(),
//                                        jFranchisee.get("lastName").toString(),
//                                        jFranchisee.get("firstName").toString()
//                                );
//                                result.add(franchisee);
//                            }
//                            final FranchiseeAdapter adapt = new FranchiseeAdapter(HomeActivity.this, result);
//                            lv.setAdapter(adapt);
//                        }catch (JSONException e){
//                            e.printStackTrace();
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("Error", error.toString());
//                    }
//                });
//
//        //Add Request to the Queue.
//        MySingleton.getInstance(HomeActivity.this).addToRequestQueue(jsonArrayRequest);
    }

