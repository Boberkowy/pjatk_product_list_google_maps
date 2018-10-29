package com.example.boberkowy.myapplication.DAO;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.boberkowy.myapplication.Model.Product;
import com.example.boberkowy.myapplication.Model.ProductList;
import com.example.boberkowy.myapplication.Model.ProductsLists;

import java.util.UUID;

import static com.example.boberkowy.myapplication.DAO.DatabaseSchema.*;

public class ProductCursorWrapper extends CursorWrapper {


    public ProductCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Product getProduct(){
        String uuidString = getString(getColumnIndex(ProductTable.Cols.UUID));
        String name = getString(getColumnIndex(ProductTable.Cols.PRODUCT_NAME));
        String count = getString(getColumnIndex(ProductTable.Cols.COUNT));
        String price = getString(getColumnIndex(ProductTable.Cols.PRICE));
        boolean purchased = getInt(getColumnIndex(ProductTable.Cols.ISPURCHASED)) > 0;

        Product product = new Product(UUID.fromString(uuidString));
        product.setName(name);
        product.setCount(count);
        product.setPrice(price);
        product.setPurchased(purchased);

        return product;
    }

    public ProductList getProductList(){
        String uuidString = getString(getColumnIndex(ProductListTable.Cols.UUID));
        String name = getString(getColumnIndex(ProductListTable.Cols.PRODUCT_LIST_NAME));

        ProductList productList = new ProductList(UUID.fromString(uuidString));
        productList.setName(name);

        return productList;
    }

    public ProductsLists getProductsLists(){
        String uuidString = getString(getColumnIndex(ProductsListsTable.Cols.UUID));
        String productUuidString = getString(getColumnIndex(ProductsListsTable.Cols.PRODUCT_ID));
        String productListUuidString = getString(getColumnIndex(ProductsListsTable.Cols.PRODUCT_LIST_ID));

        ProductsLists productsLists = new ProductsLists(UUID.fromString(uuidString));
        productsLists.setProductId(UUID.fromString(productUuidString));
        productsLists.setProductListId(UUID.fromString(productListUuidString));

        return productsLists;
    }
}
