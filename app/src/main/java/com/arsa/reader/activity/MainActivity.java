package com.arsa.reader.activity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ConnectionQuality;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.arsa.reader.R;
import com.arsa.reader.adapter.category_grid_adapter;


import com.arsa.reader.common.OnClickMaker;
import com.arsa.reader.common.util;
import com.arsa.reader.model.CategoryModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        grid_fill();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new OnClickMaker(this));

        //create db
        SQLiteDatabase db=openOrCreateDatabase("Arsaa", MODE_PRIVATE,null);
        db.execSQL("create table if not exists Package (PackageID int,PackageTitle NVARCHAR(100))");
        db.execSQL("create table if not exists Books (BookID int,BookTitle NVARCHAR(100),author NVARCHAR(100))");
        db.close();

    }

    public void grid_fill() {
        final Context context = this;
        ///////////////////////
        AndroidNetworking.get(getString(R.string.server_url) + "/Category/GetAll")
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(CategoryModel.class, new ParsedRequestListener<List<CategoryModel>>() {
                    @Override
                    public void onResponse(List<CategoryModel> category) {
                        final int PhotoSize, PhotoSpacing;
                        PhotoSize = getResources().getDimensionPixelSize(R.dimen.grid_photo_size);
                        PhotoSpacing = getResources().getDimensionPixelSize(R.dimen.grid_photo_spacing);
                        final category_grid_adapter gridAdapter;
                        // initialize image adapter
                        gridAdapter = new category_grid_adapter(context, category);
                        final GridView categoryGrid = findViewById(R.id.Category_Grid);
                        // set image adapter to the GridView
                        categoryGrid.setAdapter(gridAdapter);
                        // get the view tree observer of the grid and set the height and numcols dynamically
                        categoryGrid.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                if (gridAdapter.getNumColumns() == 0) {
                                    final int numColumns = (int) Math.floor(categoryGrid.getWidth() / (PhotoSize + PhotoSpacing));
                                    if (numColumns > 0) {
                                        final int columnWidth = (categoryGrid.getWidth() / numColumns) - PhotoSpacing;
                                        gridAdapter.setNumColumns(numColumns);
                                        gridAdapter.setItemHeight(columnWidth);
                                    }
                                }
                            }

                        });
                        SetEvent(category);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.i("rah",anError.getMessage());

                    }

                });


    }

    private void SetEvent(final List<CategoryModel> data) {
        GridView grid = this.findViewById(R.id.Category_Grid);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Intent myIntent = new Intent(MainActivity.this, PackageActivity.class);
                myIntent.putExtra("categoryID", String.valueOf(data.get(position).CategoryID));
                MainActivity.this.startActivity(myIntent);

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


}
