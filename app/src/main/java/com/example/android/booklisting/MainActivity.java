
package com.example.android.booklisting;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {

    public static final String LOG_TAG = MainActivity.class.getName();
    private static final String REQUEST_URL = "https://www.googleapis.com/books/v1/volumes";
    private static final String API_KEY= "AIzaSyDuEbc0Bb42CHuaDSmE1B9wz1xv3OU-Fuo";

    private BookAdapter mAdapter;
    private ListView bookListView;
    private TextView emptyView;
    private ProgressBar progressBar;
    private android.widget.SearchView searchView;
    private String bookSearch;
    private BookLoader bookLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_activity);

        mAdapter = new BookAdapter(this,new ArrayList<Book>());
        bookListView = (ListView) findViewById(R.id.list);
        emptyView = (TextView)findViewById(R.id.empty_view);
        progressBar = (ProgressBar)findViewById(R.id.loading_spinner);
        searchView = (SearchView) findViewById(R.id.search_view);

        bookListView.setEmptyView(emptyView);
        bookListView.setAdapter(mAdapter);
        progressBar.setVisibility(View.GONE);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String queryText) {
                bookSearch = queryText;
                getLoaderManager().destroyLoader(0);
                ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE) ;
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if(networkInfo != null && networkInfo.isConnected()) {
                    getLoaderManager().initLoader(0, null, MainActivity.this);
                    progressBar.setVisibility(View.VISIBLE);
                }
                else{
                    emptyView.setText(R.string.no_internet_connection);
                }

                return true;
            }
        });
        if(getLoaderManager().getLoader(0) != null)
            getLoaderManager().initLoader(0, null, MainActivity.this);
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {;
        Uri baseUri = Uri.parse(REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("q",bookSearch);
        uriBuilder.appendQueryParameter("key",API_KEY);
        bookLoader = new BookLoader(this,uriBuilder.toString());
        return bookLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        progressBar.setVisibility(View.GONE);
        mAdapter.clear();
        if(books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
        else
            emptyView.setText(R.string.no_book_found);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        mAdapter.clear();
    }
}
