package com.example.android.justearth;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.DecimalFormat;
import android.graphics.drawable.GradientDrawable;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class ArticleAdapter extends ArrayAdapter<Article> {

    private static final String LOCATION_SEPARATOR = " of ";

    public ArticleAdapter(Activity context, ArrayList<Article> articleArrayList) {
        super(context,  0, articleArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Article currentArticle = getItem(position);

        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title_text_view);
        String title = currentArticle.getTitle();
        titleTextView.setText(title);

        TextView sectionTextView = (TextView) listItemView.findViewById(R.id.section_text_view);
        String section = currentArticle.getSection();
        sectionTextView.setText(section);

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date_text_view);
        String date = currentArticle.getDate();
        if (date!=null) {
            date = date.substring(0, Math.min(date.length(), 10));
            dateTextView.setText(date);
        }

        TextView authorTextView = (TextView) listItemView.findViewById(R.id.author_text_view);
        String author = currentArticle.getAuthor();
        if (author!=null) {
            authorTextView.setText(author);
        }

        //add additional components of article object here
        return listItemView;
    }

}

