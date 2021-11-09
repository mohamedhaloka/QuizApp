package com.nasr.quizapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nasr.quizapp.SQLHelper.SQLHelper;


public class DetailsFragment extends Fragment {

    private  int drinkId ;
//    DetailsActivity detailsActivity = null;
    SQLiteDatabase sqlDB = null;
    Cursor cursor = null;


    public void setScoreId (int drinkId){
        this.drinkId = drinkId;
    }

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
//        detailsActivity = (DetailsActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();

        View view = getView();
        ListView historyData = view.findViewById(R.id.historyData);
        TextView scoreTxt = view.findViewById(R.id.scoreTxt2);

        // create object from my class that contain sqlite database services
        SQLHelper sqlHelper = new SQLHelper(view.getContext());
        //get readable data and put it in SQLiteDatabase var
        sqlDB = sqlHelper.getReadableDatabase();
        String[] values = {String.valueOf(drinkId)};
        // get all sqldb and put it into cursor
        cursor = sqlDB.rawQuery("SELECT NAME,ANSWER from HISTORY where _id = ?", values);
        System.out.println(cursor.moveToFirst());


        if (cursor.moveToFirst()) {
            String score = cursor.getString(0);
            String[] answerList = convertStringToArray(cursor.getString(1));
            scoreTxt.setText(score);

            System.out.println(answerList.length);


            ArrayAdapter arrayAdapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1,answerList);

            historyData.setAdapter(arrayAdapter);
        }
    }

    public static String[] convertStringToArray(String str) {
        String[] arr = str.split(strSeparator);
        return arr;
    }
    public static String strSeparator = "__,__";

    @Override
    public void onDestroy() {
        sqlDB.close();
        cursor.close();
        super.onDestroy();
    }
}