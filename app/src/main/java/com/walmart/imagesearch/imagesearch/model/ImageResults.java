package com.walmart.imagesearch.imagesearch.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kupadhy on 10/18/15.
 */
public class ImageResults implements Serializable{
    public String fullUrl;
    public String thumburl;
    public String title;

    public ImageResults(JSONObject jsonObject) {
        try {
            this.fullUrl = jsonObject.getString("url");
            this.thumburl = jsonObject.getString("tbUrl");
            this.title = jsonObject.getString("title");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ImageResults>  fromJSONArray(JSONArray jsonArray) {
        ArrayList<ImageResults> imageResultArray = new ArrayList<ImageResults>();
        Log.d("INFO", "Outside loop");
        for(int i = 0 ; i < jsonArray.length(); i++) {
            try {
                Log.d("INFO", "Inside loop");
                imageResultArray.add(new ImageResults(jsonArray.getJSONObject(i)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return imageResultArray;
    }
}