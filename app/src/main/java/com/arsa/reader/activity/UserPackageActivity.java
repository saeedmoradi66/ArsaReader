package com.arsa.reader.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.arsa.reader.R;
import com.arsa.reader.adapter.user_package_adapter;
import com.arsa.reader.common.OnClickMaker;
import com.arsa.reader.common.preferences;
import com.arsa.reader.fragment.LoginFragment;
import com.arsa.reader.model.PackageModel;
import com.arsa.reader.model.UserModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class UserPackageActivity extends BaseActivity {

    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_package);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = findViewById(R.id.lv_package);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        List_fill();
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new OnClickMaker(this));
    }

    public void List_fill() {
        preferences p = new preferences(this);


        String cellPhone = p.getstring("Phone");
        String token = p.getstring("Key");
        String serialNo = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (token == null || cellPhone == null) {
            LoginFragment dialogFragment = new LoginFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean("notAlertDialog", true);
            dialogFragment.setArguments(bundle);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment prev = getSupportFragmentManager().findFragmentByTag("LoginDialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);
            dialogFragment.show(ft, "LoginDialog");
        } else {

            LoadList(cellPhone, token, serialNo);
        }


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

    public void LoadList(String cellPhone, String token, String serialNo) {
        UserModel user = new UserModel();
        user.CellPhone = cellPhone;
        user.Token = token;
        user.SerialNo = serialNo;

        AndroidNetworking.post(getString(R.string.server_url) + "/Package/GetUserPackages")
                .addBodyParameter(user)
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(PackageModel.class, new ParsedRequestListener<List<PackageModel>>() {
                    @Override
                    public void onResponse(final List<PackageModel> packagelist) {


                            SQLiteDatabase db=openOrCreateDatabase("Arsaa", MODE_PRIVATE,null);
                            db.execSQL("delete from Package");
                            for (PackageModel item : packagelist) {

                                db.execSQL("insert into Package(PackageID,PackageTitle) values('"+item.PackageID+"','"+item.PackageTitle+"')");
                            }
                        LoadOffline();
                    }

                    @Override
                    public void onError(ANError anError) {


                    }

                });
        LoadOffline();


    }
    private  void LoadOffline()
    {
        SQLiteDatabase db=openOrCreateDatabase("Arsaa", MODE_PRIVATE,null);
        Cursor crs=db.rawQuery("select * from Package", null);
        final List<PackageModel> packagelist=new ArrayList<PackageModel>();
        while(crs.moveToNext())
        {
            PackageModel model=new PackageModel();
            model.PackageID=crs.getInt(crs.getColumnIndex("PackageID"));
            model.PackageTitle=crs.getString(crs.getColumnIndex("PackageTitle"));
            packagelist.add(model);
        }

        user_package_adapter adapter = new user_package_adapter(this, packagelist);
        if (list != null)
        {
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Intent myIntent = new Intent(UserPackageActivity.this, UserBookActivity.class);
                    myIntent.putExtra("packageID", String.valueOf(packagelist.get(i).PackageID));
                    UserPackageActivity.this.startActivity(myIntent);
                }
            });
        }
    }
}
