package com.example.ark.vinothram;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ark.vinothram.data.ProductContract;

public class ProductCursorAdapter extends CursorAdapter {

    Cursor mCursor;
    Context mContext;

    int rowsAffected;
    int newQuantity;


    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }


    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        int nameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);
        TextView colorTextView = (TextView) view.findViewById(R.id.color);
        TextView sizeTextView = (TextView) view.findViewById(R.id.size);
        int colorColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_COLOR);
        int sizeColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_SIZE);
        final TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        int quantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
        int itemIdIndex = cursor.getColumnIndex(ProductContract.ProductEntry._ID);
        final int itemId = cursor.getInt(itemIdIndex);

        String productName = cursor.getString(nameColumnIndex);
        Integer productPrice = cursor.getInt(priceColumnIndex);
        String productColor = cursor.getString(colorColumnIndex);
        Integer productSize = cursor.getInt(sizeColumnIndex);
        Integer productQuantity = cursor.getInt(quantityColumnIndex);

        if (TextUtils.isEmpty(productName)) {
            productName = context.getString(R.string.unknown_product);
        }
        if (TextUtils.isEmpty(productColor)) {
            productColor=context.getString(R.string._no_color);
        }
        nameTextView.setText(productName);
        priceTextView.setText(String.valueOf(productPrice));
        colorTextView.setText(productColor);
        sizeTextView.setText(String.valueOf(productSize));
        quantityTextView.setText(String.valueOf(productQuantity));

        Button sellButton = (Button) view.findViewById(R.id.button_sell);
        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowsAffected = saleOfAProduct(itemId, quantityTextView);
                if (rowsAffected != 0) {
                    quantityTextView.setText(String.valueOf(newQuantity));
                }
                else {
                    Toast.makeText(context, "Update not possible", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public int saleOfAProduct(int rowId, TextView qTextView) {
        mCursor.moveToPosition(rowId);
        int oldQuantity = Integer.parseInt(qTextView.getText().toString());

        if (oldQuantity > 0) {
            newQuantity = oldQuantity - 1;

            ContentValues values = new ContentValues();
            values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, String.valueOf(newQuantity));
            Uri currentProductUri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI, rowId);

            rowsAffected = mContext.getContentResolver().update(currentProductUri, values, null, null);
        }
        return rowsAffected;
    }
}
