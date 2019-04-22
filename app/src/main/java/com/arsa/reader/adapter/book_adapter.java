package com.arsa.reader.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Environment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.arsa.reader.R;
import com.arsa.reader.activity.BookReader;
import com.arsa.reader.activity.CommentActivity;
import com.arsa.reader.activity.PackageDetailActivity;
import com.arsa.reader.common.preferences;
import com.arsa.reader.model.BookModel;

import java.io.File;
import java.util.List;

public class book_adapter extends ArrayAdapter<BookModel> {
    private final Activity context;
    private final List<BookModel> _Data;

    public book_adapter(Activity context, List<BookModel> data) {
        super(context, R.layout.book_list, data);

        _Data = data;
        this.context = context;
    }


    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.book_list, null, true);

        TextView titleText = rowView.findViewById(R.id.tv_title);
        TextView contentText = rowView.findViewById(R.id.tv_Content);
        final com.androidnetworking.widget.ANImageView book_image = rowView.findViewById(R.id.book_image);

        final String URL_IMAGE = "http://testclub.ir/showmenupic.ashx?id=25";
        book_image.setImageUrl(URL_IMAGE);

        Typeface titletypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Vazir-Light-FD.otf");
        titleText.setTypeface(titletypeface);
        titleText.setText(_Data.get(position).BookTitle);

        Typeface pricetypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Vazir-Light-FD.otf");
        contentText.setTypeface(pricetypeface);
        contentText.setText(_Data.get(position).Contents);
        final Button btnRead = (Button) rowView.findViewById(R.id.btnRead);
        btnRead.setVisibility(View.GONE);
        final Button btnDownload = (Button) rowView.findViewById(R.id.btnDownload);
        final ProgressBar simpleProgressBar=(ProgressBar) rowView.findViewById(R.id.simpleProgressBar);
        simpleProgressBar.setVisibility(View.GONE);
        preferences p = new preferences(context);
        String token = p.getstring("Key");
        String serialNo = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        final String Url = "http://admin.rahmanitech.ir/ebook/downloadbook?id=" + _Data.get(position).BookID + "&token=" + token + "&serialNo=" + serialNo;
        File rootDataDir = context.getFilesDir();
        //final String dirPath=rootDataDir.toString()+"/ebooks";
        final File dirPath = new File(context.getFilesDir()+File.separator+"ebooks");
        final File filePath = new File(context.getFilesDir()+File.separator+"ebooks"+File.separator+_Data.get(position).BookID+".epub");
        if(dirPath.isDirectory()==false) {
            dirPath.mkdirs();
        }
        if(filePath.isFile())
        {
            btnDownload.setVisibility(View.GONE);
            btnRead.setVisibility(View.VISIBLE);
        }
        else
        {
            btnDownload.setVisibility(View.VISIBLE);
            btnRead.setVisibility(View.GONE);
        }

        btnDownload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                AndroidNetworking.download(Url, dirPath.toString(), Integer.toString(_Data.get(position).BookID)+".epub")
                        .setTag("downloadTest")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .setDownloadProgressListener(new DownloadProgressListener() {
                            @Override
                            public void onProgress(long bytesDownloaded, long totalBytes) {
                                // do anything with progress
                                 // initiate the progress bar
                                simpleProgressBar.setVisibility(View.VISIBLE);
                                simpleProgressBar.setMax(100); // 100 maximum value for the progress value
                                simpleProgressBar.setProgress((int)(bytesDownloaded*100/totalBytes));
                            }
                        })
                        .startDownload(new DownloadListener() {
                            @Override
                            public void onDownloadComplete() {
                                // do anything after completion
                                simpleProgressBar.setVisibility(View.GONE);
                                btnRead.setVisibility(View.VISIBLE);
                                btnDownload.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(ANError error) {
                                // handle error
                                filePath.deleteOnExit();
                            }
                        });
            }
        });

        btnRead.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), BookReader.class);
                myIntent.putExtra("BookID", String.valueOf(_Data.get(position).BookID));
                v.getContext().startActivity(myIntent);
            }
        });

        return rowView;

    }
}
