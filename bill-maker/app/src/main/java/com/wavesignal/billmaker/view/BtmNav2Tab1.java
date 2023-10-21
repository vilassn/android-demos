package com.wavesignal.billmaker.view;

import static com.wavesignal.billmaker.model.Util.getBackupDir;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.wavesignal.billmaker.R;
import com.wavesignal.billmaker.model.Bill;
import com.wavesignal.billmaker.model.Const;
import com.wavesignal.billmaker.model.Item;
import com.wavesignal.billmaker.model.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BtmNav2Tab1 extends Fragment {
    private static final String TAG = BtmNav2Tab1.class.getSimpleName();
    private ListView listView;
    private String[] listItem;

    private Bill mBill;
    private File mBillFile;
    private ProductItemListAdapter listViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.btm_nav_2_tab_1, container, false);

        listView = rootView.findViewById(R.id.listView);

        mBillFile = new File(getBackupDir() + File.separator + "products.json");
        Log.d(TAG, "Bill filepath: " + mBillFile.getAbsolutePath());
//        if (mBillFile.exists())
//            mBillFile.delete();

        mBill = (Bill) Util.readObjFromFile(mBillFile, Bill.class);
        if (mBill == null) {
            Log.d(TAG, "No bill was saved, create default bill");
            Bill bill = new Bill();
            Util.writeObjToFile(mBillFile, bill);
            mBill = (Bill) Util.readObjFromFile(mBillFile, Bill.class);
        }

        if (mBill != null) {
            Log.d(TAG, mBill.toString() + "\nTotal items: " + mBill.getItemList().size());

            showItemList();
            //showItemTable();
        }

        return rootView;
    }

    private void showItemList() {
        listViewAdapter = new ProductItemListAdapter(getActivity(), getItemList());
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                handleItemListUpdate(mBill.getItemList().get(position));
                //Toast.makeText(getApplicationContext(), "Item: " + position, Toast.LENGTH_SHORT).show();
            }
        });
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

    private List<Item> getItemList() {
        List<Item> itemList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Item item = new Item();
            item.setName("Tomato");
            item.setRate("20");
            item.setQty("2");
            item.setRateUnit(Const.RATE_UNITS[Const.UNIT_KG]);
            item.setQtyUnit(Const.QTY_UNITS[Const.UNIT_KG]);
            item.calculateAndSaveAmount();
            itemList.add(item);
        }
        return itemList;
    }
}
