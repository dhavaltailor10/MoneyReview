package com.hashrail.moneyreview;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewOperation extends AppCompatActivity {
    LayoutInflater inflater;
    TextView textView;
    Button button0, button1, button2, button3, button4, button5, button6,
            button7, button8, button9, buttonAdd, buttonSubtract,
            buttonDot, buttonEqual;
    float value1, value2;
    EditText editTextdate, editTexttitle;
    boolean Addition, Subtract;
    EditText editText, edittextcategory;
    RadioButton radioButtonexpenses, radioButtonprofit;
    Button btnok;
    private DatePicker datePicker;
    private Calendar calendar;
    RelativeLayout relativeLayout;
    private int year, month, day;
    String cat;
    Cursor cursor;
    public AlertDialog.Builder builder;
    public AlertDialog dialog;
    RadioGroup btnradiogroup;
    RadioButton btnradio, btnExpenses, btnprofit;

    DBExpendableList dbExpendableList;
    SQLiteDatabase db;
    Calendar myCalendar = Calendar.getInstance();
    Calendar myCalendardd = Calendar.getInstance();
    String id, mtitle, mcategory, mdate, mrs, mactivity;
    private String exp, icm;

    Menu menu;
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
// TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_operation);


        AdView adView = (AdView) findViewById(R.id.adViewNewoperation); //add the cast
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);

        Intent i = getIntent();
        id = i.getStringExtra("id");
        mrs = i.getStringExtra("rs");
        mtitle = i.getStringExtra("title");
        mcategory = i.getStringExtra("cate");
        mdate = i.getStringExtra("date");
        mactivity = i.getStringExtra("activity");
        dbExpendableList = new DBExpendableList(getApplicationContext());
        db = dbExpendableList.getWritableDatabase();
        db = dbExpendableList.getReadableDatabase();

        editText = (EditText) findViewById(R.id.editText2);
        editTexttitle = (EditText) findViewById(R.id.editTexttitle);


        edittextcategory = (EditText) findViewById(R.id.editextcategory);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        relativeLayout = (RelativeLayout) findViewById(R.id.relative);
        inflater = NewOperation.this.getLayoutInflater();

        setTitle("New Record");
        if (menu != null) {
            MenuItem item_down = menu.findItem(R.id.action_delete);
            item_down.setVisible(false);
        }
        btnradiogroup = (RadioGroup) findViewById(R.id.radiogroup);


        btnok = (Button) findViewById(R.id.btnnok);
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int rditemvalue = btnradiogroup.getCheckedRadioButtonId();
                btnradio = (RadioButton) findViewById(rditemvalue);


                String ename = edittextcategory.getText().toString();
                String title = editTexttitle.getText().toString();
                String edate = editTextdate.getText().toString();
                String type = btnradio.getText().toString();
                String rupees = editText.getText().toString();

                try {
                    if (title.equals("")) {
                        title = "No title";
                    }
                    if (ename.equals("")) {

                        ename = "UnCategorised";
                    }
                    if (rupees.equals("")) {
                        Toast.makeText(getApplicationContext(), "Amount can not be equal to 0", Toast.LENGTH_LONG).show();
                    } else {
                        if (type.matches("Expenses")) {

                            exp = "1";
                            icm = "0";

                        } else {

                            exp = "0";
                            icm = "1";
                        }

                        if (isValidDate(edate)) {
                            Toast.makeText(NewOperation.this, "The Item is Added into the Future Task", Toast.LENGTH_LONG).show();
                            insert(ename, title, edate, exp, icm, rupees);
                            try {
                                Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        insert(ename, title, edate, exp, icm, rupees);
                        try {
                            Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                } catch (SQLiteException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }


        });


        if (mactivity != null && mactivity.equals("HomeFragment")) {
            setTitle("Edit Record");
            if (menu != null) {
                MenuItem item_down = menu.findItem(R.id.action_delete);
                item_down.setVisible(true);
            }
            mrs = mrs.replace("+", "");
            mrs = mrs.replace("-", "");
            editText.setText(mrs);
            editTexttitle.setText(mtitle);
            edittextcategory.setText(mcategory);

            btnok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int btnrd = btnradiogroup.getCheckedRadioButtonId();
                    RadioButton btnradio1 = (RadioButton) findViewById(btnrd);
                    String incc, expp;
                    String catname = edittextcategory.getText().toString();
                    String title = editTexttitle.getText().toString();
                    String rs = editText.getText().toString();
                    String cdate = editTextdate.getText().toString();
                    String type1 = btnradio1.getText().toString();
                    if (type1.matches("Expenses")) {

                        expp = "1";
                        incc = "0";

                    } else {

                        expp = "0";
                        incc = "1";
                    }

                    updatedetail(catname, title, rs, cdate, expp, incc);


                    Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    dbExpendableList.close();


                }

            });


        } else if (mactivity == null) {

            Intent intent = getIntent();
            String text1 = intent.getStringExtra("cate");
            edittextcategory.setText(text1);

        }


        edittextcategory = (EditText) findViewById(R.id.editextcategory);


        edittextcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int btnrd = btnradiogroup.getCheckedRadioButtonId();
                RadioButton rdbtntype = (RadioButton) findViewById(btnrd);

                String typeofExpProfit = rdbtntype.getText().toString();
                if (typeofExpProfit.matches("Expenses")) {
                    Intent i = new Intent(NewOperation.this, ExpensesCategoryActivity.class);
                    startActivityForResult(i, 1);

                } else if (typeofExpProfit.matches("Profit")) {
                    Intent i = new Intent(NewOperation.this, ProfitCategoryActivity.class);
                    startActivityForResult(i, 1);
                }


            }
        });


        editTextdate = (EditText) findViewById(R.id.datelist);
        editTextdate.setCursorVisible(false);

        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editTextdate.setText(sdf.format(myCalendardd.getTime()));
        editTextdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(NewOperation.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }

    public static boolean isValidDate(String pDateString) throws ParseException {
        Date date = new SimpleDateFormat("dd-MM-yyyy").parse(pDateString);
        return new Date().before(date);
    }

    public long insert(String cname, String title, String edate, String expanses, String income, String rs) {
        ContentValues cv = new ContentValues();
        try {

            cv.put(DBExpendableList.NAME, cname);
            cv.put(DBExpendableList.CATEGORY_TITLE, title);

            cv.put(DBExpendableList.CATEGORY_DATE, edate);
            cv.put(DBExpendableList.EXPENSES, expanses);
            cv.put(DBExpendableList.INCOME, income);
            cv.put(DBExpendableList.RS, rs);

            Log.d("Inserted Data => ", cv.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return db.insert(DBExpendableList.CATEGORYLIST, null, cv);
    }

    private void updateLabel() {

        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editTextdate.setText(sdf.format(myCalendar.getTime()));
    }

    public void updatedetail(String cname, String title, String rs, String cdate, String expp, String icmm) {

        try {
            String ccname = null;
            String cctitle = null;
            int ccrs = 0;
            String q = "UPDATE categorylist SET name=" + "'" + cname + "'" + ",date=" + "'" + cdate + "'" + ",ctitle=" + "'" + title + "'" + ",rs=" + "'" + rs + "'" + ",expenses=" + "'" + expp + "'" + ",income=" + "'" + icmm + "'" + " WHERE _id=" + id;
            db.execSQL(q);


            String qq = "SELECT * FROM categorylist WHERE _id=" + id;
            Cursor c = db.rawQuery(qq, null);
            while (c.moveToNext()) {
                ccname = c.getString(1);
                cctitle = c.getString(2);
                cdate = c.getString(3);
                ccrs = c.getInt(4);
                expp = c.getString(5);
                icmm = c.getString(6);


            }
            db.execSQL(qq);
            Toast.makeText(getApplicationContext(), ccname + " " + cctitle + " " + String.valueOf(ccrs) + "" + cdate, Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String cate = data.getStringExtra("cate");
                edittextcategory.setText(cate);
            }
        }

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
