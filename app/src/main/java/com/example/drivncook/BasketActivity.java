package com.example.drivncook;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BasketActivity extends AppCompatActivity {

    private BasketArticle basketArticle;
    private List<BasketArticle> lBasketArticle = new ArrayList<>();
    private List<BasketArticle> lBasketMenu = new ArrayList<>();
    private String id;
    private JSONArray jArticles;
    private JSONArray jMenus;
    private ListView lvArticles;
    private ListView lvMenus;
    private TextView basket;
    private TextView ttlprice;
    private TextView vos_articles;
    private TextView vos_menus;
    private LinearLayout price;
    private TextView validate;
    private TextView fidelity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        lvArticles = findViewById(R.id.basket_article_list);
        lvMenus = findViewById(R.id.basket_menue_list);
        basket = findViewById(R.id.basket_id);
        ttlprice = findViewById(R.id.btotal_price);
        vos_articles = findViewById(R.id.b_articles);
        vos_menus = findViewById(R.id.b_Menus);
        price = findViewById(R.id.b_price);
        validate = findViewById(R.id.validate_basket);
        fidelity = findViewById(R.id.b_fidelity);

        final SharedPreferences shp = getSharedPreferences("order", MODE_PRIVATE);
        id = shp.getString("idOrder", null);
        final String idFranchisee = shp.getString("idFranchisee", null);
        final SharedPreferences shpLog = getSharedPreferences("logged", MODE_PRIVATE);
        String idCustomer = shpLog.getString("id", null);

        final String url = "http://51.210.7.226/getCurrentOrder/"+ id + "/" + idCustomer;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.get("order").toString().equals("nope")){
                                basket.setText("Votre panier est vide");
                                vos_articles.setVisibility(View.INVISIBLE);
                                lvArticles.setVisibility(View.INVISIBLE);
                                price.setVisibility(View.INVISIBLE);
                                validate.setVisibility(View.INVISIBLE);
                                lvMenus.setVisibility(View.INVISIBLE);
                                vos_menus.setVisibility(View.INVISIBLE);
                            }else{
                                if (response.has("article"))
                                {
                                    jArticles = response.getJSONArray("article");
                                    for (int i = 0; i < jArticles.length(); i++) {
                                        JSONObject temp = jArticles.getJSONObject(i);
                                        BasketArticle article = new BasketArticle(
                                                temp.get("name").toString(),
                                                temp.get("price").toString(),
                                                temp.get("quantity").toString(),
                                                temp.get("id").toString()
                                        );
                                        lBasketArticle.add(article);
                                        double price = Double.parseDouble(article.getPrice());
                                        double oldPrice = Double.parseDouble(ttlprice.getText().toString());
                                        oldPrice += price;
                                        ttlprice.setText(Double.toString(oldPrice));
                                    }
                                    final BasketArticleAdapter basketAdapter = new BasketArticleAdapter(BasketActivity.this, lBasketArticle);
                                    lvArticles.setAdapter(basketAdapter);
                                }else{
                                    vos_articles.setVisibility(View.INVISIBLE);
                                }

                                fidelity.setText(response.get("fidelity").toString());

                                if (response.has("menus")){
                                    jMenus = response.getJSONArray("menus");
                                    for (int i = 0; i<jMenus.length(); i++)
                                    {
                                        JSONObject temp = jMenus.getJSONObject(i);
                                        BasketArticle article = new BasketArticle(
                                                temp.get("name").toString(),
                                                temp.get("price").toString(),
                                                temp.get("quantity").toString(),
                                                temp.get("id").toString()
                                        );
                                        lBasketMenu.add(article);
                                        double price = Double.parseDouble(article.getPrice());
                                        double oldPrice = Double.parseDouble(ttlprice.getText().toString());
                                        oldPrice += price;
                                        ttlprice.setText(Double.toString(oldPrice));
                                    }
                                    final BasketArticleAdapter basketMenuAdapter = new BasketArticleAdapter(BasketActivity.this, lBasketMenu);
                                    lvMenus.setAdapter(basketMenuAdapter);
                                }else{
                                    vos_menus.setVisibility(View.INVISIBLE);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        //Add Request to the Queue.
        MySingleton.getInstance(BasketActivity.this).addToRequestQueue(jsonObjectRequest);

        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double prix = Double.parseDouble(ttlprice.getText().toString());
                double points = Double.parseDouble(fidelity.getText().toString());
                double reduction = 0;
                if (points >= prix)
                {
                    reduction = prix;
                }else if (points < prix){
                    reduction = points;
                }

                AlertDialog.Builder builder1 = new AlertDialog.Builder(BasketActivity.this);
                builder1.setTitle("Voulez-vous utiliser vos points de fidélités ? Une reduction de " + reduction + "€ est possible");
                final double finalReduction = reduction;
                builder1.setItems(new String[]{"Utiliser", "Non Merci !"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                final String urlValidateFid = "http://51.210.7.226/finalOrderFidelity/" + id + "/" + finalReduction + "/" + idFranchisee;
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                        (Request.Method.GET, urlValidateFid, null, new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    if (response.get("stat").toString().equals("200"))
                                                    {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(BasketActivity.this);
                                                        builder.setTitle("Commande en préparation");
                                                        builder.setMessage("Votre restaurant a reçu votre commande et va la préparer. Dirigez vous la bas pour payer et reçevoir votre commande");
                                                        builder.setPositiveButton("Revenir à l'accueil", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                SharedPreferences shp = getSharedPreferences("order", MODE_PRIVATE);
                                                                SharedPreferences.Editor edit = shp.edit();
                                                                edit.clear();
                                                                edit.apply();
                                                                Intent it = new Intent(BasketActivity.this, HomeActivity.class);
                                                                startActivity(it);
                                                            }
                                                        });
                                                        AlertDialog dialog = builder.create();
                                                        dialog.show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                            }
                                        });
                                //Add Request to the Queue.
                                MySingleton.getInstance(BasketActivity.this).addToRequestQueue(jsonObjectRequest);
                            case 1:
                                final String urlValidate = "http://51.210.7.226/finalOrder/" + id + "/" + idFranchisee;
                                JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest
                                        (Request.Method.GET, urlValidate, null, new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    if (response.get("stat").toString().equals("200"))
                                                    {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(BasketActivity.this);
                                                        builder.setTitle("Commande en préparation");
                                                        builder.setMessage("Votre restaurant a reçu votre commande et va la préparer. Dirigez vous la bas pour payer et reçevoir votre commande");
                                                        builder.setPositiveButton("Revenir à l'accueil", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                SharedPreferences shp = getSharedPreferences("order", MODE_PRIVATE);
                                                                SharedPreferences.Editor edit = shp.edit();
                                                                edit.clear();
                                                                edit.apply();
                                                                Intent it = new Intent(BasketActivity.this, HomeActivity.class);
                                                                startActivity(it);
                                                            }
                                                        });
                                                        AlertDialog dialog = builder.create();
                                                        dialog.show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                            }
                                        });
                                //Add Request to the Queue.
                                MySingleton.getInstance(BasketActivity.this).addToRequestQueue(jsonObjectRequest2);
                        }
                    }
                });

                AlertDialog dialog1 = builder1.create();
                dialog1.show();
            }
        });

    }


    // Menu icons are inflated just as they were with actionbar
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
