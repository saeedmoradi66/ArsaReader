package com.arsa.reader.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.arsa.reader.R;
import com.arsa.reader.adapter.cart_packages_adapter;
import com.arsa.reader.common.OnClickMaker;
import com.arsa.reader.common.preferences;
import com.arsa.reader.common.util;
import com.arsa.reader.fragment.LoginFragment;
import com.arsa.reader.model.PackageModel;
import com.arsa.reader.model.UserModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class CartActivity extends BaseActivity {

    TextView tv_TotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_cart);
        super.onCreate(savedInstanceState);



        TextView priceText = findViewById(R.id.tv_TotalPrice);
        Button btnPayment = findViewById(R.id.btnPayment);
        Typeface pricetypeface = Typeface.createFromAsset(getAssets(), "fonts/Vazir-Light-FD.otf");
        priceText.setTypeface(pricetypeface);
        btnPayment.setTypeface(pricetypeface);

        List_fill();

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences p = new preferences(CartActivity.this);
                String cellPhone = p.getstring("Phone");
                String token = p.getstring("Key");
                String serialNo = Settings.Secure.getString(CartActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
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
                    List<PackageModel> userPackages= GetUserPackages(cellPhone,token,serialNo);
                    SetFactor(token,serialNo,userPackages);
                }
            }
        });

    }

    public void List_fill() {
        final Activity context = this;
        preferences p = new preferences(this);
        Set<String> set = p.getstringset("Cart");
        if (set != null)
            if (set.size() > 0) {
                String IDs = "";
                for (String s : set) {
                    IDs += s;
                    IDs += "$";
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

                                cart_packages_adapter adapter = new cart_packages_adapter(context, packagelist);

                                ListView list = findViewById(R.id.lv_package);
                                if (list != null) {
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
                            }

                            @Override
                            public void onError(ANError anError) {


                            }

                        });
            }

    }

    public void SetFactor(String token,String serialNo,List<PackageModel> userPackages) {
        final Activity context = this;
        preferences p = new preferences(this);
        Set<String> set = p.getstringset("Cart");
        if (set != null)
            if (set.size() > 0) {
                String IDs = "";
                for (String s : set) {
                    for(PackageModel obj : userPackages) {
                        if(String.valueOf(obj.PackageID).equals(s)) {
                            Toast.makeText(CartActivity.this,String.valueOf(obj.PackageTitle)+" قبلا خریداری شده است", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    IDs += s;
                    IDs += "$";
                }
                IDs = IDs.substring(0, IDs.length() - 1);

                AndroidNetworking.get(getString(R.string.server_url) + "/Factor/Generate/{id}/{token}/{serialNo}")
                        .addPathParameter("id", String.valueOf(IDs))
                        .addPathParameter("token", token)
                        .addPathParameter("serialNo", serialNo)
                        .setTag(this)
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsObject(Integer.class, new ParsedRequestListener<Integer>() {

                            @Override
                            public void onResponse(Integer response) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://payment.arsaapub.ir/home/Index?factorid="+response.toString()));
                                preferences p = new preferences(CartActivity.this);
                                p.delete_key("Cart");
                                startActivity(browserIntent);
                            }

                            @Override
                            public void onError(ANError anError) {

                            }
                        });
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

    public void SetPrice(int value) {
        TextView tv_Total = findViewById(R.id.tv_TotalPrice);
        Typeface pricetypeface = Typeface.createFromAsset(this.getAssets(), "fonts/Vazir-Light-FD.otf");
        tv_Total.setTypeface(pricetypeface);
        tv_Total.setText("جمع کل : " + String.valueOf(value) + " ریال");
        if (value > 0) {
            Button btnPayment = findViewById(R.id.btnPayment);
            btnPayment.setVisibility(View.VISIBLE);
        }
    }
    public List<PackageModel> GetUserPackages(String cellPhone, String token, String serialNo) {
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

                    }

                    @Override
                    public void onError(ANError anError) {


                    }

                });
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
        return packagelist;
    }
}
