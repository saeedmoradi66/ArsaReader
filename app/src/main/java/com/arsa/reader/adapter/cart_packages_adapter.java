package com.arsa.reader.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.arsa.reader.R;
import com.arsa.reader.activity.CartActivity;
import com.arsa.reader.common.preferences;
import com.arsa.reader.model.PackageModel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class cart_packages_adapter extends ArrayAdapter<PackageModel> {
    private final Activity context;
    private final List<PackageModel> _Data;
    public cart_packages_adapter(Activity context, List<PackageModel> data) {
        super(context, R.layout.package_list,data);

        _Data=data;
        this.context=context;
    }


    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.cart_package_list, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.tv_title);
        TextView priceText = (TextView) rowView.findViewById(R.id.tv_price);
        TextView deleteText = (TextView) rowView.findViewById(R.id.tv_Delete);
        final com.androidnetworking.widget.ANImageView package_image = rowView.findViewById(R.id.package_image);

        final String URL_IMAGE =  this.context.getString(R.string.pic_url)+"/DownloadPackage/"+_Data.get(position).PackageID;
        package_image.setImageUrl(URL_IMAGE);

        Typeface titletypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Vazir-Light-FD.otf");
        titleText.setTypeface(titletypeface);
        titleText.setText(_Data.get(position).PackageTitle);

        deleteText.setTypeface(titletypeface);
        deleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences p =new preferences(context);
                Set<String> set ;
                set=p.getstringset("Cart");
                if(set==null)
                {
                    set = new HashSet<String>();
                }
                if(set.size()>0)
                {
                    set.remove(String.valueOf(_Data.get(position).PackageID));
                    p.setstringset("Cart",set);
                    _Data.remove(position);
                    notifyDataSetChanged();
                }
                UpdateTotalPrice();
            }
        });

        Typeface pricetypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Vazir-Light-FD.otf");
        priceText.setTypeface(pricetypeface);
        priceText.setText(_Data.get(position).Price+" ریال ");
        UpdateTotalPrice();
        return rowView;

    };
    private  void UpdateTotalPrice()
    {
        int TotalPrice=0;
        for (PackageModel item : _Data) {
            TotalPrice+=item.Price;
        }
        ((CartActivity)context).SetPrice(TotalPrice);
    }
}
