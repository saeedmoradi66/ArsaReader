package com.arsa.reader.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arsa.reader.R;
import com.arsa.reader.common.OnClickMaker;
import com.arsa.reader.common.preferences;
import com.arsa.reader.fragment.LoginFragment;
import com.arsa.reader.fragment.VerifyFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, VerifyFragment.OnFragmentCloseListener  {
    private int CartCounter = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        refreshNavigation();

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account) {

            preferences p =new preferences(this);
            if(p.getstring("Key")==null)
            {
               signin();
            }
            else
            {
                Intent myIntent = new Intent(BaseActivity.this, ProfileActivity.class);
                BaseActivity.this.startActivity(myIntent);
            }

        }
        else if (id == R.id.nav_signout) {
            signOut();
        }
        else if (id == R.id.nav_signin){
            signin();
        }
        else if (id == R.id.nav_home){
            Intent myIntent = new Intent(BaseActivity.this, MainActivity.class);
            BaseActivity.this.startActivity(myIntent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart, menu);
        MenuItem item = menu.findItem(R.id.cart);
        MenuItemCompat.setActionView(item, R.layout.cart_layout);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);

        RefreshCart(notifCount);

        MenuItemCompat.getActionView(menu.findItem(R.id.cart)).findViewById(R.id.btnCart).setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                Intent myIntent = new Intent(BaseActivity.this, CartActivity.class);
                BaseActivity.this.startActivity(myIntent);

            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void SetCartItem(int packageID)
    {
        preferences p =new preferences(this);
        Set<String> set ;
        set=p.getstringset("Cart");
        if(set==null)
        {
            set = new HashSet<String>();
        }
        set.add(String.valueOf(packageID));
        p.setstringset("Cart",set);
        TextView tv = (TextView) findViewById(R.id.tvCartCount);
        Typeface pricetypeface = Typeface.createFromAsset(this.getAssets(), "fonts/Vazir-Bold-FD.otf");
        tv.setTypeface(pricetypeface);
        tv.setText(String.valueOf(set.size()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        RefreshCart(null);

    }
    public  void RefreshCart(RelativeLayout CartLayout)
    {
        preferences p =new preferences(this);
        Set<String> set ;
        set=p.getstringset("Cart");
        TextView tv;
        if(CartLayout==null)
        {
             tv = (TextView) findViewById(R.id.tvCartCount);
        }
        else
        {
            tv = (TextView) CartLayout.findViewById(R.id.tvCartCount);
        }

        Typeface pricetypeface = Typeface.createFromAsset(this.getAssets(), "fonts/Vazir-Bold-FD.otf");
        if(tv!=null)
        {
            tv.setTypeface(pricetypeface);
            if(set!=null)
            {
                tv.setText(String.valueOf(set.size()));
            }
            else
            {
                tv.setText("0");
            }
        }
    }
    public void signOut()
    {
        preferences p =new preferences(this);
        p.delete_key("Key");
        p.delete_key("Phone");
        refreshNavigation();
        SQLiteDatabase db=openOrCreateDatabase("Arsaa", MODE_PRIVATE,null);
        db.execSQL("delete  from Package");
        db.execSQL("delete from Books");
        db.close();
        final File dirPath = new File(getFilesDir()+File.separator+"ebooks");
        final File databasePath = new File(String.valueOf(getDatabasePath("Arsaa")));
        if(dirPath.isDirectory()==true) {
            deleteRecursive(dirPath);
        }
        if(databasePath.isFile()==true) {
            databasePath.delete();
        }
    }
    public boolean isLogin()
    {
        preferences p = new preferences(this);
        String cellPhone = p.getstring("Phone");
        String token = p.getstring("Key");
        return (token != null && cellPhone != null) ;
    }
    public void signin()
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
    public void refreshNavigation()
    {
        NavigationView Nav=(NavigationView)findViewById(R.id.nav_view);
        Menu nav_Menu = Nav.getMenu();
        if(isLogin())
        {
            nav_Menu.findItem(R.id.nav_signout).setVisible(true);
            nav_Menu.findItem(R.id.nav_signin).setVisible(false);
        }
        else
        {
            nav_Menu.findItem(R.id.nav_signout).setVisible(false);
            nav_Menu.findItem(R.id.nav_signin).setVisible(true);
        }
    }
    void deleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();

    }
    @Override
    public void onFragmentClose() {
        refreshNavigation();
    }
}


