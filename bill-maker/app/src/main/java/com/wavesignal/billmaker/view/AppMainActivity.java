package com.wavesignal.billmaker.view;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wavesignal.billmaker.R;

public class AppMainActivity extends AppCompatActivity {

    private static Context context;
    Fragment activeFragment;
    Fragment selectedFragment1 = BtmNav1.newInstance();
    Fragment selectedFragment2 = BtmNav2.newInstance();
    Fragment selectedFragment3 = BtmNav3.newInstance();

    public static Context getContext() {
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnItemSelectedListener(mOnItemSelectedListener);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //transaction.replace(R.id.frame_layout, selectedFragment);
        transaction.add(R.id.frame_layout, selectedFragment1);
        transaction.add(R.id.frame_layout, selectedFragment2).hide(selectedFragment2);
        transaction.add(R.id.frame_layout, selectedFragment3).hide(selectedFragment3);
        transaction.commit();

        activeFragment =  selectedFragment1;
    }

    BottomNavigationView.OnItemSelectedListener mOnItemSelectedListener = item -> {
        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.action_item1:
                selectedFragment = selectedFragment1;
                break;
            case R.id.action_item2:
                selectedFragment = selectedFragment2;
                break;
            case R.id.action_item3:
                selectedFragment = selectedFragment3;
                break;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //transaction.replace(R.id.frame_layout, selectedFragment);
        transaction.hide(activeFragment).show(selectedFragment);
        transaction.commit();

        activeFragment = selectedFragment;

        return true;
    };

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    // create an action bar button
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
//        // If you don't have res/menu, just create a directory named "menu" inside res
//        getMenuInflater().inflate(R.menu.action_bar_items, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    // handle button activities
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.mybutton) {
//            // do something here
//        }
//        return super.onOptionsItemSelected(item);
//    }
}