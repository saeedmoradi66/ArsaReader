package com.arsa.reader.tab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arsa.reader.R;

import androidx.fragment.app.Fragment;

public class tab_Description extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_description, container, false);
    }
}
