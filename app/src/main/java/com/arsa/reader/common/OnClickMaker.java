package com.arsa.reader.common;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.arsa.reader.activity.UserPackageActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class OnClickMaker implements View.OnClickListener {
    private Context context;

    public OnClickMaker(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), UserPackageActivity.class);
        view.getContext().startActivity(intent);
    }
}