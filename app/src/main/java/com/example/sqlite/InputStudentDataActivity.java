package com.example.sqlite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class InputStudentDataActivity extends AppCompatActivity {
    EditText full_name,address,personal_number,home_number,first_parent,second_parent,first_parent_number,second_parent_number;
    AlertDialog.Builder adb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_student_data);
        initAll();
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
        return super.onOptionsItemSelected(item);
    }

    public void save_student_data(View view) {
        adb = new AlertDialog.Builder(this);
        adb.setTitle("Save changes?");
        adb.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        })
        AlertDialog ad = adb.create();
        ad.show();
    }
}