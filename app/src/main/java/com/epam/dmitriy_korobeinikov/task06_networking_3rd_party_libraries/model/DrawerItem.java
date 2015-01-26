package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model;

/**
 * Created by Dmitriy Korobeynikov on 1/23/2015.
 */
public class DrawerItem {

    private String mTitle;
    private int mIcon;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getIcon() {
        return mIcon;
    }

    public void setIcon(int icon) {
        mIcon = icon;
    }

    public DrawerItem(String title, int icon) {
        mTitle = title;
        mIcon = icon;

    }
}
