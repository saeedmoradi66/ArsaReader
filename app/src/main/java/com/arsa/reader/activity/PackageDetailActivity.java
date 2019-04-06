package com.arsa.reader.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.arsa.reader.R;
import com.arsa.reader.adapter.book_adapter;
import com.arsa.reader.adapter.book_simple_adapter;
import com.arsa.reader.adapter.pager_adapter;
import com.arsa.reader.common.OnClickMaker;
import com.arsa.reader.model.BookModel;
import com.arsa.reader.model.PackageModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.text.HtmlCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;


public class PackageDetailActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        GetData(Integer.parseInt(intent.getStringExtra("packageID")));
        GetBooks(Integer.parseInt(intent.getStringExtra("packageID")));

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new OnClickMaker(this));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final pager_adapter adapter= new pager_adapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void GetData(int id) {
        final Activity context = this;
        AndroidNetworking.get(getString(R.string.server_url) + "/Package/GetByID/{id}")
                .addPathParameter("id", String.valueOf(id))
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(PackageModel.class, new ParsedRequestListener<PackageModel>() {
                    @Override
                    public void onResponse(final PackageModel packagedetail) {

                        TextView titleText = (TextView) findViewById(R.id.tv_title);
                        Typeface titletypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Vazir-Light-FD.otf");
                        titleText.setTypeface(titletypeface);
                        titleText.setText(packagedetail.PackageTitle);

                        final com.androidnetworking.widget.ANImageView package_image = findViewById(R.id.package_image);
                        final String URL_IMAGE = "http://testclub.ir/showmenupic.ashx?id=25";
                        package_image.setImageUrl(URL_IMAGE);

                        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
                        ratingBar.setRating(packagedetail.Score);

                        TextView priceText = (TextView) findViewById(R.id.tv_price);
                        Typeface pricetypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Vazir-Light-FD.otf");
                        priceText.setTypeface(pricetypeface);
                        priceText.setText(packagedetail.Price+" ریال ");

                        TextView descriptionText = (TextView) findViewById(R.id.tv_description);
                        descriptionText.setText(HtmlCompat.fromHtml(packagedetail.Description, 0) );
                    }

                    @Override
                    public void onError(ANError anError) {


                    }

                });
    }
    public void GetBooks(int id) {
        final Activity context = this;
        AndroidNetworking.get(getString(R.string.server_url) + "/Book/GetByPackageID/{id}")
                .addPathParameter("id", String.valueOf(id))
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(BookModel.class, new ParsedRequestListener<List<BookModel>>() {
                    @Override
                    public void onResponse(List<BookModel> bookList) {

                        book_simple_adapter adapter = new book_simple_adapter(context, bookList);

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
