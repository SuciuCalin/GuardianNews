package com.example.android.guardiannews;

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
 * Created by JukUm on 5/30/2017.
 */

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    //Returns a list of Editorial objects, that has been built up from parsing the given JSON response.
    public static List<Editorial> fetchData(String editorialJSON) {

        URL url = createUrl(editorialJSON);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making http request.", e);
        }

        return parseJson(jsonResponse);

    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem creating the URL", e);
        }
        return url;
    }

    //Make a HTTP request to the given URL, and return the response as a String
    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromSteam(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Guardian JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    //Converts the input stream into a String which contains the entire JSON response
    private static String readFromSteam(InputStream inputStream) throws IOException {
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


    private static List<Editorial> parseJson(String json) {
        List<Editorial> editorials = new ArrayList<>();

        try {
            JSONObject jsonResponse = new JSONObject(json);
            JSONObject response = jsonResponse.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject currentListObject = results.getJSONObject(i);

                String section = currentListObject.getString("sectionName");
                String title = currentListObject.getString("webTitle");
                String url = currentListObject.getString("webUrl");

                Editorial editorial = new Editorial(title, section, url);
                editorials.add(editorial);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the JSON results", e);
        }
        return editorials;
    }

}