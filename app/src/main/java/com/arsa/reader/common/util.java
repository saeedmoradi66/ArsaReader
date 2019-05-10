package com.arsa.reader.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.arsa.reader.R;
import com.arsa.reader.activity.PackageActivity;
import com.arsa.reader.activity.PackageDetailActivity;
import com.arsa.reader.adapter.package_adapter;
import com.arsa.reader.model.PackageModel;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.List;

import static androidx.core.content.ContextCompat.getSystemService;

public class util {
    public static int getScreenHeight(Context activity) {
        int columnHeight;
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();

        point.x = display.getWidth();
        point.y = display.getHeight();

        columnHeight = point.y;
        return columnHeight;
    }

    public static boolean checkActiveInternetConnection(Context context) {

        final boolean[] Result = {false};

        AndroidNetworking.get(context.getString(R.string.server_url) + "/CheckOnline")
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("rah",response);
                        if(response.toString().equals("200"))
                        {
                            Result[0] =true;
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.i("rah",anError.getMessage());
                    }
                } );
        return Result[0];
    }
}
