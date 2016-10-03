package com.example.android.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    private String mUrl;
    private static final String LOG_TAG = BookLoader.class.getSimpleName();

    public BookLoader(Context context,String url) {
        super(context);
        mUrl = url;
    }

    @Override
    public ArrayList<Book> loadInBackground(){
        if(mUrl == null){
            return null;
        }

        ArrayList<Book> books = QueryUtils.fetchBookData(mUrl);
        return books;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
