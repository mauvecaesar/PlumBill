package com.mahalwar.plumbill.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.mahalwar.plumbill.data.ProductContract.ProductEntry;


public class ProductDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "product.db";
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " +
            ProductEntry.TABLE_NAME + "(" +
            ProductEntry._ID + " INTEGER PRIMARY KEY" + "," +
            ProductEntry.COLUMN_NAME + " TEXT" + "," +
            ProductEntry.COLUMN_QUANTITY + " INTEGER" + ";";

    public ProductDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldDbVer, int newDbVer) {
        onUpgrade(sqLiteDatabase,oldDbVer,newDbVer);
    }
}
