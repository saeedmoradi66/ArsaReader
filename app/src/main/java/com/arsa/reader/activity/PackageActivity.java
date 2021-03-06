package com.arsa.reader.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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
import com.arsa.reader.model.PackageModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import java.util.List;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


public class PackageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_package);
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        List_fill(Integer.parseInt(intent.getStringExtra("categoryID")));

    }

    public void List_fill(int id) {
        final Activity context = this;
        AndroidNetworking.get(getString(R.string.server_url) + "/Package/GetByCategoryID/{id}")
                .addPathParameter("id", String.valueOf(id))
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

                                Intent myIntent = new Intent(PackageActivity.this, PackageDetailActivity.class);
                                myIntent.putExtra("packageID", String.valueOf(packagelist.get(i).PackageID));
                                PackageActivity.this.startActivity(myIntent);
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
