package com.example.drivncook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ArticleAdapter extends BaseAdapter {

    private Context context;
    private List<Article> lArticle;

    public ArticleAdapter(Context context, List<Article> lArticle) {
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
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(this.context);
            convertView = inflater.inflate(R.layout.activity_article_list, null);
        }

        TextView tv_n = convertView.findViewById(R.id.article_name);
        TextView tv_p = convertView.findViewById(R.id.article_price);

        Article a = (Article) getItem(position);

        tv_n.setText(a.getName());
        tv_p.setText(a.getPrice() + "€");

        return convertView;
    }
}
