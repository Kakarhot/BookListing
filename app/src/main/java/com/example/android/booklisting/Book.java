package com.example.android.booklisting;

import java.util.ArrayList;

public class Book {
    private String mTitle;
    private ArrayList<String> mAuthor;
    private int mHasAuthor;

    public Book(String title, ArrayList<String> author) {
        mTitle = title;
        mAuthor = author;
        mHasAuthor = 1;
    }

    public Book(String title) {
        mTitle = title;
        mHasAuthor = 0;
    }

    public String getTitle(){
        return this.mTitle;
    }

    public String getAuthor(){
        if (mHasAuthor == 1) {
            StringBuilder output = new StringBuilder();
            for (int i = 0; i < mAuthor.size(); i++) {
                output.append(mAuthor.get(i));
                if (i < mAuthor.size() - 1)
                    output.append(", ");
            }
            return output.toString();
        }
        else
            return null;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
