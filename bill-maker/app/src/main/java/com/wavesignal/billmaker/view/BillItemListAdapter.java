package com.wavesignal.billmaker.view;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wavesignal.billmaker.R;
import com.wavesignal.billmaker.model.Item;

import java.util.List;

public class BillItemListAdapter extends ArrayAdapter<Item> {

    private final Activity context;
    private final List<Item> itemList;

    public BillItemListAdapter(Activity context, List<Item> items) {
        super(context, R.layout.bill_listview_item, items);
        this.context = context;
        this.itemList = items;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.bill_listview_item, null, true);

        if ((position % 2) != 0)
            rowView.setBackgroundColor(Color.GRAY);

        TextView tvSno = rowView.findViewById(R.id.sno);
        TextView tvItem = rowView.findViewById(R.id.item);
        TextView tvRate = rowView.findViewById(R.id.rate);
        TextView tvRateUnit = rowView.findViewById(R.id.rateUnit);
        TextView tvQuantity = rowView.findViewById(R.id.qty);
        TextView tvQuantityUnit = rowView.findViewById(R.id.quantityUnit);
        TextView tvAmount = rowView.findViewById(R.id.amount);

        tvSno.setText((position + 1) + ".");
        tvItem.setText(itemList.get(position).getName());
        tvRate.setText(itemList.get(position).getRate());
        tvRateUnit.setText(itemList.get(position).getRateUnit());
        tvQuantity.setText(itemList.get(position).getQty());
        tvQuantityUnit.setText(itemList.get(position).getQtyUnit());
        tvAmount.setText(itemList.get(position).getAmount());

        //Log.d("TAG", "Position ===========> " + position);

        return rowView;
    }
}
