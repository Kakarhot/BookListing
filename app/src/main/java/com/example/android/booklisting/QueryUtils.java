package com.example.android.booklisting;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public final class QueryUtils {


    private static final String LOG_TAG = QueryUtils.class.getSimpleName();


    private QueryUtils() {
    }

    public static ArrayList<Book> fetchBookData(String requestedUrl){
        URL url = createUrl(requestedUrl);
        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(url);
        }
        catch (IOException e){
            Log.e(LOG_TAG,"Error closing input stream");
        }

        ArrayList<Book> books = extractBooks(jsonResponse);

        return books;
    }

    private static URL createUrl(String requestedUrl){
        URL url = null;
        try{
            url = new URL(requestedUrl);
        }
        catch(MalformedURLException e){
            Log.e(LOG_TAG,"Problem building the URL");
        }
        return url;
    }


    private static String makeHttpRequest(URL url)throws IOException{
        String jsonResponse = "";

        if(url == null)
            return jsonResponse;

        HttpURLConnection urlConnection = null;
        InputStream stream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                stream = urlConnection.getInputStream();
                jsonResponse = readFromStream(stream);
            } else
                Log.e(LOG_TAG, "Error response code:" + urlConnection.getResponseCode());
        }
        catch (IOException e){
            Log.e(LOG_TAG,"Problem retrieving book JSON results");
        }
        finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(stream != null){
                stream.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream stream) throws IOException{
        StringBuilder output = new StringBuilder();
        if (stream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(stream,Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line != null){
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }

    private static ArrayList<Book> extractBooks(String jsonResponse) {

        if (TextUtils.isEmpty(jsonResponse)){
            return null;
        }

        ArrayList<Book> books = new ArrayList<>();

        try {
            JSONObject JSONRootObject = new JSONObject(jsonResponse);
            if(JSONRootObject.has("items")) {
                JSONArray jsonArray = JSONRootObject.optJSONArray("items");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject items = jsonArray.getJSONObject(i);
                    JSONObject volumeInfo = items.optJSONObject("volumeInfo");
                    String title = volumeInfo.optString("title");
                    if (volumeInfo.has("authors")) {
                        JSONArray authorsArray = volumeInfo.optJSONArray("authors");
                        ArrayList<String> authors = new ArrayList<>();
                        for (int j = 0; j < authorsArray.length(); j++) {
                            authors.add(authorsArray.getString(j));
                        }
                        books.add(new Book(title, authors));
                    } else
                        books.add(new Book(title));
                }
            }
            else
                return null;
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the book JSON results", e);
        }

        return books;
    }
}
