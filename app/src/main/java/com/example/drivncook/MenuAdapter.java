package com.example.drivncook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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

        Menu m = (Menu) getItem(position);

        tv_n.setText(m.getName());
        tv_p.setText(m.getPrice() + "€");

        return convertView;
    }
}
