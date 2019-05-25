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
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

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
        final ImageView package_image = rowView.findViewById(R.id.package_image);

        final String URL_IMAGE =this.context.getString(R.string.pic_url)+"/DownloadPackage/"+_Data.get(position).PackageID;
        Picasso.with(context).setIndicatorsEnabled(false);
       Picasso.with(context)
                .load(URL_IMAGE)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(package_image, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.v("Picasso"," fetch image");
                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(context)
                                .load(URL_IMAGE)
                                .into(package_image, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.v("Picasso","Could not fetch image");
                                    }
                                });
                    }
                });

        Typeface titletypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Vazir-Light-FD.otf");
        titleText.setTypeface(titletypeface);
        titleText.setText(_Data.get(position).PackageTitle);

        Typeface pricetypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Vazir-Light-FD.otf");
        priceText.setTypeface(pricetypeface);
        priceText.setText(_Data.get(position).Price+" ریال ");
        ratingBar.setRating(_Data.get(position).Score);
        return rowView;

    };
}
