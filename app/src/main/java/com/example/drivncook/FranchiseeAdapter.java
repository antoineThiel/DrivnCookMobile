package com.example.drivncook;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FranchiseeAdapter extends RecyclerView.Adapter<FranchiseeAdapter.MyViewHolder> {

    private List<Franchisee> lfranchisee;
    private Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView lastName, firstName;
        private CardView cardView;
        private Context context;
        MyViewHolder(View v)
        {
            super(v);
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
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, FranchiseeActivity.class);
                it.putExtra("id", franchisee.getId());
                context.startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lfranchisee.size();
    }

}
