package com.arsa.reader.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.arsa.reader.R;
import com.arsa.reader.adapter.package_adapter;
import com.arsa.reader.common.OnClickMaker;
import com.arsa.reader.common.preferences;
import com.arsa.reader.model.PackageModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Set;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


public class CartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
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
        List_fill();

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new OnClickMaker(this));
    }

    public void List_fill() {
        final Activity context = this;
        preferences p =new preferences(this);
        Set<String> set =p.getstringset("Cart");
        String IDs="";
        for (String s : set) {
            IDs+=s;
            IDs+="$";
        }
        IDs += IDs.substring(0, IDs.length() - 1);

        AndroidNetworking.get(getString(R.string.server_url) + "/Package/GetByIDs/{id}")
                .addPathParameter("id", String.valueOf(IDs))
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(PackageModel.class, new ParsedRequestListener<List<PackageModel>>() {
                    @Override
                    public void onResponse(final List<PackageModel> packagelist) {

                        package_adapter adapter=new package_adapter(context,packagelist);

                        ListView list = findViewById(R.id.lv_package);
                        if(list!=null)
                        list.setAdapter(adapter);

                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                Intent myIntent = new Intent(CartActivity.this, PackageDetailActivity.class);
                                myIntent.putExtra("packageID", String.valueOf(packagelist.get(i).PackageID));
                                CartActivity.this.startActivity(myIntent);
                            }
                        });
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
