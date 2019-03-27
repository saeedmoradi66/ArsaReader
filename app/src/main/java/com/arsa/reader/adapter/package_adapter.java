package com.arsa.reader.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidnetworking.interfaces.ParsedRequestListener;
import com.arsa.reader.R;
import com.arsa.reader.model.PackageModel;

import java.util.List;

public class package_adapter extends ArrayAdapter<PackageModel> {
    private final Activity context;
    private final List<PackageModel> _Data;
    public package_adapter(Activity context, List<PackageModel> data) {
        super(context, R.layout.package_list,data);

        _Data=data;
        this.context=context;
    }


    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.package_list, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.tv_title);
        TextView priceText = (TextView) rowView.findViewById(R.id.tv_price);
        RatingBar ratingBar = (RatingBar) rowView.findViewById(R.id.ratingBar);
        final com.androidnetworking.widget.ANImageView package_image = rowView.findViewById(R.id.package_image);

        final String URL_IMAGE = "http://testclub.ir/showmenupic.ashx?id=25";
        package_image.setImageUrl(URL_IMAGE);

        Typeface titletypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Vazir-Light-FD.otf");
        titleText.setTypeface(titletypeface);
        titleText.setText(_Data.get(position).PackageTitle);

        Typeface pricetypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Vazir-Light-FD.otf");
        priceText.setTypeface(pricetypeface);
        priceText.setText(_Data.get(position).Price+" ریال ");
        ratingBar.setRating(_Data.get(position).Rate);
        return rowView;

    };
}
