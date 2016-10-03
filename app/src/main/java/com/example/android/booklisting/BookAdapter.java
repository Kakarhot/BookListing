package com.example.android.booklisting;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter {

    public BookAdapter(Context context, ArrayList<Book> books){
        super(context,0,books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        Book book = (Book)getItem(position);

        TextView titleView = (TextView)listItemView.findViewById(R.id.title);
        TextView authorView = (TextView)listItemView.findViewById(R.id.author);
        titleView.setTypeface(null, Typeface.BOLD);
        authorView.setTypeface(null, Typeface.ITALIC);

        String title = book.getTitle();
        String author = book.getAuthor();

        titleView.setText(title);
        authorView.setText(author);

        return listItemView;
    }
}
