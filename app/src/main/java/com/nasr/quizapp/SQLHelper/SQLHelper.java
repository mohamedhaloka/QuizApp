package com.nasr.quizapp.SQLHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLHelper extends SQLiteOpenHelper {

    public static final String dbName = "History";
    public static final int dbVersion = 1;

    public SQLHelper(@Nullable Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE HISTORY (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ANSWER TEXT," +
                "NAME TEXT);");

    }


    public void sqlInsert(SQLiteDatabase sqLiteDatabase, String score,String Answer) {
        String sqlInsertKey = "INSERT into HISTORY (ANSWER,NAME) values (?,?)";

        String[] args = {Answer,score};

        sqLiteDatabase.execSQL(sqlInsertKey, args);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
