package com.example.drivncook;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
    private List<Article> lArticle =  new ArrayList<>();
    private JSONArray jMenu;
    private JSONArray jArticle;
    private TextView textviewn;
    private ListView lvArticle;
    private ListView lvMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_franchisee);
        textviewn = findViewById(R.id.fr_name);
        lvArticle = findViewById(R.id.article_list);
        lvMenu = findViewById(R.id.menu_list);


        id = getIntent().getStringExtra("id");

        final SharedPreferences shp = getSharedPreferences("order",MODE_PRIVATE);
        SharedPreferences.Editor edit = shp.edit();
        edit.putString("idFranchisee", id);
        edit.apply();

        final String url ="http://10.0.2.2/getfranchiseeinfo/"+id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            franchisee = new Franchisee(
                                    id,
                                    response.get("lastname").toString(),
                                    response.get("firstname").toString()
                            );
                            jMenu = response.getJSONArray("menu");
                            for (int i =0; i<jMenu.length(); i++)
                            {
                                JSONObject temp = jMenu.getJSONObject(i);
                                Menu menu = new Menu(
                                        temp.get("name").toString(),
                                        temp.get("price").toString(),
                                        temp.get("id").toString()
                                );
                                lMenu.add(menu);
                            }

                            final MenuAdapter menuAdapter = new MenuAdapter(FranchiseeActivity.this, lMenu);
                            lvMenu.setAdapter(menuAdapter);


                            jArticle = response.getJSONArray("article");
                            for (int i = 0; i< jArticle.length(); i++)
                            {
                                JSONObject temp = jArticle.getJSONObject(i);
                                Article article = new Article(
                                        temp.get("name").toString(),
                                        temp.get("price").toString(),
                                        temp.get("unit").toString(),
                                        temp.get("quantity").toString(),
                                        temp.get("id").toString()
                                );
                                lArticle.add(article);
                            }

                            final ArticleAdapter articleAdapter = new ArticleAdapter(FranchiseeActivity.this, lArticle);
                            lvArticle.setAdapter(articleAdapter);

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
}
