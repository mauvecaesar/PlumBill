package com.mahalwar.plumbill.user;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mahalwar.plumbill.R;

import java.util.ArrayList;


public class AddBillActivity extends android.app.Activity {
    RecyclerView recyclerView;
    private static int itemCount=0;
    ProductAdapter adapter;
    ArrayList<Product> products;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bill_activity);

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        intent = new Intent(getApplicationContext(), AddItemActivity.class);
        recyclerView = findViewById(R.id.recyclerView);
        products = new ArrayList<>();
        adapter = new ProductAdapter(products);

        floatingActionButton.setOnClickListener(view -> {
            startActivity(intent);

            String item = intent.getStringExtra("Item");
            String quantity = intent.getStringExtra("Quantity");

            products.addAll(Product.createProductsList(item, quantity));

            adapter.notifyItemInserted(itemCount++);

            // Attach the adapter to the recyclerview to populate items
            recyclerView.setAdapter(adapter);
            // Set layout manager to position the items
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        });
    }

}

/*
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
}*/
