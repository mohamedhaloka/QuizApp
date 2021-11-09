package com.nasr.quizapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.nasr.quizapp.SQLHelper.SQLHelper;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    public static String strSeparator = "__,__";

    Question[] questions = new Question[]{new Question("1+2=7?", "False"), new Question("102+8=110?", "True"), new Question("12+2=7?", "False"), new Question("2222+2=700?", "False"), new Question("88+76=100?", "False")};

    int currentQuestion = 0;

    List<String> answerSaved = new ArrayList<String>() {};

    int score = 0;


    int seconds;
    boolean running;

    SQLiteDatabase sqLiteDatabase = null;

    CountDownTimer countDownTimer;

    public static String convertArrayToString(List<String> array) {
        String str = "";
        for (int i = 0; i < array.size(); i++) {
            str = str + array.get(i);
            if (i < array.size() - 1) {
                str = str + strSeparator;
            }
        }
        return str;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLHelper sqlHelper = new SQLHelper(this);
        sqLiteDatabase = sqlHelper.getWritableDatabase();

        updateUI();

        running = true;
        seconds = 20;

        TextView countTxt = findViewById(R.id.countDown);

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                if(seconds>0) {
                    countTxt.setText(" Reminding Time is " + seconds);
                }
                else{
                    seconds = 20;

                    currentQuestion = 0;
                    score = 0;
                    answerSaved.clear();

                    updateUI();

                }


                if(running)
                {
                    seconds--;
                }
                handler.postDelayed(this,1000);
            }
        });
    }

    public void updateUI() {
        TextView textView = findViewById(R.id.quiz_question);
        textView.setText(questions[this.currentQuestion].question);


        TextView scoreTxt = findViewById(R.id.score);
        String strValue = String.valueOf(score);
        scoreTxt.setText("Score: " + strValue);

        Button btn = findViewById(R.id.button2);
        btn.getText();
    }

    public void btnOnClick(View view) {

        Button btn = findViewById(view.getId());

        if (btn.getText().toString().equals(this.questions[currentQuestion].answer)) {
            btn.setBackgroundColor(Color.GREEN);
            score++;
            answerSaved.add(this.questions[currentQuestion].question + " : " + "TRUE");

        } else {
            btn.setBackgroundColor(Color.RED);
            answerSaved.add(this.questions[currentQuestion].question + " : " + "FALSE");
        }
        currentQuestion++;

        System.out.println("Question Number is " + this.currentQuestion);
        System.out.println("Question Length is " + (this.questions.length));


        if (this.currentQuestion < this.questions.length) {
            updateUI();


        } else {
            SQLHelper sqlHelper = new SQLHelper(this);

            String answerStr = convertArrayToString(answerSaved);

            int reqCode = 1;
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            showNotification(this, "You Finished :D", "Your Score is " + score, intent, reqCode);


            sqlHelper.sqlInsert(sqLiteDatabase, String.valueOf(score), answerStr);

            currentQuestion = 0;
            score = 0;
            answerSaved.clear();

            updateUI();
        }

        countDownTimer = new CountDownTimer(500, 500) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                btn.setBackgroundColor(Color.DKGRAY);
                countDownTimer.cancel();

            }
        }.start();

    }

    public void historyBtn(View view) {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
        running = false;
    }

    @Override
    protected void onDestroy() {
        sqLiteDatabase.close();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        running = true;
        super.onStart();
    }


    @Override
    protected void onStop() {
        running = false;
        super.onStop();
    }

    public void showNotification(Context context, String title, String message, Intent intent, int reqCode) {

        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.congrats);

        String CHANNEL_ID = "channel_name";

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bmp));

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(reqCode, notificationBuilder.build());
    }
}


class Question {
    String question;
    String answer;


    Question(String q, String a) {
        this.question = q;
        this.answer = a;
    }
}