package com.example.boberkowy.myapplication.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.boberkowy.myapplication.Model.Product;
import com.example.boberkowy.myapplication.Model.ProductList;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductsListLab {
     private static ProductsListLab sProductsListLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private ProductsListLab(Context context){
        this.mContext = context;
        this.mDatabase = new DatabaseHelper(context).getWritableDatabase();
    }


    public static ProductsListLab get(Context context) {
        if(sProductsListLab == null){
            sProductsListLab = new ProductsListLab(context);
        }
        return sProductsListLab;
    }

    public void addProductList(ProductList productList){
        ContentValues values = getProductListValues(productList);
        mDatabase.insert(DatabaseSchema.ProductListTable.NAME, null, values);
    }

    public void updateProductList(ProductList productList){
        String uuidString = productList.getId().toString();
        ContentValues values = getProductListValues(productList);

        mDatabase.update(DatabaseSchema.ProductTable.NAME,values, DatabaseSchema.ProductListTable.Cols.UUID + "= ?", new String[] {uuidString});
    }

    public void deleteProductList(ProductList productList){
        String uuidString = productList.getId().toString();

        mDatabase.delete(DatabaseSchema.ProductTable.NAME, DatabaseSchema.ProductListTable.Cols.UUID + "= ?", new String[] {uuidString});
    }

    public List<ProductList> getProductsList(){
        List<ProductList> productLists = new ArrayList<>();

        ProductCursorWrapper cursor = queryProducts(null,null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                productLists.add(cursor.getProductList());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return productLists;
    }

    public Product getProductList(UUID id){

        ProductCursorWrapper cursor = queryProducts(DatabaseSchema.ProductListTable.Cols.UUID + "= ?", new String[] {id.toString()} );

        try {
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getProduct();
        }finally {
            cursor.close();
        }
    }


    private ProductCursorWrapper queryProducts(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                DatabaseSchema.ProductListTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new ProductCursorWrapper(cursor);
    }

    private static ContentValues getProductListValues(ProductList productList){
        ContentValues values = new ContentValues();

        values.put(DatabaseSchema.ProductListTable.Cols.UUID,productList.getId().toString());
        values.put(DatabaseSchema.ProductListTable.Cols.PRODUCT_LIST_NAME,productList.getName());

        return values;
    }
}
