package com.example.drivncook;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class BasketArticleAdapter extends BaseAdapter {

    private Context context;
    private List<BasketArticle> lArticle;

    public BasketArticleAdapter(Context context, List<BasketArticle> lArticle) {
        this.context = context;
        this.lArticle = lArticle;
    }

    @Override
    public int getCount() {
        return this.lArticle.size();
    }

    @Override
    public Object getItem(int position) {
        return this.lArticle.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            convertView = inflater.inflate(R.layout.activity_basket_article_list, null);
        }

        final LinearLayout linearLayout = convertView.findViewById(R.id.LineBasket);
        final TextView tv_n = convertView.findViewById(R.id.basket_article_name);
        TextView tv_p = convertView.findViewById(R.id.basket_article_price);
        TextView tv_q = convertView.findViewById(R.id.basket_article_quantity);
        Button delete = convertView.findViewById(R.id.btn_delete_basket);
        final BasketArticle a = (BasketArticle) getItem(position);

        tv_n.setText(a.getName());
        tv_p.setText(a.getPrice() + "€");
        tv_q.setText(a.getQuantity());
        final SharedPreferences shp = context.getSharedPreferences("order", Context.MODE_PRIVATE);
        final String idOrder = shp.getString("idOrder", null);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://51.210.7.226/deleteBasketArticle/" + a.getId() + "/" + idOrder + "/" + a.getName();
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    linearLayout.setVisibility(View.INVISIBLE);
                                    if (response.get("articles").equals("empty")) {

                                        SharedPreferences.Editor edit = shp.edit();
                                        edit.putString("emptyBasket", "true");
                                        edit.apply();
                                    }
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
                MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
            }
        });
        return convertView;
    }
}
