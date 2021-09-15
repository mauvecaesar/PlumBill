package com.mahalwar.plumbill.user;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mahalwar.plumbill.data.ProductContract.ProductEntry;
import com.mahalwar.plumbill.data.ProductCursorAdapter;
import com.mahalwar.plumbill.R;

import org.jetbrains.annotations.NotNull;

import static android.view.ViewGroup.FOCUS_BLOCK_DESCENDANTS;
import static com.mahalwar.plumbill.data.ProductContract.BASE_CONTENT_URI;
import static com.mahalwar.plumbill.data.ProductContract.CONTENT_URI;
import static com.mahalwar.plumbill.data.ProductContract.ProductEntry.COLUMN_NAME;
import static com.mahalwar.plumbill.data.ProductContract.ProductEntry._ID;


public class AddBillActivity extends android.app.Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    private ProductCursorAdapter mProductAdapter;

    String[] mProjection = {_ID, COLUMN_NAME, ProductEntry.COLUMN_QUANTITY,};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bill_activity);

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);

        floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddBillActivity.this, AddItemActivity.class);
            AddBillActivity.this.startActivity(intent);
        });

        int LOADER_ID = 1;
        getLoaderManager().initLoader(LOADER_ID, null, null);
        ListView listView = findViewById(R.id.list);
        listView.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
        listView.setEmptyView(listView.getEmptyView());
        mProductAdapter = new ProductCursorAdapter(this, null);
        listView.setAdapter(mProductAdapter);

        listView.setOnItemClickListener((adapterView, view, i, id) -> {
            Intent intent = new Intent(AddBillActivity.this, AddItemActivity.class);
            Uri currentPetUri = ContentUris.withAppendedId(CONTENT_URI, id);
            intent.setData(currentPetUri);
            startActivity(intent);
        });
    }

    public void deleteProductData() {
        getContentResolver().delete(BASE_CONTENT_URI, null, null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.delete_data) {
            deleteProductData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @NotNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, CONTENT_URI, mProjection, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mProductAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull @NotNull Loader<Cursor> loader) {
        mProductAdapter.swapCursor(null);
    }
}
