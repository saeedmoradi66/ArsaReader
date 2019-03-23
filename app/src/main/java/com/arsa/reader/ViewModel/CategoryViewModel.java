package com.arsa.reader.ViewModel;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.arsa.reader.model.CategoryModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryViewModel {

    public List<CategoryModel> GetAllCategory() {

        final List<CategoryModel> list = new ArrayList<CategoryModel>();
        AndroidNetworking.get("http://api.rahmanitech.ir/api/Category/GetAll")
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(CategoryModel.class, new ParsedRequestListener<List<CategoryModel>>() {
                    @Override
                    public void onResponse(List<CategoryModel> category) {
                        list.addAll(category);

                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.i("rah", anError.getMessage());
                    }

                });

        return list;
    }

}
