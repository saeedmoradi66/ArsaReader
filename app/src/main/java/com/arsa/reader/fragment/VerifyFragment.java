package com.arsa.reader.fragment;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONArrayRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseAndStringRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.arsa.reader.R;
import com.arsa.reader.common.preferences;
import com.arsa.reader.model.ArsaResponse;
import com.arsa.reader.model.CategoryModel;
import com.arsa.reader.model.UserModel;

import org.json.JSONArray;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import okhttp3.Response;

public class VerifyFragment extends DialogFragment {
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.verify_dialog, container, false);

        return rootView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnDone = view.findViewById(R.id.btnVerify);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText Code = getView().findViewById(R.id.tvVerifyCode);
                if (Code.getText().length() != 5)
                {
                    Code.setError("کد فعالسازی صحیح نمی باشد");
                    return;
                }
                UserModel user = new UserModel();
                user.Code = Code.getText().toString();
                user.SerialNo = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

                AndroidNetworking.post(getString(R.string.server_url) + "/User/VerifyCode")
                        .addBodyParameter(user)
                        .setTag("test")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsObject(ArsaResponse.class, new ParsedRequestListener<ArsaResponse>() {
                            @Override
                            public void onResponse(ArsaResponse response) {
                                EditText Code = getView().findViewById(R.id.tvVerifyCode);
                                if (response.StatusCode.equals("401"))
                                {
                                    Code.setError("کدفعالسازی صحیح نمی باشد");
                                    return;
                                }
                                else  if(response.StatusCode.equals("200"))
                                {
                                    preferences p =new preferences(getActivity());
                                    p.setstring("Key",response.Token);
                                    dismiss();
                                }
                            }

                            @Override
                            public void onError(ANError error) {

                            }
                        });
                

            }
        });
    }


}
