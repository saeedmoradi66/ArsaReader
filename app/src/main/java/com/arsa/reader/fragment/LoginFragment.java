package com.arsa.reader.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.arsa.reader.R;
import com.arsa.reader.activity.MainActivity;
import com.arsa.reader.activity.PackageActivity;
import com.arsa.reader.common.preferences;
import com.arsa.reader.model.UserModel;
import org.json.JSONArray;
import org.json.JSONObject;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class LoginFragment extends DialogFragment {
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_dialog, container, false);

        return rootView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnDone = view.findViewById(R.id.btnLogin);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText Mobile = getView().findViewById(R.id.tvMobile);
                if (Mobile.getText().length() != 11)
                {
                    Mobile.setError(" شماره تلفن صحیح نمی باشد");
                    return;
                }
                UserModel user = new UserModel();
                user.CellPhone = Mobile.getText().toString();
                user.SerialNo = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

                preferences p =new preferences(getActivity());
                p.setstring("Phone",user.CellPhone);

                AndroidNetworking.post(getString(R.string.server_url) + "/User/GetCode")
                        .addBodyParameter(user)
                        .setTag("test")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONArray(new JSONArrayRequestListener() {
                            @Override
                            public void onResponse(JSONArray response) {

                            }
                            @Override
                            public void onError(ANError error) {
                                // handle error
                            }
                        });
                VerifyFragment dialogFragment = new VerifyFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("notAlertDialog", true);
                dialogFragment.setArguments(bundle);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("LoginDialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                dialogFragment.show(ft, "Dialog");

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        preferences p =new preferences(getActivity());
        if(p.getstring("Key")!=null)
        {
            dismiss();
        }
    }
}
