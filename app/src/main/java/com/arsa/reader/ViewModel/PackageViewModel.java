package com.arsa.reader.ViewModel;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.arsa.reader.model.PackageModel;

import java.util.ArrayList;
import java.util.List;

public class PackageViewModel {

    public List<PackageModel> GetByUserID() {

        final List<PackageModel> list = new ArrayList<PackageModel>();
        AndroidNetworking.get("http://api.rahmanitech.ir/api/Package/GetByUserID")
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(PackageModel.class, new ParsedRequestListener<List<PackageModel>>() {
                    @Override
                    public void onResponse(List<PackageModel> packageList) {
                        list.addAll(packageList);

                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.i("rah", anError.getMessage());
                    }

                });

        return list;
    }

}
