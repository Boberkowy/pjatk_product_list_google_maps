package com.example.boberkowy.myapplication.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.boberkowy.myapplication.Model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.boberkowy.myapplication.DAO.DatabaseSchema.ProductTable;

public class ProductLab {

    private static ProductLab sProductLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private ProductLab(Context context) {
        this.mContext = context;
        mDatabase = new DatabaseHelper(mContext).getWritableDatabase();
    }

    public static ProductLab get(Context context) {

        if (sProductLab == null) {
            sProductLab = new ProductLab(context);
        }
        return sProductLab;
    }

    public void addProduct(Product product) {
        ContentValues values = getProductValues(product);
        mDatabase.insert(ProductTable.NAME, null, values);
    }

    public void updateProduct(Product product) {
        String uuidString = product.getId().toString();
        ContentValues values = getProductValues(product);

        mDatabase.update(ProductTable.NAME, values, ProductTable.Cols.UUID + "= ?", new String[]{uuidString});
    }

    public void deleteProduct(UUID id) {
        mDatabase.delete(ProductTable.NAME, ProductTable.Cols.UUID + "= ?", new String[]{id.toString()});
    }

    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();

        ProductCursorWrapper cursor = queryProducts(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                products.add(cursor.getProduct());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return products;
    }

    public Product getProduct(UUID id) {

        ProductCursorWrapper cursor = queryProducts(ProductTable.Cols.UUID + "= ?", new String[]{id.toString()});

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getProduct();
        } finally {
            cursor.close();
        }
    }

    public UUID getProductIdByName(String name){

        ProductCursorWrapper cursor = queryProducts(ProductTable.Cols.PRODUCT_NAME + "=?", new String[]{name});

        try{
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getProduct().getId();
        }finally {
            cursor.close();
        }
    }

    private ProductCursorWrapper queryProducts(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ProductTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new ProductCursorWrapper(cursor);
    }

    private static ContentValues    getProductValues(Product product) {
        ContentValues values = new ContentValues();

        values.put(ProductTable.Cols.UUID, product.getId().toString());
        values.put(ProductTable.Cols.PRODUCT_NAME, product.getName());
        values.put(ProductTable.Cols.PRICE, product.getPrice());
        values.put(ProductTable.Cols.COUNT, product.getCount());
        values.put(ProductTable.Cols.ISPURCHASED, product.isPurchased());

        return values;
    }
}
