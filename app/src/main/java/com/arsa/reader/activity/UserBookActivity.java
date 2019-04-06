package com.arsa.reader.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.arsa.reader.R;
import com.arsa.reader.adapter.book_adapter;
import com.arsa.reader.common.OnClickMaker;
import com.arsa.reader.model.BookModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class UserBookActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_book);
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
        Intent intent = getIntent();
        List_fill(Integer.parseInt(intent.getStringExtra("packageID")));

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new OnClickMaker(this));
    }

    public void List_fill(int id) {
        final Activity context = this;
        AndroidNetworking.get(getString(R.string.server_url) + "/Book/GetByPackageID/{id}")
                .addPathParameter("id", String.valueOf(id))
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(BookModel.class, new ParsedRequestListener<List<BookModel>>() {
                    @Override
                    public void onResponse(List<BookModel> bookList) {

                        book_adapter adapter = new book_adapter(context, bookList);

                        final ListView list = findViewById(R.id.lv_book);
                        if (list != null)
                            list.setAdapter(adapter);


                    }

                    @Override
                    public void onError(ANError anError) {


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
