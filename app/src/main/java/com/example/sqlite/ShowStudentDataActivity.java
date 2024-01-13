package com.example.sqlite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ShowStudentDataActivity extends AppCompatActivity {
    SQLiteDatabase db;
    HelperDB hlp;
    Cursor crsr;
    ListView lv;
    ArrayList<String> tbl = new ArrayList<>();
    String TABLE = "Student";
    String[] columns = {"Name", "Address", "Personal_Phone_number", "Home_Phone_number",
            "First_parent_name", "Second_parent_name", "First_parent_number", "Second_parent_number"};
    String selection = "_id=?";
    String[] selectionArgs = {"a"};
    String groupBy = null;
    String having = null;
    String orderBy = null;
    String limit = null;
    int id_student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_student_data);
        lv = (ListView) findViewById(R.id.listview2);
        hlp = new HelperDB(this);
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
                , tbl);
        lv.setAdapter(adp);
        id_student = getIntent().getIntExtra("id",-1);
        reset();
    }

    /**
     * Update the list view
     */
    public void reset() {
        selectionArgs[0] = ""+ id_student;
        db = hlp.getReadableDatabase();
        crsr = db.query(TABLE, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        int col1 = crsr.getColumnIndex(Student.NAME);
        int col2 = crsr.getColumnIndex(Student.ADDRESS);
        int col3 = crsr.getColumnIndex(Student.PERSONAL_PHONE_NUMBER);
        int col4 = crsr.getColumnIndex(Student.HOME_PHONE_NUMBER);
        int col5 = crsr.getColumnIndex(Student.PARENT_1_NAME);
        int col6 = crsr.getColumnIndex(Student.PARENT_2_NAME);
        int col7 = crsr.getColumnIndex(Student.PARENT_1_PHONE_NUMBER);
        int col8 = crsr.getColumnIndex(Student.PARENT_2_PHONE_NUMBER);
        String name, address, parent_1_name, parent_2_name, personal_phone_number, home_phone_number, parent_1_phone_number, parent_2_phone_number;
        crsr.moveToFirst();
        while (!crsr.isAfterLast()) {
            name = crsr.getString(col1);
            address = crsr.getString(col2);
            personal_phone_number = crsr.getString(col3);
            home_phone_number = crsr.getString(col4);
            parent_1_name = crsr.getString(col5);
            parent_2_name = crsr.getString(col6);
            parent_1_phone_number = crsr.getString(col7);
            parent_2_phone_number = crsr.getString(col8);
            tbl.add("Name: " + name);
            tbl.add("Address: " + address);
            tbl.add("Personal number: " + personal_phone_number);
            tbl.add("Home number: " + home_phone_number);
            tbl.add("First parent: " + parent_1_name);
            tbl.add("" + parent_1_name + "'s number: " + parent_1_phone_number);
            tbl.add("Second parent: " + parent_2_name);
            tbl.add("" + parent_2_name + "'s number: " + parent_2_phone_number);
            crsr.moveToNext();
        }
        crsr.close();
        db.close();
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
                , tbl);
        adp.notifyDataSetChanged();
        lv.setAdapter(adp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String st = item.getTitle().toString();
        if (st.equals("Credits screen")) {
            Intent si = new Intent(this, CreditsActivity.class);
            startActivity(si);
        }
        if (st.equals("Register student")) {
            Intent si = new Intent(this, InputStudentDataActivity.class);
            startActivity(si);
        }
        if (st.equals("Students List")) {
            Intent si = new Intent(this,ShowStudentActivity.class);
            si.putExtra("the_id", id_student);
            startActivity(si);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * sends the user to the input data activity
     * @param view
     */
    public void Gotoinput(View view) {
        Intent si = new Intent(this, InputStudentDataActivity.class);
        si.putExtra("the_id", id_student);
        si.putExtra("from edit", true);
        startActivity(si);
    }
}