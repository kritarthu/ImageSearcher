package com.walmart.imagesearch.imagesearch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.walmart.imagesearch.R;
import com.walmart.imagesearch.imagesearch.adapter.ImageResultsAdapter;
import com.walmart.imagesearch.imagesearch.helper.EndlessScrollListener;
import com.walmart.imagesearch.imagesearch.model.ImageFilters;
import com.walmart.imagesearch.imagesearch.model.ImageResults;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ImageSearch extends AppCompatActivity {

    private GridView gvImageGrid;
    private String url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0";
    private ArrayList<ImageResults> imageResults;
    private ImageResultsAdapter imageResultsAdapter;
    String query = "";
    private final int REQUEST_CODE = 1;
    ImageFilters filter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);
        setupViews();
        gvImageGrid = (GridView) findViewById(R.id.gvResults);
        imageResults = new ArrayList<ImageResults>();
        imageResultsAdapter = new ImageResultsAdapter(this, imageResults);
        gvImageGrid.setAdapter(imageResultsAdapter);
    }

    private void setupViews() {
        gvImageGrid = (GridView) findViewById(R.id.gvResults);
        gvImageGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ImageSearch.this, ImageDisplayActivity.class);
                ImageResults imageResult = imageResults.get(position);
                i.putExtra("image", imageResult);
                startActivity(i);
            }
        });

        gvImageGrid.setOnScrollListener(new EndlessScrollListener() {

            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                callAPI(page, totalItemsCount);
                imageResultsAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchText) {
                query = searchText;
                imageResultsAdapter.clear();
                callAPI(0, 8);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(ImageSearch.this, SearchFilter.class);
            startActivityForResult(i, REQUEST_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void callAPI(int offset, int count) {
        Toast.makeText(this, "Loading images", Toast.LENGTH_SHORT).show();

        AsyncHttpClient httpClient = new AsyncHttpClient();
        Log.d("INFO", count+"");
        httpClient.get(getUrl() + "&rsz=" + count + "&start=" + offset*count + "&q=" + this.query, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("INFO", response.toString());
                JSONArray imageResult = null;
                try {
                    imageResult = response.getJSONObject("responseData").getJSONArray("results");
                    imageResultsAdapter.addAll(ImageResults.fromJSONArray(imageResult));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // ActivityOne.java, time to handle the result of the sub-activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            filter = (ImageFilters)data.getExtras().get("filter");
            int code = data.getExtras().getInt("code", 0);
            imageResultsAdapter.clear();
            if(filter!=null) {
                callAPI(0, 8);
            }
        }
    }

    private String getUrl() {
        String filterUrl = url;
        if(filter!=null) {
            if(filter.site!="all") {
                filterUrl = filterUrl+"&as_sitesearch="+filter.site;
            }
            if(filter.color!="all") {
                filterUrl = filterUrl+"&imgcolor="+filter.color;
            }
            if(filter.size!="all") {
                filterUrl = filterUrl +"&imgsz="+filter.size;
            }
            if(filter.type!="all") {
                filterUrl = filterUrl+"&imgtype="+filter.type;
            }
        }
        return filterUrl;
    }
}