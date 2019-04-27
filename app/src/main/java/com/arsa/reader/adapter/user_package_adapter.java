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

import com.arsa.reader.R;
import com.arsa.reader.model.PackageModel;
import com.arsa.reader.model.UserPackagesModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class user_package_adapter extends ArrayAdapter<UserPackagesModel> {
    private final Activity context;
    private final List<UserPackagesModel> _Data;
    public user_package_adapter(Activity context, List<UserPackagesModel> data) {
        super(context, R.layout.user_package_list,data);

        _Data=data;
        this.context=context;
    }


    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.user_package_list, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.tv_title);
        final ImageView package_image = rowView.findViewById(R.id.package_image);

        final String URL_IMAGE = "http://testclub.ir/showmenupic.ashx?id=25";

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


        return rowView;

    };
}
