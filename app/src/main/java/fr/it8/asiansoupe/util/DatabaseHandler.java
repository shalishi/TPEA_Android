package fr.it8.asiansoupe.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

import fr.it8.asiansoupe.bean.Product;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DH_DB";
    private static final String TABLE_CART = "shippingCart";
    private static final String ENTITY_ID = "entity_id";
    private static final String NAME = "name";
    private static final String PRICE = "price";
    private static final String IMG = "img";
    private static final String QTY = "qty";
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CART + "("
                + ENTITY_ID + " INTEGER PRIMARY KEY," + NAME + " TEXT,"+ PRICE + " TEXT,"+ IMG + " TEXT,"
                + QTY + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);

        // Create tables again
        onCreate(db);
    }

    // code to add the new product
    public void addProductToCart(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        Product product1=getProduct(product.getEntity_id());
        if(product1.getEntity_id()>0){
                product1.setQty((product1.getQty()+product.getQty()));
                updateProduct(product1);
        }else {
            ContentValues values = new ContentValues();
            values.put(ENTITY_ID, product.getEntity_id());
            values.put(NAME, product.getName());
            values.put(PRICE, product.getPrice());
            values.put(IMG, product.getImg());
            values.put(QTY, 1);
            // Inserting Row
            db.insert(TABLE_CART, null, values);
        }
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public int MinusProductToCart(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        Product product1=getProduct(product.getEntity_id());
        if(product1.getEntity_id()>0){
            if(product1.getQty()>product.getQty())
            product1.setQty((product1.getQty()-product.getQty()));
            if((product1.getQty()-product.getQty())>0) {
                Integer resutat = updateProduct(product1);
                db.close();
                return resutat;
            }else{
                deleteProduct(product1);
                db.close();
                return 1;
            }
        }
        db.close();
        return 0;
    }



    public Product getProduct(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CART, new String[] { ENTITY_ID,
                        NAME,PRICE,IMG,QTY }, ENTITY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null && cursor.getCount()>0){
            cursor.moveToFirst();
            Product product = new Product(cursor.getInt(0),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));

            return product;
        }
        Product product = new Product();

        return product;
    }

    // code to get all products in a list view
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<Product>();
        String selectQuery = "SELECT  * FROM " + TABLE_CART;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(cursor.getInt(0),
                        cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
                productList.add(product);
            } while (cursor.moveToNext());
        }
        db.close();
        return productList;
    }

    // update the single product
    public int updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ENTITY_ID, product.getEntity_id());
        values.put(NAME, product.getName());
        values.put(PRICE, product.getPrice());
        values.put(IMG, product.getImg());
        values.put(QTY, product.getQty());
        Integer resutat= db.update(TABLE_CART, values, ENTITY_ID + " = ?",
                new String[] { String.valueOf(product.getEntity_id()) });
        db.close();
        return resutat;
    }

    // Deleting single product
    public void deleteProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, ENTITY_ID + " = ?",
                new String[] { String.valueOf(product.getEntity_id()) });
        db.close();
    }

    // Getting products Count
    public int getProductsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CART;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }


    // DELETE DB
    public void deleteDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CART);
        onCreate(db);
        db.close();
    }
}
