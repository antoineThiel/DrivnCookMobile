package com.example.drivncook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MenuContentAdapter extends BaseAdapter {

    private Context context;
    private List<Article> larticles;

    public MenuContentAdapter(Context context, List<Article> larticles){
        this.context = context;
        this.larticles = larticles;
    }

    @Override
    public int getCount() {
       return this.larticles.size();
    }

    @Override
    public Object getItem(int position) {
        return this.larticles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(this.context);
            convertView =inflater.inflate(R.layout.activity_content_menu_list, null);
        }

        TextView tv_n = convertView.findViewById(R.id.cm_name);
        Article a = (Article) getItem(position);
        tv_n.setText(a.getName() + " " + a.getQuantity() + " " + a.getUnit());
        return convertView;
    }
}
