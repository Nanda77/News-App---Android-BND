package com.example.android.newsguardian;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Nanda on 16/11/16.
 */

public class ArticleAdapter extends ArrayAdapter<Article> {


    public ArticleAdapter(Context context, List<Article> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }


        Article currentArticle = getItem(position);

        TextView articleTitle = (TextView) listItemView.findViewById(R.id.article_title);
        articleTitle.setText(currentArticle.getmTitle());

        TextView articleSection = (TextView) listItemView.findViewById(R.id.section);
        articleSection.setText(currentArticle.getmSection());

        TextView datePublished = (TextView) listItemView.findViewById(R.id.date_published);
        datePublished.setText(currentArticle.getmDate());

        return listItemView;
    }

}



