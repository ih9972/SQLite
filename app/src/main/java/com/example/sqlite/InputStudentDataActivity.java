package com.example.sqlite;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;


public class InputStudentDataActivity extends AppCompatActivity {
    String TABLE = "Student";
    String[] columns = {Student.NAME, Student.ADDRESS, Student.PERSONAL_PHONE_NUMBER, Student.HOME_PHONE_NUMBER,
            Student.PARENT_1_NAME, Student.PARENT_2_NAME,
            Student.PARENT_1_PHONE_NUMBER, Student.PARENT_2_PHONE_NUMBER};
    String selection = Student.KEY_ID +"=?";
    String[] selectionArgs = new String[1];
    String groupBy = null;
    String having = null;
    String orderBy = null;
    String limit = null;
    EditText full_name, address, personal_number, home_number, first_parent, second_parent, first_parent_number, second_parent_number;
    AlertDialog.Builder adb;
    SQLiteDatabase db;
    HelperDB hlp;
    ContentValues cv;
    Context context = this;
    Cursor crsr;
    int id;
    boolean edit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_student_data);
        initAll();
        hlp = new HelperDB(this);
        db = hlp.getWritableDatabase();
        db.close();
        cv = new ContentValues();

    }

    /**
     * This method resets all the input fields
     */
    private void initAll() {
        full_name = (EditText) findViewById(R.id.name_et);
        address = (EditText) findViewById(R.id.address_et);
        personal_number = (EditText) findViewById(R.id.p_phone_et);
        home_number = (EditText) findViewById(R.id.home_number_et);
        first_parent = (EditText) findViewById(R.id.parent_1_name_et);
        second_parent = (EditText) findViewById(R.id.parent_2_name_et);
        first_parent_number = (EditText) findViewById(R.id.parent_1_phone_et);
        second_parent_number = (EditText) findViewById(R.id.parent_2_phone_et);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Intent gi = getIntent();
        boolean fromedit = gi.getBooleanExtra("from edit", false);
        id = gi.getIntExtra("the_id",-1);
        if (fromedit && id != -1) {
            selectionArgs[0] = ""+id;
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
            crsr.moveToFirst();
            while (!crsr.isAfterLast()) {
                full_name.setText(crsr.getString(col1));
                address.setText(crsr.getString(col2));
                personal_number.setText( crsr.getString(col3));
                home_number.setText(crsr.getString(col4));
                first_parent.setText(crsr.getString(col5));
                second_parent.setText(crsr.getString(col6));
                first_parent_number.setText(crsr.getString(col7));
                second_parent_number.setText(crsr.getString(col8));
                crsr.moveToNext();
            }
            crsr.close();
            db.close();
            edit = true;
        }
        else edit = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main,menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * this method matches the credit to the option that is selected.
     * @param item The menu item that was selected.
     *
     * @return
     */
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String st = item.getTitle().toString();
        if (st.equals("Credits screen")) {
            Intent si = new Intent(this, com.example.sqlite.CreditsActivity.class);
            startActivity(si);
        }
        if (st.equals("Students List")){
            Intent si = new Intent(this, com.example.sqlite.ShowStudentActivity.class);
            startActivity(si);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Saves the data from the input fields to the sql chart
     * both input and edit data
     * @param view
     */
    public void save_student_data(View view) {
        if (!(full_name.getText().toString().isEmpty() )){
            adb = new AlertDialog.Builder(this);
            adb.setTitle("Save changes?");
            adb.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    cv.clear();
                    cv.put(Student.ACTIVE, 0);
                    cv.put(Student.NAME, full_name.getText().toString());
                    cv.put(Student.ADDRESS, address.getText().toString());
                    cv.put(Student.PERSONAL_PHONE_NUMBER, personal_number.getText().toString());
                    cv.put(Student.HOME_PHONE_NUMBER, home_number.getText().toString());
                    cv.put(Student.PARENT_1_NAME, first_parent.getText().toString());
                    cv.put(Student.PARENT_2_NAME, second_parent.getText().toString());
                    cv.put(Student.PARENT_1_PHONE_NUMBER, first_parent_number.getText().toString());
                    cv.put(Student.PARENT_2_PHONE_NUMBER, second_parent_number.getText().toString());
                    db = hlp.getWritableDatabase();
                    if (!edit) {
                        db.insert(Student.TABLE_STUDENTS, null, cv);
                        db.close();
                        Toast.makeText(context, "Data Saved", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        db.update(Student.TABLE_STUDENTS, cv, Student.KEY_ID + "=?", new String[]{"" + id});
                        db.close();
                        Toast.makeText(context, "Changes Saved", Toast.LENGTH_SHORT).show();
                    }
                    Intent si = new Intent(context, ShowStudentActivity.class);
                    startActivity(si);
                }
            });
            adb.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog ad = adb.create();
            ad.show();
        }
        else Toast.makeText(context, "Fill The Name Field", Toast.LENGTH_SHORT).show();
}

}

