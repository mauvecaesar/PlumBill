package com.mahalwar.plumbill.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahalwar.plumbill.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View productView = inflater.inflate(R.layout.bill_list_item, parent, false);

        // Return a new holder instance
        return new ViewHolder(productView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Product product = products.get(position);

        // Set item views based on your views and data model
        TextView itemTextView = holder.itemTextView;
        itemTextView.setText(product.getItem());
        TextView quantityTextView = holder.quantityTextView;
        quantityTextView.setText(product.getQuantity());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView itemTextView;
        public TextView quantityTextView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            itemTextView = itemView.findViewById(R.id.item_name);
            quantityTextView = itemView.findViewById(R.id.item_quantity);
        }
    }

    private List<Product> products;

    // Pass in the contact array into the constructor
    public ProductAdapter(List<Product> products) {
        this.products = products;
    }
}