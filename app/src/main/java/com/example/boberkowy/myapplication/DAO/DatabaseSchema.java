package com.example.boberkowy.myapplication.DAO;

public class DatabaseSchema {

    public static final class ProductTable {
        public static final String NAME = "Product";

        public static final class Cols {
            public static final String UUID = "id";
            public static final String PRODUCT_NAME ="name";
            public static final String COUNT = "count";
            public static final String PRICE = "price";
            public static final String ISPURCHASED = "isPurchased";
        }
    }

    public static final class ProductListTable {
        public static final String NAME = "ProductList";

        public static final class Cols {
            public static final String UUID = "id";
            public static final String PRODUCT_LIST_NAME ="name";
        }
    }

    public static final class ProductsListsTable {
        public static final String NAME = "ProductsLists";

        public static final class Cols {
            public static final String UUID = "id";
            public static final String PRODUCT_ID = "product_id";
            public static final String PRODUCT_LIST_ID ="list_id";
        }
    }
}
