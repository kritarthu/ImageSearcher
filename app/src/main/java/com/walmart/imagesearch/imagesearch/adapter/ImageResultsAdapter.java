package com.walmart.imagesearch.imagesearch.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.walmart.imagesearch.R;
import com.walmart.imagesearch.imagesearch.model.ImageResults;

import java.util.List;

/**
 * Created by kupadhy on 10/18/15.
 */
public class ImageResultsAdapter extends ArrayAdapter<ImageResults>{
    public ImageResultsAdapter(Context context, List<ImageResults> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageResults imageInfo = getItem(position);
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result,parent,false);
        }
        ImageView ivImage =(ImageView)convertView.findViewById(R.id.ivImage);
        TextView tvTitle =(TextView)convertView.findViewById(R.id.tvTitle);
        ivImage.setImageResource(0);
        tvTitle.setText(Html.fromHtml(imageInfo.title));
        Picasso.with(getContext()).load(imageInfo.thumburl).into(ivImage);
        return convertView;
    }
}
