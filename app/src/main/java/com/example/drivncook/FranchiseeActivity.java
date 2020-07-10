package com.example.drivncook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

public class FranchiseeActivity extends AppCompatActivity {

    private String id;
    private Franchisee franchisee;
    private List<Menu> lMenu = new ArrayList<>();
    private List<Article> lArticle = new ArrayList<>();
    private JSONArray jMenu;
    private JSONArray jArticle;
    private TextView textviewn;
    private ListView lvArticle;
    private ListView lvMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_franchisee);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        textviewn = findViewById(R.id.fr_name);
        lvArticle = findViewById(R.id.article_list);
        lvMenu = findViewById(R.id.menu_list);
        id = getIntent().getStringExtra("id");

        final SharedPreferences shp = getSharedPreferences("order", MODE_PRIVATE);
        SharedPreferences.Editor edit = shp.edit();
        edit.putString("idFranchisee", id);
        edit.apply();

        final String url = "http://51.210.7.226/getfranchiseeinfo/" + id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            franchisee = new Franchisee(
                                    id,
                                    response.get("lastname").toString(),
                                    response.get("firstname").toString(),
                                    "null",
                                    "null",
                                    "null"
                            );
                            if (response.has("menu"))
                            {
                                jMenu = response.getJSONArray("menu");
                                for (int i = 0; i < jMenu.length(); i++) {
                                    JSONObject temp = jMenu.getJSONObject(i);
                                    Menu menu = new Menu(
                                            temp.get("name").toString(),
                                            temp.get("price").toString(),
                                            temp.get("id").toString()
                                    );
                                    if (temp.get("reduc").equals("1")){
                                        menu.setReduc(1);
                                    }else{
                                        menu.setReduc(0);
                                    }
                                    lMenu.add(menu);
                                }

                                final MenuAdapter menuAdapter = new MenuAdapter(FranchiseeActivity.this, lMenu);
                                lvMenu.setAdapter(menuAdapter);
                            }
                            if (response.has("article")){
                                jArticle = response.getJSONArray("article");
                                for (int i = 0; i < jArticle.length(); i++) {
                                    JSONObject temp = jArticle.getJSONObject(i);
                                    Article article = new Article(
                                            temp.get("name").toString(),
                                            temp.get("price").toString(),
                                            temp.get("unit").toString(),
                                            temp.get("quantity").toString(),
                                            temp.get("id").toString()
                                    );
                                    if (temp.get("reduc").equals("1")){
                                        article.setReduc(1);
                                    }else{
                                        article.setReduc(0);
                                    }
                                    lArticle.add(article);
                                }

                                final ArticleAdapter articleAdapter = new ArticleAdapter(FranchiseeActivity.this, lArticle);
                                lvArticle.setAdapter(articleAdapter);
                            }




                            textviewn.setText(franchisee.getLastName() + " " + franchisee.getFirstName());

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
        MySingleton.getInstance(FranchiseeActivity.this).addToRequestQueue(jsonObjectRequest);

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
                Intent it = new Intent(FranchiseeActivity.this, BasketActivity.class);
                startActivity(it);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
