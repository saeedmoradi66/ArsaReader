package com.arsa.reader.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.arsa.reader.R;
import com.arsa.reader.model.BookModel;

import java.util.List;

public class book_adapter extends ArrayAdapter<BookModel> {
    private final Activity context;
    private final List<BookModel> _Data;

    public book_adapter(Activity context, List<BookModel> data) {
        super(context, R.layout.book_list, data);

        _Data = data;
        this.context = context;
    }


    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.book_list, null, true);

        TextView titleText = rowView.findViewById(R.id.tv_title);
        TextView contentText = rowView.findViewById(R.id.tv_Content);
        final com.androidnetworking.widget.ANImageView book_image = rowView.findViewById(R.id.book_image);

        final String URL_IMAGE = "http://testclub.ir/showmenupic.ashx?id=25";
        book_image.setImageUrl(URL_IMAGE);

        Typeface titletypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Vazir-Light-FD.otf");
        titleText.setTypeface(titletypeface);
        titleText.setText(_Data.get(position).BookTitle);

        Typeface pricetypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Vazir-Light-FD.otf");
        contentText.setTypeface(pricetypeface);
        contentText.setText(_Data.get(position).Contents);

        return rowView;

    }
}
