package com.example.android.guardiannews;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Editorial>> {

    private static final int TASK_LOADER_ID = 1;
    private static final String GUARDIAN_API = "http://content.guardianapis.com/search?q=";
    private static final String API_KEY = "&api-key=2ce28575-3e4c-4f72-a28c-e0ffc0c5d965";

    private EditorialAdapter    mEditorialAdapter;
    private TextView            mNoResultsTextView;
    private RecyclerView        mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    private String mQuery = GUARDIAN_API + API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(TASK_LOADER_ID, null, this);

        mNoResultsTextView = (TextView) findViewById(R.id.no_results);
        mLayoutManager = new LinearLayoutManager(this);
        mEditorialAdapter = new EditorialAdapter(this, new ArrayList<Editorial>());

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mEditorialAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        android.widget.SearchView searchView = (android.widget.SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                mQuery = GUARDIAN_API + query + API_KEY;

                // If there is a network connection, fetch the data.
                // Else display the no connection error message.
                if (isInternetConnectionAvailable()) {
                    LoaderManager loaderManager = getLoaderManager();
                    loaderManager.restartLoader(TASK_LOADER_ID, null, MainActivity.this);
                } else {
                    Toast.makeText(MainActivity.this, R.string.no_internet_connection,
                            Toast.LENGTH_SHORT).show();
                    ProgressBar loadingSpinner = (ProgressBar) findViewById(R.id.progress_bar);
                    loadingSpinner.setVisibility(View.GONE);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    //Checks the state of the network connectivity.
    private boolean isInternetConnectionAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    @Override
    public Loader<List<Editorial>> onCreateLoader(int id, Bundle args) {

        Uri baseUri = Uri.parse(mQuery);
        return new TaskLoader(this, baseUri.toString());

    }

    @Override
    public void onLoadFinished(Loader<List<Editorial>> loader, List<Editorial> editorials) {

        ProgressBar loadingSpinner = (ProgressBar) findViewById(R.id.progress_bar);
        loadingSpinner.setVisibility(View.GONE);

        mRecyclerView.setVisibility(View.VISIBLE);
        mEditorialAdapter = new EditorialAdapter(MainActivity.this, new ArrayList<Editorial>());

        if (editorials != null && !editorials.isEmpty()) {
            mEditorialAdapter = new EditorialAdapter(MainActivity.this, editorials);
            mRecyclerView.setAdapter(mEditorialAdapter);

        } else {
            mNoResultsTextView.setText(R.string.no_results);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Editorial>> loader) {
        mEditorialAdapter = new EditorialAdapter(MainActivity.this, new ArrayList<Editorial>());

    }

}
