package com.example.drivncook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class FranchiseeAdapter extends RecyclerView.Adapter<FranchiseeAdapter.MyViewHolder> {

    private List<Franchisee> lfranchisee;
    private Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView lastName, firstName, position;
        private CardView cardView;
        private Context context;
        MyViewHolder(View v)
        {
            super(v);
            this.position = (TextView) v.findViewById(R.id.position);
            this.lastName = (TextView) v.findViewById(R.id.lastName);
            this.firstName = (TextView) v.findViewById(R.id.firstName);
            this.cardView = (CardView) v.findViewById(R.id.cardFranchisee);
            this.context = v.getContext();
        }

    }

    public FranchiseeAdapter(List<Franchisee> lfranchisee, Context context){
        this.lfranchisee = lfranchisee;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.franchisee_list, parent, false);
        return new FranchiseeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Franchisee franchisee = lfranchisee.get(position);
        holder.firstName.setText(franchisee.getFirstName());
        holder.lastName.setText(franchisee.getLastName());
        double distance = Double.parseDouble(franchisee.getDistance());
        int intDistance = (int) distance;
        if (intDistance >= 1000){
            intDistance = intDistance/1000;
            holder.position.setText(intDistance + "km - " +franchisee.getCity() + " - " + franchisee.getAddress());
        }else{
            holder.position.setText(intDistance + "m- " +franchisee.getCity() + " - " + franchisee.getAddress());
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SharedPreferences shp = context.getSharedPreferences("order", Context.MODE_PRIVATE);
                String idFranchisee = shp.getString("idFranchisee", null);
                String emptyBasket = shp.getString("emptyBasket", null);
                if (idFranchisee != null && !idFranchisee.equals(franchisee.getId()) && emptyBasket.equals("false"))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Si vous procédez, votre panier sera vidé.").setTitle("Une commande est en cours" + emptyBasket);
                    builder.setPositiveButton("Continuer", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final String url = "http://51.210.7.226/deleteBasket/" + shp.getString("idOrder", null);
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                String newOrderId = response.get("order").toString();
                                                SharedPreferences.Editor edit = shp.edit();
                                                edit.putString("idOrder", newOrderId);
                                                edit.apply();
                                                Intent it = new Intent(context, FranchiseeActivity.class);
                                                it.putExtra("id", franchisee.getId());
                                                context.startActivity(it);
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
                            MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
                        }
                    });
                    builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                    AlertDialog dialog = builder.create();

                }else{
                    Intent it = new Intent(context, FranchiseeActivity.class);
                    it.putExtra("id", franchisee.getId());
                    context.startActivity(it);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lfranchisee.size();
    }

}
