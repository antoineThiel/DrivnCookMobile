package com.example.drivncook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("Registered")
public class ProductActivity extends AppCompatActivity {

    private TextView fName;
    private TextView pName;
    private TextView pPrice;
    private TextView quantity;
    private TextView totalPrice;
    private Button less;
    private Button more;
    private TextView addCArt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        fName = findViewById(R.id.product_fname);
        pName = findViewById(R.id.product_pname);
        quantity = findViewById(R.id.quantity);
        less = findViewById(R.id.id_less);
        more = findViewById(R.id.id_more);
        totalPrice = findViewById(R.id.ptotal_price);
        pPrice = findViewById(R.id.pPrice);
        addCArt = findViewById(R.id.add_cart);

        less.setVisibility(View.INVISIBLE);


        final SharedPreferences shp = getSharedPreferences("order",MODE_PRIVATE);
        String idFranchisee = shp.getString("idFranchisee", null);
        final String id = getIntent().getStringExtra("id");
        final String urlgetFranchisee =  "http://51.210.7.226/getfranchiseeinfo/" + idFranchisee;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, urlgetFranchisee, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            fName.setText(response.get("lastname").toString() + " " + response.get("firstname").toString());
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
        MySingleton.getInstance(ProductActivity.this).addToRequestQueue(jsonObjectRequest);


        final String url ="http://51.210.7.226/getarticle/"+id;
        final int[] maxQty = new int[1];

        JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                           pName.setText(response.get("nom").toString() + " " + response.get("quantity") + response.get("unit"));
                           pPrice.setText(response.get("price").toString());
                           totalPrice.setText(response.get("price").toString());
                           maxQty[0] = Integer.parseInt(response.get("stock").toString());
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
        MySingleton.getInstance(ProductActivity.this).addToRequestQueue(jsonObjectRequest2);

        less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(quantity.getText().toString());
                if (qty == 2){
                    less.setVisibility(View.INVISIBLE);
                }

                if (qty == maxQty[0])
                    more.setVisibility(View.VISIBLE);
                if (qty > 0){
                    qty--;
                    String newQty = Integer.toString(qty);
                    quantity.setText(newQty);
                  //  int ttlPrice = Integer.parseInt(totalPrice.getText().toString());
                    double ttlPrice = Double.parseDouble(totalPrice.getText().toString());
                    ttlPrice -= Double.parseDouble(pPrice.getText().toString());
                    String newTtlPrice = Double.toString(ttlPrice);
                    totalPrice.setText(newTtlPrice);
                }
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(quantity.getText().toString());
                if (qty == 1){
                    less.setVisibility(View.VISIBLE);
                }
                if (qty == maxQty[0]-1){
                    more.setVisibility(View.INVISIBLE);
                }
                if (qty < maxQty[0]) {
                    qty++;
                    String newQty = Integer.toString(qty);
                    quantity.setText(newQty);
                    double ttlPrice = Double.parseDouble(totalPrice.getText().toString());
                    ttlPrice += Double.parseDouble(pPrice.getText().toString());
                    String newTtlPrice = Double.toString(ttlPrice);
                    totalPrice.setText(newTtlPrice);
                }
            }
        });

        final String orderId = shp.getString("idOrder", null);
        addCArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject order = new JSONObject();
                try {
                    order.put("pid", id );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    order.put("oid", orderId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    order.put("pqty", quantity.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    order.put("price", totalPrice.getText().toString());
                }catch (JSONException e) {
                    e.printStackTrace();
                }



                String url = "http://51.210.7.226/addCart";

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, order,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.get("status").toString().equals("200")) {
                                        addCArt.setText("added");
                                        addCArt.setBackgroundColor(Color.parseColor("#42f57b"));
                                        SharedPreferences.Editor edit = shp.edit();
                                        edit.putString("emptyBasket", "false");
                                        edit.apply();

                                    }
                                }catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error){

                            }
                        });
                MySingleton.getInstance(ProductActivity.this).addToRequestQueue(jsonObjectRequest);
            }
        });
    }

    // Menu icons are inflated just as they were with actionbar
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_basket:
                Intent it = new Intent(ProductActivity.this, BasketActivity.class);
                startActivity(it);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
