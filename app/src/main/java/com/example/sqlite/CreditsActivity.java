package com.example.sqlite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class CreditsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main,menu);

        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String st = item.getTitle().toString();
        if (st.equals("Students List")) {
            Intent si = new Intent(this, com.example.sqlite.ShowStudentActivity.class);
            startActivity(si);
        }
        if (st.equals("Register student")){
            Intent si = new Intent(this, com.example.sqlite.InputStudentDataActivity.class);
            startActivity(si);
        }
        return super.onOptionsItemSelected(item);
    }
}