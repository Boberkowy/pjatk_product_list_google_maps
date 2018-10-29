package com.example.boberkowy.myapplication.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.boberkowy.myapplication.Model.Product;
import com.example.boberkowy.myapplication.Model.ProductsLists;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.boberkowy.myapplication.DAO.DatabaseSchema.*;

public class ProductsListsLab {

    private static ProductsListsLab sProductsListsLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public ProductsListsLab(Context context) {
        this.mContext = context;
        this.mDatabase = new DatabaseHelper(context).getWritableDatabase();
    }

    public static ProductsListsLab get(Context context) {

        if (sProductsListsLab == null) {
            sProductsListsLab = new ProductsListsLab(context);
        }
        return sProductsListsLab;
    }

    public void addProductsLists(ProductsLists productsLists) {
        ContentValues values = getProductValues(productsLists);
        mDatabase.insert(ProductsListsTable.NAME, null, values);
    }

    public void updateProductsLists(ProductsLists product) {
        String uuidString = product.getId().toString();
        ContentValues values = getProductValues(product);

        mDatabase.update(ProductsListsTable.NAME, values, ProductsListsTable.Cols.UUID + "= ?", new String[]{uuidString});
    }

    public void deleteProductsLists(UUID id) {
        mDatabase.delete(ProductsListsTable.NAME, ProductsListsTable.Cols.UUID + "= ?", new String[]{id.toString()});
    }

    public List<ProductsLists> getProductsFromList(UUID id) {
        List<ProductsLists> productsLists = new ArrayList<>();

        ProductCursorWrapper cursor = queryProductsLists(ProductsListsTable.Cols.PRODUCT_LIST_ID+ "= ?", new String[]{id.toString()});

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                productsLists.add(cursor.getProductsLists());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return productsLists;
    }

    private ProductCursorWrapper queryProductsLists(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ProductsListsTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new ProductCursorWrapper(cursor);
    }

    private static ContentValues getProductValues(ProductsLists productsLists) {
        ContentValues values = new ContentValues();

        values.put(ProductsListsTable.Cols.UUID, productsLists.getId().toString());
        values.put(ProductsListsTable.Cols.PRODUCT_ID, productsLists.getProductId().toString());
        values.put(ProductsListsTable.Cols.PRODUCT_LIST_ID, productsLists.getProductListId().toString());

        return values;
    }
}
