package com.arsa.reader.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.arsa.reader.R;
import com.arsa.reader.adapter.category_grid_adapter;
import com.arsa.reader.adapter.comment_adapter;
import com.arsa.reader.common.OnClickMaker;
import com.arsa.reader.common.preferences;
import com.arsa.reader.model.ArsaResponse;
import com.arsa.reader.model.CategoryModel;
import com.arsa.reader.model.CommentModel;
import com.arsa.reader.model.RateModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


public class CommentActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final Intent intent = getIntent();
        GetComments(Integer.parseInt(intent.getStringExtra("PackageID")));

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        Button btnDone = findViewById(R.id.btnComment);

       btnDone.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               final EditText txtComment = findViewById(R.id.tv_comment);

               if (txtComment.getText().length() >0)
               {
                   CommentModel model = new CommentModel();
                   model.CommentText =  txtComment.getText().toString();
                   model.SerialNo= Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                   preferences p =new preferences(CommentActivity.this);
                   model.Token=p.getstring("Key");
                   model.PackageID=Integer.parseInt(intent.getStringExtra("PackageID"));

                   AndroidNetworking.post(getString(R.string.server_url) + "/Comment/Insert")
                           .addBodyParameter(model)
                           .setTag("test")
                           .setPriority(Priority.MEDIUM)
                           .build()
                           .getAsObject(ArsaResponse.class, new ParsedRequestListener<ArsaResponse>() {
                               @Override
                               public void onResponse(ArsaResponse response) {
                                   if (response.StatusCode.equals("401"))
                                   {
                                       Toast.makeText(CommentActivity.this,"لطفا وارد حساب کاربری خود شوید.", Toast.LENGTH_LONG).show();

                                   }
                                   else if (response.StatusCode.equals("200"))
                                   {
                                       Toast.makeText(CommentActivity.this,"امتیاز با موفقیت ثبت شد", Toast.LENGTH_LONG).show();
                                        txtComment.setText("");
                                       GetComments(Integer.parseInt(intent.getStringExtra("PackageID")));
                                   }
                               }

                               @Override
                               public void onError(ANError error) {
                                   Log.i("rah",error.getMessage());
                               }
                           });
               }
           }
       });

    }

    public void GetComments(int id) {
        final Activity context = this;
        AndroidNetworking.get(getString(R.string.server_url) + "/Comment/GetByPackageID/{id}")
                .addPathParameter("id", String.valueOf(id))
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(CommentModel.class, new ParsedRequestListener<List<CommentModel>>() {
                    @Override
                    public void onResponse(List<CommentModel> commentList) {

                        comment_adapter adapter = new comment_adapter(context, commentList);
                        final ListView list = findViewById(R.id.lv_comment);
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
