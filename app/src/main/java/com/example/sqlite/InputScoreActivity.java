package com.example.sqlite;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class InputScoreActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText scoreText;
    Spinner QurterSpinner,SubjectSpinner,TypeSpinner;
    AlertDialog.Builder adb;
    SQLiteDatabase db;
    HelperDB hlp;
    ContentValues cv;
    Context context = this;
    String id_student;
    ArrayAdapter<String> adp_subject;
    ArrayAdapter<String> adp_type;
    ArrayAdapter<String> adp_quarter;

    String [] subjects = {"english", "math", "cyber", "history"};
    String [] type_assignment = {"test", "homework", "project","examination"};
    String [] quarters = {"1","2","3","4"};
    String quarter =quarters[0]
            ,type=type_assignment[0],subject=subjects[0];
    Intent gi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_score);
        initAll();
        hlp = new HelperDB(this);
        db = hlp.getWritableDatabase();
        db.close();
        cv = new ContentValues();
        adp_subject = new ArrayAdapter<String>(this,androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
        ,subjects);
        adp_type =new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,type_assignment);
        adp_quarter = new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
                ,quarters);
        QurterSpinner.setAdapter(adp_quarter);
        SubjectSpinner.setAdapter(adp_subject);
        TypeSpinner.setAdapter(adp_type);
        QurterSpinner.setOnItemSelectedListener(this);
        SubjectSpinner.setOnItemSelectedListener(this);
        TypeSpinner.setOnItemSelectedListener(this);
        gi = getIntent();
        id_student = gi.getStringExtra("id");
    }

    public void initAll() {
        scoreText = (EditText) findViewById(R.id.editTextNumber);
        SubjectSpinner = (Spinner) findViewById(R.id.spinner);
        TypeSpinner = (Spinner) findViewById(R.id.spinner2);
        QurterSpinner = (Spinner) findViewById(R.id.spinner3);
    }

    public void SaveScore(View view) {
        if (!scoreText.getText().toString().isEmpty()){
            adb = new AlertDialog.Builder(this);
            adb.setTitle("Save changes?");
            adb.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    cv.clear();
                    cv.put(Scores.STUDENT_SCORE_KEY_ID,id_student);
                    cv.put(Scores.QUARTER,quarter);
                    cv.put(Scores.SUBJECT,subject);
                    cv.put(Scores.TYPE,type);
                    cv.put(Scores.SCORE,Integer.parseInt(scoreText.getText().toString()));
                    db = hlp.getWritableDatabase();
                    db.insert(Scores.TABLE_SCORES, null, cv);
                    db.close();
                    Toast.makeText(context, "Data Saved", Toast.LENGTH_SHORT).show();
                    Intent si = new Intent(context,ShowScoresActivityActivity.class);
                    si.putExtra("id",id_student);
                    si.putExtra("frominput",true);
                    finish();
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
        else Toast.makeText(context, "Fill All The Fields", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinner){
            subject = subjects[position];
        }
        if (parent.getId() == R.id.spinner2){
            quarter = quarters[position];
        }
        if (parent.getId() == R.id.spinner3){
            type = type_assignment[position];
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}