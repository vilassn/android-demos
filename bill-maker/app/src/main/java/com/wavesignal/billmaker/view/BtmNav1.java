package com.wavesignal.billmaker.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.wavesignal.billmaker.R;

public class BtmNav1 extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public static BtmNav1 newInstance() {
        BtmNav1 fragment = new BtmNav1();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.btm_nav_1, container, false);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.pager);
        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        return view;
    }
    private void setupViewPager(ViewPager viewPager) {
        PagerAdapter adapter = new PagerAdapter(getChildFragmentManager());
        adapter.addFragment(new BtmNav1Tab1(), "Tab1");
        adapter.addFragment(new BtmNav1Tab2(), "Tab2");
        viewPager.setOffscreenPageLimit(adapter.getCount());
        viewPager.setAdapter(adapter);
    }
    private void setupTabIcons() {

        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        TextView tabOne = view1.findViewById(R.id.text);
        tabOne.setText("Bill");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home_black_24dp, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        View view2 = LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        TextView tabTwo = view2.findViewById(R.id.text);
        tabTwo.setText("History");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_favorite_border_black_24dp, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

//        View view3 = LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
//        TextView tabThree = view3.findViewById(R.id.text);
//        tabThree.setText("Three");
//        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_settings_black_24dp, 0, 0);
//        tabLayout.getTabAt(2).setCustomView(tabThree);
    }
}
