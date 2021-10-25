package com.mahalwar.plumbill.user;

import java.util.ArrayList;

public class Product {
    private String item;
    private String quantity;

    public Product(String name, String quantity) {
        item = name;
        this.quantity = quantity;
    }

    public String getItem() {
        return item;
    }

    public String getQuantity() {
        return quantity;
    }

    private static int itemCount = 0;

    public static ArrayList<Product> createProductsList(String item, String quantity) {
        ArrayList<Product> products = new ArrayList<>();

        products.add(itemCount++, new Product(item, quantity));

        return products;
    }
}
