package com.example.csuer.mshare;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by csuer on 2017/4/7.
 */

public class HitMouse extends AppCompatActivity {
    private int i=0;
    private ImageView mouse;
    private int[][]position=new int[][]{{870,156},{35,985},{500,1055},{555,555},{625,182},
            {50,50},{1100,1300},{425,888},{99,366},{785,1268},{488,1200},{18,1300}};
    private Button start;
//    private Thread t;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private int game_record;
    private TextView textView_record;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hit_mouse);
        mouse=(ImageView)findViewById(R.id.mouse);
        mouse.setVisibility(View.INVISIBLE);
        start=(Button)findViewById(R.id.start_game);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startgame();
            }
        });
       preferences= PreferenceManager.getDefaultSharedPreferences(this);
       game_record=preferences.getInt("game_record",0);
       textView_record=(TextView)findViewById(R.id.text_game_record);
       textView_record.setText("     最高记录:\n\n"+"10s打掉"+String.valueOf(game_record)+"只地鼠");

    }
    Handler handler = new Handler();
    Runnable thread = new Runnable(){
        public void run() {
            int index=new Random().nextInt(position.length);
                   mouse.setX(position[index][0]);
                   mouse.setY(position[index][1]);
                   mouse.setVisibility(View.VISIBLE);
            mouse.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.setVisibility(View.INVISIBLE);
                    i++;
                    mouse.setVisibility(View.INVISIBLE);
                    Toast.makeText(HitMouse.this,"已经打了"+i+"只地鼠",Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
            handler.postDelayed(thread, new Random().nextInt(600)+100);
        }
    };
    private void startgame(){

        handler.post(thread);
        Timer timer=new Timer();
        timer.schedule(new endgame(),10000);
    }
class  endgame extends TimerTask{
    @Override
    public void run()
    {

        handler.removeCallbacks(thread);
        if(i>game_record){
            editor=preferences.edit();
            editor.putInt("game_record",i);
            editor.apply();
        }

        showUi();
        Intent intent=new Intent(HitMouse.this,HitMouse.class);
        PendingIntent pi=PendingIntent.getActivity(HitMouse.this,0,intent,0);
        NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Notification notification=new NotificationCompat.Builder(HitMouse.this).setContentText("打地鼠游戏结果")
                .setContentText("10s内你一共打了"+i+"只地鼠 !").setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.home_title_android).setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.home_title_android))
                .setAutoCancel(true).setVibrate(new long[]{0,1000}).setPriority(NotificationCompat.PRIORITY_MAX).setContentIntent(pi).build();
        manager.notify(1,notification);

        i=0;
    }
}
    private void showUi(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                game_record=preferences.getInt("game_record",0);
                textView_record.setText("     最高记录:\n" +
                        "\n"+"10s打掉"+game_record+"只地鼠");
            }
        });
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(thread);
        super.onDestroy();
    }
}
