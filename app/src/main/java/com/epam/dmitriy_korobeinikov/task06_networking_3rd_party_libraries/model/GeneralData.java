package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.ArrayList;

/**
 * Created by Dmitriy_Korobeinikov on 12/15/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeneralData {

    private int total_count;
    private boolean incomplete_results;
    private ArrayList<ItemsData> items;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GeneralData{");
        sb.append("total_count=").append(total_count);
        sb.append(", incomplete_results=").append(incomplete_results);
        sb.append(", items=").append(items);
        sb.append('}');
        return sb.toString();
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public boolean isIncomplete_results() {
        return incomplete_results;
    }

    public void setIncomplete_results(boolean incomplete_results) {
        this.incomplete_results = incomplete_results;
    }

    public ArrayList<ItemsData> getItems() {
        return items;
    }

    public void setItems(ArrayList<ItemsData> items) {
        this.items = items;
    }
}
