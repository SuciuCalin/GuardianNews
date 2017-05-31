package com.example.android.guardiannews;

/**
 * Created by JukUm on 5/30/2017.
 */

public final class Editorial {

    private final String mEditorialTitle;    //title of the editorial
    private final String mEditorialSection;  //author of the editorial
    private final String mEditorialUrl;      //website url of the editorial

    /**
     * Create a new {@link Editorial} object with the title, section, and url
     *
     * @param editorialTitle    is the title of the editorial
     * @param editorialSection  is the section of the editorial
     * @param editorialUrl      is the website url of the editorial
     */
    public Editorial(String editorialTitle, String editorialSection, String editorialUrl) {
        this.mEditorialTitle = editorialTitle;
        this.mEditorialSection = editorialSection;
        this.mEditorialUrl = editorialUrl;
    }

    //Returns the title of the editorial
    public String getEditorialTitle() {
        return mEditorialTitle;
    }

    //Returns the section of the editorial
    public String getEditorialSection() {
        return mEditorialSection;
    }

    //Returns the website URL of the editorial
    public String getEditorialUrl() {
        return mEditorialUrl;
    }

}