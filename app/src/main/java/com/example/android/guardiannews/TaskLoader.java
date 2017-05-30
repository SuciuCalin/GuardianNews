package com.example.android.guardiannews;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by JukUm on 5/30/2017.
 */

public class TaskLoader extends AsyncTaskLoader<List<Editorial>> {

    private static final String LOG_TAG = TaskLoader.class.getName();
    private String mUrl;

    public TaskLoader(Context context, String url) {
        super(context);
        this.mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Editorial> loadInBackground() {

        if (mUrl == null) {
            return null;
        }
        return QueryUtils.fetchData(mUrl);

    }
}