package com.wavesignal.billmaker.view;

import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wavesignal.billmaker.R;
import com.wavesignal.billmaker.model.Bill;
import com.wavesignal.billmaker.model.Const;
import com.wavesignal.billmaker.model.Item;
import com.wavesignal.billmaker.model.Util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BtmNav1Tab1 extends Fragment {

    public static final String TAG = "BillMaker";
    //private static final String TAG = BtmNav1Tab1.class.getSimpleName();

    private TextView tvBillTitle;
    private EditText etCustInfo;
    private EditText etBillDate;
    private EditText etBillNo;
    private Button btnAddItem;
    private Button btnSaveBill;
    private ListView listView;
    private TableLayout tableLayout;
    private BillItemListAdapter listViewAdapter;
    private CardView cardView;
    private ImageButton arrow;

    private Bill mBill;
    private File mBillFile;

    private static final int SEEK_TIME = 5000;   // 5sec
    private static final int UPDATE_TIME = 1000; // 1sec
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public void updateAudioProgress() {
        Log.d(TAG, "Current Position: ");
    }

    private final Runnable updateSongTime = new Runnable() {
        @Override
        public void run() {
            updateAudioProgress();
            mHandler.postDelayed(this, UPDATE_TIME);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.btm_nav_1_tab_1, container, false);

        tvBillTitle = rootView.findViewById(R.id.billTitle);
        etCustInfo = rootView.findViewById(R.id.custValue);
        etBillDate = rootView.findViewById(R.id.dateValue);
        etBillNo = rootView.findViewById(R.id.billNoValue);
        btnAddItem = rootView.findViewById(R.id.btnAddItem);
        btnSaveBill = rootView.findViewById(R.id.btnSaveBill);
        listView = rootView.findViewById(R.id.listView);

        cardView = rootView.findViewById(R.id.base_cardview);
        arrow = rootView.findViewById(R.id.arrow_button);
        View hiddenView = rootView.findViewById(R.id.hidden_view);

        arrow.setOnClickListener(view -> {
            if (hiddenView.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                hiddenView.setVisibility(View.GONE);
                arrow.setImageResource(R.drawable.ic_baseline_expand_more_24);
            } else {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                hiddenView.setVisibility(View.VISIBLE);
                arrow.setImageResource(R.drawable.ic_baseline_expand_less_24);
            }
        });

        btnAddItem.setOnClickListener(v -> handleItemListUpdate(null));
        btnSaveBill.setOnClickListener(v -> handleSaveBill());

        mBillFile = new File(Util.getBackupDir() + File.separator + "bill.json");
        Log.d(TAG, "Bill filepath: " + mBillFile.getAbsolutePath());
//        if (mBillFile.exists())
//            mBillFile.delete();

        mBill = (Bill) Util.readObjFromFile(mBillFile, Bill.class);
        if (mBill == null) {
            Log.d(TAG, "No bill was saved, create default bill");
            Bill bill = getDefaultBill();
            Util.writeObjToFile(mBillFile, bill);
            mBill = (Bill) Util.readObjFromFile(mBillFile, Bill.class);
        }

        if (mBill != null) {
            tvBillTitle.setText(mBill.getTitle());
            etCustInfo.setText(mBill.getCustInfo());
            etBillDate.setText(mBill.getBillDate());
            etBillNo.setText(mBill.getBillNo());
            Log.d(TAG, mBill.toString() + "\nTotal items: " + mBill.getItemList().size());

            showItemList();
            //showItemTable();
        }

        //handleNightMode();

        mHandler.postDelayed(updateSongTime, UPDATE_TIME);

        return rootView;
    }

    private void showItemList() {
        listViewAdapter = new BillItemListAdapter(getActivity(), mBill.getItemList());
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                handleItemListUpdate(mBill.getItemList().get(position));
                //Toast.makeText(getApplicationContext(), "Item: " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showItemTable() {
//        tableLayout = findViewById(R.id.tableView);
//        for (int i = 0; i < mBill.getItemList().size(); i++) {
//            TableRow tableRow = (TableRow) LayoutInflater.from(this).inflate(R.layout.tableview_item, scrollView, false);
//
//            if ((i % 2) != 0) tableRow.setBackgroundColor(Color.GRAY);
//
//            TextView tvSno = tableRow.findViewById(R.id.sno);
//            TextView tvItem = tableRow.findViewById(R.id.item);
//            TextView tvRate = tableRow.findViewById(R.id.rate);
//            TextView tvQuantity = tableRow.findViewById(R.id.quantity);
//            TextView tvAmount = tableRow.findViewById(R.id.amount);
//
//            tvSno.setText((i + 1) + ".");
//            tvItem.setText(mBill.getItemList().get(i).getName());
//            tvRate.setText(mBill.getItemList().get(i).getRate());
//            tvQuantity.setText(mBill.getItemList().get(i).getQuantity());
//            tvAmount.setText(mBill.getItemList().get(i).getAmount());
//
//            tableLayout.addView(tableRow);
//            tableRow.setOnClickListener(v -> {
//                TableRow row = (TableRow) v;
//                TextView tv0 = (TextView) row.getChildAt(0);
//                TextView tv1 = (TextView) row.getChildAt(1);
//                TextView tv2 = (TextView) row.getChildAt(2);
//                TextView tv3 = (TextView) row.getChildAt(3);
//                TextView tv4 = (TextView) row.getChildAt(4);
//
//                Item item = new Item();
//                item.setName(tv1.getText().toString());
//                item.setRate(tv2.getText().toString())
//                item.setRateUnit(tv2.getText().toString());
//                item.setQuantity(tv3.getText().toString());
//                handleAddItem(item);
//
//                //Toast.makeText(MainActivity.this, "Item clicked", Toast.LENGTH_SHORT).show();
//            });
//        }
    }

    private void handleItemListUpdate(Item item) {

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_layout);
        dialog.setCancelable(false);

        EditText etName = dialog.findViewById(R.id.name);
        EditText etRate = dialog.findViewById(R.id.rate);
        EditText etQty = dialog.findViewById(R.id.qty);
        Spinner spnrRateUnit = dialog.findViewById(R.id.spinnerRate);
        Spinner spnrQtyUnit = dialog.findViewById(R.id.spinnerQty);
        Button btnAdd = dialog.findViewById(R.id.btnAdd);
        Button btnRemove = dialog.findViewById(R.id.btnRemove);
        Button btnModify = dialog.findViewById(R.id.btnModify);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        ArrayAdapter<String> adapterRate = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Const.RATE_UNITS);
        adapterRate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrRateUnit.setAdapter(adapterRate);

        ArrayAdapter<String> adapterQty = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Const.QTY_UNITS);
        adapterQty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrQtyUnit.setAdapter(adapterQty);

        // If item is null, new item can be added only. Otherwise, it can be removed or modified.
        if (item == null) {
            btnRemove.setEnabled(false);
            btnModify.setEnabled(false);
        } else {
            etName.setText(item.getName());
            etRate.setText(item.getRate());
            etQty.setText(item.getQty());

            if (item.getRateUnit().equals(Const.RATE_UNITS[Const.UNIT_KG]))
                spnrRateUnit.setSelection(Const.UNIT_KG);
            else if (item.getRateUnit().equals(Const.RATE_UNITS[Const.UNIT_GM]))
                spnrRateUnit.setSelection(Const.UNIT_GM);
            else if (item.getRateUnit().equals(Const.RATE_UNITS[Const.UNIT_UNIT]))
                spnrRateUnit.setSelection(Const.UNIT_UNIT);

            if (item.getQtyUnit().equals(Const.QTY_UNITS[Const.UNIT_KG]))
                spnrQtyUnit.setSelection(Const.UNIT_KG);
            else if (item.getQtyUnit().equals(Const.QTY_UNITS[Const.UNIT_GM]))
                spnrQtyUnit.setSelection(Const.UNIT_GM);
            else if (item.getQtyUnit().equals(Const.QTY_UNITS[Const.UNIT_UNIT]))
                spnrQtyUnit.setSelection(Const.UNIT_UNIT);
        }

        btnAdd.setOnClickListener(v -> {
            Item newItem = new Item();
            newItem.setName(String.valueOf(etName.getText()));
            newItem.setRate(String.valueOf(etRate.getText()));
            newItem.setQty(String.valueOf(etQty.getText()));
            newItem.setRateUnit(String.valueOf(spnrRateUnit.getSelectedItem()));
            newItem.setQtyUnit(String.valueOf(spnrQtyUnit.getSelectedItem()));
            newItem.calculateAndSaveAmount();

            mBill.addItem(newItem);
            Util.writeObjToFile(mBillFile, mBill);
            listViewAdapter.notifyDataSetChanged();

            dialog.dismiss();
            Log.d(TAG, "Item added: " + newItem);
            //Toast.makeText(MainActivity.this, "Item added", Toast.LENGTH_SHORT).show();
        });

        btnRemove.setOnClickListener(v -> {
            mBill.removeItem(item);
            Util.writeObjToFile(mBillFile, mBill);
            listViewAdapter.notifyDataSetChanged();

            dialog.dismiss();
            Log.d(TAG, "Item removed: " + item);
            //Toast.makeText(MainActivity.this, "Item removed", Toast.LENGTH_SHORT).show();
        });

        btnModify.setOnClickListener(v -> {
            item.setName(String.valueOf(etName.getText()));
            item.setRate(String.valueOf(etRate.getText()));
            item.setQty(String.valueOf(etQty.getText()));
            item.setRateUnit(String.valueOf(spnrRateUnit.getSelectedItem()));
            item.setQtyUnit(String.valueOf(spnrQtyUnit.getSelectedItem()));
            item.calculateAndSaveAmount();

            Util.writeObjToFile(mBillFile, mBill);
            listViewAdapter.notifyDataSetChanged();

            dialog.dismiss();
            Log.d(TAG, "Item modified: " + item);
            //Toast.makeText(MainActivity.this, "Item modified", Toast.LENGTH_SHORT).show();
        });

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
            Log.d(TAG, "Cancelled: " + item);
            //Toast.makeText(MainActivity.this, "cancelled", Toast.LENGTH_SHORT).show();
        });

        // show dialog
        dialog.show();
    }

    private void handleSaveBill() {
        mBill.setTitle((String) tvBillTitle.getText());
        mBill.setCustInfo(String.valueOf(etCustInfo.getText()));
        mBill.setBillDate(String.valueOf(etBillDate.getText()));
        mBill.setBillNo(String.valueOf(etBillNo.getText()));

        Util.writeObjToFile(mBillFile, mBill);
    }

    private Bill getDefaultBill() {
        Bill bill = new Bill();
        bill.setTitle("Grocery Bill");
        bill.setCustInfo("");
        bill.setBillDate("");
        bill.setBillNo("");

        List<Item> itemList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Item item = new Item();
            item.setName("Tomato");
            item.setRate("20");
            item.setQty("2");
            item.setRateUnit(Const.RATE_UNITS[Const.UNIT_KG]);
            item.setQtyUnit(Const.QTY_UNITS[Const.UNIT_KG]);
            item.calculateAndSaveAmount();
            itemList.add(item);
        }

        bill.setItemList(itemList);

        return bill;
    }



    private void handleNightMode() {
        int nightModeFlags = getActivity().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES: {
                handleNightModeOn();
                break;
            }
            case Configuration.UI_MODE_NIGHT_NO: {
                handleNightModeOff();
                break;
            }
            case Configuration.UI_MODE_NIGHT_UNDEFINED: {
                break;
            }
        }
    }

    private void handleNightModeOff() {
        Window window = getActivity().getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // finally change the color
        //window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
    }

    private void handleNightModeOn() {
        Window window = getActivity().getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // finally change the color
        //window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
    }

}
