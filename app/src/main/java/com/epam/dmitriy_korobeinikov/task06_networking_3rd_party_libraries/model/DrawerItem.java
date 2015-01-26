package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model;

/**
 * Created by Dmitriy Korobeynikov on 1/23/2015.
 */
public class DrawerItem {

    private String title;
    private int icon;

    public DrawerItem(String title, int icon) {
        this.title = title;
        this.icon = icon;

    }

    public String getTitle() {
        return this.title;
    }

    public int getIcon() {
        return this.icon;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
