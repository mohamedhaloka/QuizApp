package com.nasr.quizapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.nasr.quizapp.SQLHelper.SQLHelper;

public class HistoryActivity extends AppCompatActivity {

    SQLiteDatabase sqlDB = null;
    Cursor cursor = null;
    SimpleCursorAdapter simpleCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // create object from my class that contain sqlite database services
        SQLHelper sqlHelper = new SQLHelper(this);
        //get readable data and put it in SQLiteDatabase var
        sqlDB = sqlHelper.getReadableDatabase();
        // get all sql db and put it into cursor
        cursor = sqlDB.rawQuery("SELECT _id ,NAME,ANSWER from HISTORY", null);
        // create adapter to work with cursor and db
         simpleCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, new String[]{"NAME"}, new int[]{android.R.id.text1}, 0);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void onClick(long id){
        FrameLayout frameLayout = findViewById(R.id.frameStack);

        if (frameLayout == null) {

            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("drinkId", (int) id);
            startActivity(intent);
        } else {
            DetailsFragment detailsFragment = new DetailsFragment();
            detailsFragment.setScoreId((int)id);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameStack, detailsFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }




    @Override
    protected void onDestroy() {
        sqlDB.close();
        cursor.close();
        super.onDestroy();
    }
}