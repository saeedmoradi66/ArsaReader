package com.arsa.reader.adapter;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arsa.reader.R;
import com.arsa.reader.model.CategoryModel;

import java.util.List;

public class category_grid_adapter extends BaseAdapter {
    private LayoutInflater Inflater;
    private int ItemHeight = 0;
    private int NumColumns = 0;
    private List<CategoryModel> _Data;
    private Context _Context;
    private RelativeLayout.LayoutParams ImageViewLayoutParams;

    //Constructor
    public category_grid_adapter(Context context, List<CategoryModel> Data) {
        _Data = Data;
        _Context = context;
        Inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageViewLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    //Get Number Of Data
    public int getCount() {
        return _Data.size();
    }

    // get number of columns
    public int getNumColumns() {
        return NumColumns;
    }

    // set number of columns
    public void setNumColumns(int numColumns) {
        NumColumns = numColumns;
    }

    // set photo item height
    public void setItemHeight(int height) {
        if (height == ItemHeight) {
            return;
        }
        ItemHeight = height;
        ImageViewLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, ItemHeight);
        notifyDataSetChanged();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        if (view == null)
            view = Inflater.inflate(R.layout.photo_item, null);
        final com.androidnetworking.widget.ANImageView cover = view.findViewById(R.id.cover);
        TextView title = view.findViewById(R.id.title);
        cover.setLayoutParams(ImageViewLayoutParams);
        // Check the height matches our calculated column width
        if (cover.getLayoutParams().height != ItemHeight) {
            cover.setLayoutParams(ImageViewLayoutParams);
        }

        Typeface face = Typeface.createFromAsset(_Context.getAssets(), "fonts/BBCNassim.otf");
        title.setTypeface(face);
        final String URL_IMAGE = "http://testclub.ir/showmenupic.ashx?id=25";
        cover.setImageUrl(URL_IMAGE);
        title.setText(_Data.get(position % _Data.size()).CategoryTitle);

        return view;
    }
}