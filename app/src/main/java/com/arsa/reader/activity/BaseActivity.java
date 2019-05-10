package com.arsa.reader.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

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

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private int CartCounter = 0;


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account) {

            preferences p =new preferences(this);
            if(p.getstring("Key")==null)
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
            else
            {
                Intent myIntent = new Intent(BaseActivity.this, ProfileActivity.class);
                BaseActivity.this.startActivity(myIntent);
            }

        }
        else if (id == R.id.nav_signout) {
            preferences p =new preferences(this);
            p.delete_key("Key");
            p.delete_key("Phone");
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
}


