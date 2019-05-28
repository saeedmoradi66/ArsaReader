package com.arsa.reader.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.arsa.reader.R;
import com.arsa.reader.adapter.book_adapter;
import com.arsa.reader.adapter.user_package_adapter;
import com.arsa.reader.common.OnClickMaker;
import com.arsa.reader.model.BookModel;
import com.arsa.reader.model.PackageModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
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

        setContentView(R.layout.activity_user_book);
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        List_fill(Integer.parseInt(intent.getStringExtra("packageID")));

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

                       /* book_adapter adapter = new book_adapter(context, bookList);

                        final ListView list = findViewById(R.id.lv_book);
                        if (list != null)
                            list.setAdapter(adapter);*/
                        SQLiteDatabase db=openOrCreateDatabase("Arsaa", MODE_PRIVATE,null);
                        db.execSQL("delete from Books");
                        for (BookModel item : bookList) {

                            db.execSQL("insert into Books(BookID,BookTitle,author) values('"+item.BookID+"','"+item.BookTitle+"','"+item.author+"')");
                        }
                        Loadoffline();

                    }

                    @Override
                    public void onError(ANError anError) {


                    }

                });
        Loadoffline();
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
    private  void Loadoffline()
    {
        SQLiteDatabase db=openOrCreateDatabase("Arsaa", MODE_PRIVATE,null);
        Cursor crs=db.rawQuery("select * from Books", null);
        final List<BookModel> Booklist=new ArrayList<BookModel>();
        while(crs.moveToNext())
        {
            BookModel model=new BookModel();
            model.BookID=crs.getInt(crs.getColumnIndex("BookID"));
            model.BookTitle=crs.getString(crs.getColumnIndex("BookTitle"));
            model.author=crs.getString(crs.getColumnIndex("author"));
            Booklist.add(model);
        }

        book_adapter adapter = new book_adapter(this, Booklist);
        final ListView list = findViewById(R.id.lv_book);
        if (list != null)
        {
            list.setAdapter(adapter);

        }
    }

}
