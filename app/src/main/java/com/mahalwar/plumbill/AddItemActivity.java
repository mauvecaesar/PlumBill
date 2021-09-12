package com.mahalwar.plumbill;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import com.mahalwar.plumbill.data.ProductContract.ProductEntry;

import static com.mahalwar.plumbill.data.ProductContract.CONTENT_URI;
import static com.mahalwar.plumbill.data.ProductContract.ProductEntry.COLUMN_NAME;
import static com.mahalwar.plumbill.data.ProductContract.ProductEntry.COLUMN_QUANTITY;
import static com.mahalwar.plumbill.data.ProductContract.ProductEntry._ID;


import androidx.core.app.NavUtils;

public class AddItemActivity extends android.app.Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri mCurrentProduct;
    EditText mQuantityEditText;
    Spinner spinner;
    private final String LOGTAG = AddItemActivity.class.getName();
    private boolean mProductHasChanged = false;

    public AddItemActivity() {
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_activity);

        mQuantityEditText = findViewById(R.id.edit_text_quantity);
        ImageButton mPlusButton = findViewById(R.id.increase_quantity);
        ImageButton mMinusButton = findViewById(R.id.decrease_quantity);
        spinner = findViewById(R.id.spinner);

        mCurrentProduct = getIntent().getData();

        if (mCurrentProduct != null) {
            setTitle(R.string.enter_product_title);
            int LOADER_ID = 1;
            getLoaderManager().initLoader(LOADER_ID, null, this);
        } else {
            setTitle(R.string.enter_product_title);
        }

        mPlusButton.setOnClickListener(view -> {
            AddOneToQuantity();
            mProductHasChanged = true;
        });
        mMinusButton.setOnClickListener(view -> {
            RemoveOneToQuantity();
            mProductHasChanged = true;
        });

    }

    public void AddOneToQuantity() {
        String previousValueString = mQuantityEditText.getText().toString();
        int value;
        if (previousValueString.isEmpty()) {
            value = 0;
        } else {
            if (Integer.parseInt(previousValueString) < 0) {
                Toast.makeText(this, getString(R.string.invalid_input),
                        Toast.LENGTH_SHORT).show();
                mQuantityEditText.setText("0");
            }
            value = Integer.parseInt(previousValueString);
        }
        mQuantityEditText.setText(String.valueOf(value + 1));
    }

    private void RemoveOneToQuantity() {
        String previousValueString = mQuantityEditText.getText().toString();
        int value = Integer.parseInt(previousValueString);
        if (value == 0) {
            Toast.makeText(this, getString(R.string.invalid_input),
                    Toast.LENGTH_SHORT).show();
            mQuantityEditText.setText("0");
        } else {
            if (previousValueString.isEmpty()) {
                value = 0;
            } else {
                if (Integer.parseInt(previousValueString) < 0) {
                    Toast.makeText(this, getString(R.string.invalid_input),
                            Toast.LENGTH_SHORT).show();
                    mQuantityEditText.setText("0");
                }
                value = Integer.parseInt(previousValueString);
            }
            mQuantityEditText.setText(String.valueOf(value - 1));
        }
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_product_data);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep, (dialog, id) -> {
            if (dialog != null) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void saveProduct() {

        String name = spinner.getSelectedItem().toString().trim();

        String quantity = mQuantityEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, getString(R.string.need_input),
                    Toast.LENGTH_SHORT).show();
        } else {
            if (mCurrentProduct == null &&
                    TextUtils.isEmpty(name) &&
                    TextUtils.isEmpty(quantity)) {
                return;
            }

            int Quantity = 0;
            if (!TextUtils.isEmpty(quantity)) {
                Quantity = Integer.parseInt(quantity);
            }

            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, name);
            values.put(COLUMN_QUANTITY, Quantity);

            if (mCurrentProduct == null) {

                Uri newUri = getContentResolver().insert(CONTENT_URI, values);

                if (newUri == null) {
                    Toast.makeText(this, getString(R.string.failed_update),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.successful_save),
                            Toast.LENGTH_SHORT).show();
                }
            } else {

                int rowsAffected = getContentResolver().update(mCurrentProduct, values, null, null);

                if (rowsAffected == 0) {
                    Toast.makeText(this, getString(R.string.failed_update),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.successful_update),
                            Toast.LENGTH_SHORT).show();
                }
            }

            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!mProductHasChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener = (dialogInterface, i) -> finish();
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveProduct();
                return true;

            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;

            case android.R.id.home:
                if (!mProductHasChanged) {
                    NavUtils.navigateUpFromSameTask(AddItemActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener = (dialogInterface, i) -> NavUtils.navigateUpFromSameTask(AddItemActivity.this);
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {_ID,
                COLUMN_NAME,
                ProductEntry.COLUMN_QUANTITY};

        return new CursorLoader(this,
                mCurrentProduct,
                projection,
                null,
                null,
                null);
    }

    @SuppressLint("SetTextI18n")
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(COLUMN_NAME);
            int quantityColumnIndex = cursor.getColumnIndex(COLUMN_QUANTITY);

            String name = cursor.getString(nameColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);

            mQuantityEditText.setText(Integer.toString(quantity));
        }
    }

    @SuppressLint("SetTextI18n")
    public void onLoaderReset(Loader<Cursor> loader) {
        mQuantityEditText.setText(Integer.toString(0));
    }

    private void showDeleteConfirmationDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog);
        builder.setPositiveButton(R.string.delete, (dialog, id) -> deletePet());
        builder.setNegativeButton(R.string.keep, (dialog, id) -> {
            if (dialog != null) {
                dialog.dismiss();
            }
        });
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void deletePet() {
        if (mCurrentProduct != null) {

            int rowsDeleted = getContentResolver().delete(mCurrentProduct, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.unsuccessful_delete),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.successful_delete),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
}
