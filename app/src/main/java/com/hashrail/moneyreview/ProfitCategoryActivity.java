package com.hashrail.moneyreview;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ProfitCategoryActivity extends AppCompatActivity {

    TextView textviewcat;
    LayoutInflater inflater;
    ArrayList<String> list = new ArrayList<>();
    ListViewAdapterForProfitList adapter;
    EditText edittextname;
    ListView listView;
    Cursor cursor;
    RelativeLayout relativeLayout;
    DBExpendableList dbHelper = new DBExpendableList(ProfitCategoryActivity.this);
    private Context context;
    private SQLiteDatabase db;
    String[] from = {DBExpendableList.CATEGORY_NAME_EXPENSES};
    int[] to = {R.id.textlist};
    Toolbar toolbar;
    RelativeLayout MainHideNoDataForProfit;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit);

        toolbar = (Toolbar) findViewById(R.id.toolbarProfit);
       // MainHideNoDataForProfit = (RelativeLayout) findViewById(R.id.MainHideNoDataForProfit);
        setSupportActionBar(toolbar);
        setTitle("Profit Category");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inflater = ProfitCategoryActivity.this.getLayoutInflater();

        listView = (ListView) findViewById(R.id.list_profit_category_list);

        db = dbHelper.openDB(db);
        list.clear();
        fetch();
        while (cursor.moveToNext()) {
            list.add(cursor.getString(1));
        }
            adapter = new ListViewAdapterForProfitList(ProfitCategoryActivity.this, R.layout.item_listview_for_exp_profit, list, from, to);
            listView.setAdapter(adapter);
            listView.setItemsCanFocus(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String text = (String) adapterView.getItemAtPosition(i);
                Intent intent = new Intent();
                intent.putExtra("cate", text);
                setResult(RESULT_OK, intent);
                finish();


            }
        });

        relativeLayout = (RelativeLayout) findViewById(R.id.relative);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabcategory);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop();
            }
        });

    }


    public long insert(String cname) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBExpendableList.CATEGORY_NAME_INCOME, cname);
        return db.insert(DBExpendableList.TABLE_CATEGORY_INCOME, null, contentValue);
    }

    public Cursor fetch() {
        list.clear();
        try {

            String q = "select * from " + DBExpendableList.TABLE_CATEGORY_INCOME;
            cursor = db.rawQuery(q, null);
            while (cursor.moveToNext()) {
                list.add(cursor.getString(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cursor;
    }

    public void pop() {

        inflater = ProfitCategoryActivity.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.activity_add_category_item, null);
        edittextname = (EditText) c.findViewById(R.id.addcategoryitem);
        relativeLayout = (RelativeLayout) findViewById(R.id.relative);

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfitCategoryActivity.this);
        builder.setView(c);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = edittextname.getText().toString();

                if (name.equals("")) {
                    Snackbar snackbar1 = Snackbar.make(relativeLayout, "Please Fill Up Name", Snackbar.LENGTH_LONG).setActionTextColor(Color.RED);
                    snackbar1.show();
                } else {
                    db = dbHelper.openDB(db);
                    insert(name);
                    try {
                        Cursor cursor = fetch();
                        while (cursor.moveToNext()) {
                            list.add(cursor.getString(1));
                        }

                    } catch (SQLiteException e) {
                        e.printStackTrace();
                    }

                }
            }


        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
