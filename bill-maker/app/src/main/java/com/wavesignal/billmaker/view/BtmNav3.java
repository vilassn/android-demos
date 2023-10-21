package com.wavesignal.billmaker.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.wavesignal.billmaker.R;

public class BtmNav3 extends Fragment {
    public static BtmNav3 newInstance() {
        BtmNav3 fragment = new BtmNav3();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.btm_nav_3, container, false);
    }
}