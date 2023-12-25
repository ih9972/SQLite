package com.example.sqlite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class InputStudentDataActivity extends AppCompatActivity {
    EditText full_name,address,personal_number,home_number,first_parent,second_parent,first_parent_number,second_parent_number;
    AlertDialog.Builder adb;
    SQLiteDatabase db;
    HelperDB hlp;
    ContentValues cv;
    Context context = this;

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
     *
     */
    public void initAll(){
        full_name = (EditText)findViewById(R.id.name_et);
        address = (EditText)findViewById(R.id.address_et);
        personal_number = (EditText)findViewById(R.id.p_phone_et);
        home_number = (EditText)findViewById(R.id.home_number_et);
        first_parent = (EditText)findViewById(R.id.parent_1_name_et);
        second_parent= (EditText)findViewById(R.id.parent_2_name_et);
        first_parent_number = (EditText)findViewById(R.id.parent_1_phone_et);
        second_parent_number = (EditText)findViewById(R.id.parent_2_phone_et);
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
        if (st.equals("Student data")){
            Intent si = new Intent(this, com.example.sqlite.ShowStudentActivity.class);
            startActivity(si);
        }
        return super.onOptionsItemSelected(item);
    }

    public void save_student_data(View view) {
        if (!(full_name.getText().toString().isEmpty() )){
            adb = new AlertDialog.Builder(this);
            adb.setTitle("Save changes?");
            adb.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    cv.clear();
                    cv.put(Student.NAME,full_name.getText().toString());
                    cv.put(Student.ADDRESS,address.getText().toString());
                    cv.put(Student.PERSONAL_PHONE_NUMBER,personal_number.getText().toString());
                    cv.put(Student.HOME_PHONE_NUMBER,home_number.getText().toString());
                    cv.put(Student.PARENT_1_NAME,first_parent.getText().toString());
                    cv.put(Student.PARENT_2_NAME,second_parent.getText().toString());
                    cv.put(Student.PARENT_1_PHONE_NUMBER,first_parent_number.getText().toString());
                    cv.put(Student.PARENT_2_PHONE_NUMBER,second_parent_number.getText().toString());
                    db = hlp.getWritableDatabase();
                    db.insert(Student.TABLE_STUDENTS, null, cv);
                    db.close();
                    Toast.makeText(context, "Data Saved", Toast.LENGTH_SHORT).show();
                }
            })
            ;
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