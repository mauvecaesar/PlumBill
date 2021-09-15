package com.mahalwar.plumbill.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.mahalwar.plumbill.user.AddBillActivity;
import com.mahalwar.plumbill.R;

import static com.mahalwar.plumbill.data.ProductContract.ProductEntry.COLUMN_NAME;
import static com.mahalwar.plumbill.data.ProductContract.ProductEntry.COLUMN_QUANTITY;
import static com.mahalwar.plumbill.data.ProductContract.ProductEntry._ID;

public class ProductCursorAdapter extends CursorAdapter {

    public ProductCursorAdapter(AddBillActivity context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = view.findViewById(R.id.name);
        TextView quantityTextView = view.findViewById(R.id.quantity);

        int nameColumnIndex = cursor.getColumnIndex(COLUMN_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(COLUMN_QUANTITY);

        String productName = cursor.getString(nameColumnIndex);
        final int quantity = cursor.getInt(quantityColumnIndex);

        nameTextView.setText(productName);
        quantityTextView.setText("Quantity: " + quantity);


        @SuppressLint("Range") final long id = cursor.getLong(cursor.getColumnIndex(_ID));
    }
}
