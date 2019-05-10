package com.arsa.reader.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.arsa.reader.R;
import com.arsa.reader.adapter.cart_packages_adapter;
import com.arsa.reader.adapter.package_adapter;
import com.arsa.reader.common.OnClickMaker;
import com.arsa.reader.common.preferences;
import com.arsa.reader.common.util;
import com.arsa.reader.fragment.LoginFragment;
import com.arsa.reader.fragment.VerifyFragment;
import com.arsa.reader.model.ArsaResponse;
import com.arsa.reader.model.PackageModel;
import com.arsa.reader.model.UserModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;

import java.util.List;
import java.util.Set;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class ProfileActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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

        GetData();

        Button btnDone = findViewById(R.id.btnSave);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText TvName = findViewById(R.id.tv_Name);
                if (TvName.getText().length() <1)
                {
                    TvName.setError(" نام و نام خانوادگی صحیح نمی باشد");
                    return;
                }
                UserModel user = new UserModel();
                user.Name = TvName.getText().toString();
                user.SerialNo = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                preferences p =new preferences(ProfileActivity.this);
                user.Token=p.getstring("Key");

                AndroidNetworking.post(getString(R.string.server_url) + "/User/UpdateProfile")
                        .addBodyParameter(user)
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsObject(ArsaResponse.class, new ParsedRequestListener<ArsaResponse>() {
                            @Override
                            public void onResponse(ArsaResponse response) {
                                if (response.StatusCode.equals("200"))
                                {
                                    Toast.makeText(ProfileActivity.this,"پروفایل با موفقیت بروزرسانی شد.", Toast.LENGTH_LONG).show();

                                }
                                else if (response.StatusCode.equals("401"))
                                {
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
                                }
                                }
                            @Override
                            public void onError(ANError error) {
                                // handle error
                            }
                        });

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
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.cart).setVisible(false);
        super.onPrepareOptionsMenu(menu);
        return false;
    }

    public void GetData() {
        UserModel user = new UserModel();
        preferences p = new preferences(this);
        user.Token = p.getstring("Key");
        user.SerialNo =  Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        AndroidNetworking.post(getString(R.string.server_url) + "/User/GetProfile")
                .addBodyParameter(user)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(UserModel.class, new ParsedRequestListener<UserModel>() {

                    @Override
                    public void onResponse(UserModel response) {
                        TextView TvCellPhone = findViewById(R.id.txtCellPhone);
                        EditText txtName = findViewById(R.id.tv_Name);
                        Button btnSave = findViewById(R.id.btnSave);
                        Typeface titletypeface = Typeface.createFromAsset(getAssets(), "fonts/Vazir-Light-FD.otf");
                        txtName.setTypeface(titletypeface);
                        TvCellPhone.setTypeface(titletypeface);
                        btnSave.setTypeface(titletypeface);
                        if(!response.Name.equals(response.CellPhone))
                        {
                            txtName.setText(response.Name);
                        }

                        TvCellPhone.setText(response.CellPhone);
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });


    }

}
