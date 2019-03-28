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

public class book_simple_adapter extends ArrayAdapter<BookModel> {
    private final Activity context;
    private final List<BookModel> _Data;

    public book_simple_adapter(Activity context, List<BookModel> data) {
        super(context, R.layout.book_simplelist, data);

        _Data = data;
        this.context = context;
    }


    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.book_simplelist, null, true);

        TextView titleText = rowView.findViewById(R.id.tv_title);

        Typeface titletypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Vazir-Light-FD.otf");
        titleText.setTypeface(titletypeface);
        titleText.setText(_Data.get(position).BookTitle);



        return rowView;

    }
}
