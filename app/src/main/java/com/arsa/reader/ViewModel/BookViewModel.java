package com.arsa.reader.ViewModel;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.arsa.reader.model.BookModel;

import java.util.ArrayList;
import java.util.List;

public class BookViewModel {

    public List<BookModel> GetByPackageID() {

        final List<BookModel> list = new ArrayList<BookModel>();
        AndroidNetworking.get("http://api.rahmanitech.ir/api/Book/GetByPackageID")
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(BookModel.class, new ParsedRequestListener<List<BookModel>>() {
                    @Override
                    public void onResponse(List<BookModel> bookList) {
                        list.addAll(bookList);

                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.i("rah", anError.getMessage());
                    }

                });

        return list;
    }

}
