package com.example.sqlite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShowStudentActivity extends AppCompatActivity {
    SQLiteDatabase db;
    HelperDB hlp;
    Cursor crsr;
    ListView lv;
    ArrayList<String> tbl = new ArrayList<>();
    ArrayAdapter adp;
    String TABLE = "Student";
    String[] columns = {"Name"};
    String selection = null;
    String[] selectionArgs = null;
    String groupBy = null;
    String having = null;
    String orderBy = Student.NAME;
    String limit = null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_student);
        lv = (ListView) findViewById(R.id.listview);
        hlp = new HelperDB(this);
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
                com.google.android.material.R.layout.support_simple_spinner_dropdown_item,tbl);
        lv.setAdapter(adp);
        reset();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reset();
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String st = item.getTitle().toString();
        if (st.equals("Credits screen")) {
            Intent si = new Intent(this, com.example.sqlite.CreditsActivity.class);
            startActivity(si);
        }
        if (st.equals("Edit student data")) {
            Intent si = new Intent(this, com.example.sqlite.InputStudentDataActivity.class);
            startActivity(si);
        }
        return super.onOptionsItemSelected(item);
    }

    public void reset(){
        String name_;
        db = hlp.getReadableDatabase();
        crsr = db.query(TABLE, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        int col1 = crsr.getColumnIndex(Student.NAME);
        crsr.moveToFirst();
        while (!crsr.isAfterLast()) {
            name_ = crsr.getString(col1);
            tbl.add("" + name_);
            crsr.moveToNext();
        }
        crsr.close();
        db.close();
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item,tbl);
        adp.notifyDataSetChanged();
        lv.setAdapter(adp);
    }
}