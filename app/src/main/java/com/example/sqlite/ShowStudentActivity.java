package com.example.sqlite;

import static com.example.sqlite.Student.TABLE_STUDENTS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShowStudentActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    SQLiteDatabase db;
    HelperDB hlp;
    Cursor crsr;
    ListView lv;
    ArrayList<String> tbl = new ArrayList<>();
    String TABLE = "Student";
    String[] columns = {"Name"};
    String selection = Student.ACTIVE+"=?";
    String[] selectionArgs = {"1"};
    String groupBy = null;
    String having = null;
    String orderBy = Student.NAME;
    String limit = null;
    Context context = this;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_student);
        lv = (ListView) findViewById(R.id.listview);
        hlp = new HelperDB(this);
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
                com.google.android.material.R.layout.support_simple_spinner_dropdown_item, tbl);
        lv.setAdapter(adp);
        lv.setOnItemClickListener((AdapterView.OnItemClickListener)this);
        registerForContextMenu(lv);
        Toast.makeText(context,"Press On Student For More Data" , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo info){
        Log.d("ContextMenu", "onCreateContextMenu called");
        menu.setHeaderTitle("Possible Operations");
        menu.add("Student Data");
        menu.add("Delete Student");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        String action=item.getTitle().toString();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String selectedStudentName = tbl.get(info.position);
        if (action.equals("Student Data")){
            Intent si = new Intent(this, com.example.sqlite.ShowStudentDataActivity.class);
            si.putExtra("Student_name",selectedStudentName);
            Log.d("studentNamesi", "Name: " + selectedStudentName);
            startActivity(si);
        }
        else if (action.equals("Delete Student")){
            ContentValues cv = new ContentValues();
            //cv.put(Student.ACTIVE,"1");
            db=hlp.getWritableDatabase();
            db.update(TABLE_STUDENTS,cv,Student.ACTIVE+"=?", new String[]{"1"});
            db.close();
            reset();
        }
        return true;
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
        if (st.equals("Register student")) {
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        openContextMenu(view);
    }
}