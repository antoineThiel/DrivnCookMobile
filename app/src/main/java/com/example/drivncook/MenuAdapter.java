package com.example.drivncook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.util.List;

public class MenuAdapter extends BaseAdapter {

    private Context context;
    private List<Menu> lMenu;

    public MenuAdapter(Context context, List<Menu> lMenu) {
        this.context = context;
        this.lMenu = lMenu;
    }


    @Override
    public int getCount() {
        return this.lMenu.size();
    }

    @Override
    public Object getItem(int position) {
        return this.lMenu.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            convertView = inflater.inflate(R.layout.activity_menu_list, null);
        }

        TextView tv_n = convertView.findViewById(R.id.menu_name);
        TextView tv_p = convertView.findViewById(R.id.menu_price);
        CardView card = convertView.findViewById(R.id.cardMenu);

        final Menu m = (Menu) getItem(position);

        if (m.getReduc() == 1)
        {
            tv_p.setTextColor(Color.parseColor("#42f57b"));
        }else {
            tv_p.setTextColor(Color.parseColor("#000000"));
        }

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, MenuActivity.class);
                it.putExtra("id", m.getId());
                context.startActivity(it);
            }
        });

        tv_n.setText(m.getName());
        tv_p.setText(m.getPrice() + "€");

        return convertView;
    }
}
