package com.example.boberkowy.myapplication.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.boberkowy.myapplication.DAO.DatabaseSchema.*;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "productMarks.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + ProductTable.NAME +
                "(" +
                "_id integer primary key autoincrement," +
                ProductTable.Cols.UUID + ", " +
                ProductTable.Cols.PRODUCT_NAME + ", " +
                ProductTable.Cols.PRICE + ", " +
                ProductTable.Cols.COUNT + ", " +
                ProductTable.Cols.ISPURCHASED + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
