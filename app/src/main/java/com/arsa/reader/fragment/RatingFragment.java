package com.arsa.reader.fragment;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.arsa.reader.R;
import com.arsa.reader.common.preferences;
import com.arsa.reader.model.ArsaResponse;
import com.arsa.reader.model.RateModel;
import com.arsa.reader.model.UserModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class RatingFragment extends DialogFragment {
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.rating_dialog, container, false);

        return rootView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnDone = view.findViewById(R.id.btnRate);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RatingBar Rate = getView().findViewById(R.id.ratingBar);
                RateModel model = new RateModel();
                model.Score = (byte) Rate.getRating();
                model.SerialNo=Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
                preferences p =new preferences(getActivity());
                model.Token=p.getstring("Key");
                Bundle bundle = getArguments();
                model.PackageID=bundle.getInt("Package");

                AndroidNetworking.post(getString(R.string.server_url) + "/Rate/Insert")
                        .addBodyParameter(model)
                        .setTag("test")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsObject(ArsaResponse.class, new ParsedRequestListener<ArsaResponse>() {
                            @Override
                            public void onResponse(ArsaResponse response) {
                                if (response.StatusCode.equals("401"))
                                {
                                    Toast.makeText(getActivity(),"لطفا وارد حساب کاربری خود شوید.", Toast.LENGTH_LONG).show();
                                    dismiss();
                                }
                                else if (response.StatusCode.equals("200"))
                                {
                                    Toast.makeText(getActivity(),"امتیاز با موفقیت ثبت شد", Toast.LENGTH_LONG).show();
                                    dismiss();
                                }
                            }

                            @Override
                            public void onError(ANError error) {
                                Log.i("rah",error.getMessage());
                            }
                        });
            }
        });
    }


}
