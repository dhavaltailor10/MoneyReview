package com.hashrail.moneyreview;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by new-3 on 6/20/2016.
 */
public class DBExpendableList extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION = 7;
    public static final String DATABASE_NAME = "mydatabase4";
    public static final String CATEGORYLIST = "categorylist";
    public static final String ID = "_id";
    public static final String CATEGORY_TITLE = "ctitle";
    public static final String NAME = "name";
    public static final String CATEGORY_DATE = "date";
    public static final String RS = "rs";
    public static final String EXPENSES = "expenses";
    public static final String INCOME = "income";

    /* EXPENSES CATEGORY TABLE*/
    public static final String TABLE_CATEGORY_EXPENSES = "categoryexpenses";
    public static final String EXP_ID = "_id";
    public static final String CATEGORY_NAME_EXPENSES = "cnameexp";

    /* INCOME CATEGORY TABLE*/
    public static final String TABLE_CATEGORY_INCOME = "categoryincome";
    public static final String INC_ID = "_id";
    public static final String CATEGORY_NAME_INCOME = "cnameinc";


    public DBExpendableList(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String q = "CREATE TABLE IF NOT EXISTS " + CATEGORYLIST + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT NOT NULL, " + CATEGORY_TITLE + " TEXT NOT NULL, " + CATEGORY_DATE + " DATE, " + RS + " INTEGER," + EXPENSES + " TEXT, " + INCOME + " TEXT" + ")";
        String queryexp = "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORY_EXPENSES + "(" + EXP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CATEGORY_NAME_EXPENSES + " TEXT" + ")";
        String queryinc = "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORY_INCOME + "(" + INC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CATEGORY_NAME_INCOME + " TEXT" + ")";
        Log.e("Create Table", "" + q);
        sqLiteDatabase.execSQL(q);
        sqLiteDatabase.execSQL(queryexp);
        sqLiteDatabase.execSQL(queryinc);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CATEGORYLIST);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY_EXPENSES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY_INCOME);
    }


    public SQLiteDatabase openDB(SQLiteDatabase db) {
        db = this.getWritableDatabase();
        return db;
    }

    @Override
    public synchronized void close() {
        super.close();
    }
}
