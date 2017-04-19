package com.example.android.newsguardian;

/**
 * Created by Nanda on 16/11/16.
 */

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
import java.util.List;

/**
 * Created by Nanda on 16/11/16.
 */

public final class QueryUtilities {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtilities.class.getSimpleName();


    private QueryUtilities() {
    }


    public static List<Article> fetchGuardianData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem while making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of articles
        List<Article> articles = extractFeatureFromJson(jsonResponse);


        return articles;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Guardian News JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the  InputStream into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of  Article objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Article> extractFeatureFromJson(String guardianNewsResponse) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(guardianNewsResponse)) {
            return null;
        }

        // Create an empty ArrayList
        List<Article> articles = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(guardianNewsResponse);

            JSONObject initialObject = baseJsonResponse.getJSONObject("response");

            // Extract the JSONArray associated with the key called "items",
            // which represents a list of items.
            JSONArray resultsArray = initialObject.getJSONArray("results");


            for (int i = 0; i < resultsArray.length(); i++) {


                JSONObject currentArticle = resultsArray.getJSONObject(i);

                String title = currentArticle.getString("webTitle");


                String section = currentArticle.getString("sectionName");

                String publishedDate = currentArticle.getString("webPublicationDate");

                String date = publishedDate.replace("T", "   ");
                String datePublished = date.replace("Z", " Hrs");

                String url = currentArticle.getString("webUrl");

                // Create a new Article object with the magnitude, location, time,
                // and url from the JSON response.
                Article article = new Article(title, section, datePublished, url);

                // Add the new article to the list of articles.
                articles.add(article);
            }

        } catch (JSONException e) {

            Log.e("QueryUtilities", "Problem parsing the Guardian News results", e);
        }

        return articles;
    }

}

