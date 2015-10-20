package com.walmart.imagesearch.imagesearch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;

import com.walmart.imagesearch.R;
import com.walmart.imagesearch.imagesearch.model.ImageFilters;

public class SearchFilter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSubmit(View v) {
        Spinner spColor = (Spinner) findViewById(R.id.spColor);
        Spinner spSite = (Spinner) findViewById(R.id.spSite);
        Spinner spSize = (Spinner) findViewById(R.id.spSize);
        Spinner spType = (Spinner) findViewById(R.id.spType);
        ImageFilters imageFilter = new ImageFilters();
        imageFilter.color = spColor.getSelectedItem().toString();
        imageFilter.type = spType.getSelectedItem().toString();
        imageFilter.size = spSize.getSelectedItem().toString();
        imageFilter.site = spSite.getSelectedItem().toString();
        // Prepare data intent
        Intent data = new Intent();
        // Pass relevant data back as a result
        data.putExtra("filter", imageFilter);
        data.putExtra("code", 200); // ints work too
        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }
}
