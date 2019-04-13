package com.arsa.reader.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.arsa.reader.R;
import com.arsa.reader.model.BookModel;
import com.arsa.reader.model.CommentModel;

import java.util.List;

public class comment_adapter extends ArrayAdapter<CommentModel> {
    private final Activity context;
    private final List<CommentModel> _Data;

    public comment_adapter(Activity context, List<CommentModel> data) {
        super(context, R.layout.comment_list, data);

        _Data = data;
        this.context = context;
    }


    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.comment_list, null, true);
        Log.i("rah",_Data.get(position).CommentText);

        TextView commentText = rowView.findViewById(R.id.tv_comment);
        TextView dateText = rowView.findViewById(R.id.tv_date);
        TextView timeText = rowView.findViewById(R.id.tv_time);

        Typeface titletypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Vazir-Light-FD.otf");
        commentText.setTypeface(titletypeface);
        commentText.setText(_Data.get(position).CommentText);

        Typeface pricetypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Vazir-Light-FD.otf");
        dateText.setTypeface(pricetypeface);
        dateText.setText(_Data.get(position).Date);

        timeText.setTypeface(pricetypeface);
        timeText.setText(_Data.get(position).Time);

        return rowView;

    }
}
