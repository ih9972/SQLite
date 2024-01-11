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
    int id_student;
    int id_score;
    ArrayAdapter<String> adp_subject;
    ArrayAdapter<String> adp_type;
    ArrayAdapter<String> adp_quarter;
    String tableName = Scores.TABLE_SCORES;
    String[] columns = {Scores.SCORE,Scores.TYPE,Scores.QUARTER,Scores.SUBJECT,Scores.SCORE_KEY_ID};
    String selection = Scores.SCORE_KEY_ID +"=?";
    String[] selectionArgs = {"someValue"};
    String groupBy = null;
    String having = null;
    String orderBy = null;
    String limit = null;
    String [] subjects = {"english", "math", "cyber", "history"};
    String [] type_assignment = {"test", "homework", "project","examination"};
    String [] quarters = {"1","2","3","4"};
    String quarter =quarters[0]
            ,type=type_assignment[0],subject=subjects[0];
    Intent gi;
    Cursor crsr;
    boolean edit;

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
        id_student = gi.getIntExtra("the_id",-1);
    }

    protected void onStart() {
        super.onStart();
        if (gi.getBooleanExtra("edit", false)) {
            id_score = gi.getIntExtra("the_score_id",-1);
            selectionArgs[0] = ""+id_score;
            db = hlp.getReadableDatabase();
            crsr = db.query(tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            int col1 = crsr.getColumnIndex(Scores.SCORE);
            int col2 = crsr.getColumnIndex(Scores.QUARTER);
            int col3 = crsr.getColumnIndex(Scores.SUBJECT);
            int col4 = crsr.getColumnIndex(Scores.TYPE);
            crsr.moveToFirst();
            while (!crsr.isAfterLast()) {
                scoreText.setText(crsr.getString(col1));
                type = crsr.getString(col4);
                quarter = crsr.getString(col2);
                subject = crsr.getString(col3);
                crsr.moveToNext();
            }
            crsr.close();
            db.close();
            edit = true;
        }
        else edit = false;
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
                    cv.put(Scores.STUDENT_SCORE_KEY_ID, id_student);
                    cv.put(Scores.QUARTER, quarter);
                    cv.put(Scores.SUBJECT, subject);
                    cv.put(Scores.TYPE, type);
                    cv.put(Scores.SCORE, Integer.parseInt(scoreText.getText().toString()));
                    db = hlp.getWritableDatabase();
                    if (!edit) {
                        db.insert(Scores.TABLE_SCORES, null, cv);
                        db.close();
                        Toast.makeText(context, "Data Saved", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        db.update(Scores.TABLE_SCORES, cv, Scores.SCORE_KEY_ID + "=?", new String[]{""});
                        db.close();
                        Toast.makeText(context, "Changes Saved", Toast.LENGTH_SHORT).show();
                    }
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

    public void CancelScore(View view) {
        finish();
    }
}