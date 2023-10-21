package com.wavesignal.billmaker.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.wavesignal.billmaker.R;
public class BtmNav1Tab2 extends Fragment {
    ListView listView;
    String[] listItem;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.btm_nav_1_tab_2, container, false);

        listView = rootView.findViewById(R.id.listView);
        listItem = getResources().getStringArray(R.array.array_technology);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.custom_listview, listItem);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, position, l) -> {
            String value = adapter.getItem(position);
            Toast.makeText(getActivity().getBaseContext(), value, Toast.LENGTH_SHORT).show();
        });

        return rootView;
    }
}
