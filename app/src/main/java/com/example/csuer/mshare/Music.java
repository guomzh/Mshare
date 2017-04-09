package com.example.csuer.mshare;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by csuer on 2017/4/7.
 */

public class Music extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private List<Map<String, Object>> audioList=new ArrayList<Map<String,Object>>();
    private int currentItem=0;
    private ImageView pause;
    private ImageView pre;
    private ImageView next;
    private SeekBar seekBar_music;
    private TextView music_name;
    private TextView singer_name;
    private Spinner spinner;
    private int playsort=0;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_music);
        //初始化spinner
        initSpi_one();
         music_name=(TextView)findViewById(R.id.music_name);
         singer_name=(TextView)findViewById(R.id.singer_name);
        seekBar_music=(SeekBar)findViewById(R.id.seekbar_music);
        seekBar_music.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser==true&&mediaPlayer.isPlaying()){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mediaPlayer=new MediaPlayer();
        pause=(ImageView) findViewById(R.id.pause);
        pre=(ImageView) findViewById(R.id.pre);
        next=(ImageView) findViewById(R.id.next);
        audioList();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                nextMusic();
            }
        });
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preMusic();
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying())
                {mediaPlayer.pause();
                    handler.removeCallbacks(updateThread);
                    ((ImageView)v).setImageResource(R.drawable.play);
                }
                else
                {
                    mediaPlayer.start();
                    mediaPlayer.seekTo(seekBar_music.getProgress());
                    handler.post(updateThread);
                    ((ImageView)v).setImageResource(R.drawable.pause);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextMusic();
            }
        });
    }



    private void audioList(){
        if(ContextCompat.checkSelfPermission(Music.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(Music.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        else
        {
          query();
        }
//        ArrayAdapter<String>adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,audioList);
        SimpleAdapter adapter=new SimpleAdapter(this, audioList, android.R.layout.simple_list_item_1, new String[]{"title"}, new int[]{android.R.id.text1});
        ListView listView=(ListView)findViewById(R.id.list);
        listView.setAdapter(adapter);
        int j=new Random().nextInt(audioList.size());
        music_name.setText((String) (audioList.get(j).get("title")));
        singer_name.setText((String) (audioList.get(j).get("artist")));
        playMusic((String) (audioList.get(j).get("url")));
        mediaPlayer.pause();
        pause.setImageResource(R.drawable.play);
        handler.removeCallbacks(updateThread);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentItem=position;
                playMusic((String) (audioList.get(currentItem).get("url")));
                music_name.setText((String) (audioList.get(currentItem).get("title")));
                singer_name.setText((String) (audioList.get(currentItem).get("artist")));
            }
        });
    }
    Handler handler = new Handler();
    Runnable updateThread = new Runnable(){
        public void run() {
            //获得歌曲现在播放位置并设置成播放进度条的值
            seekBar_music.setProgress(mediaPlayer.getCurrentPosition());
            //每次延迟100毫秒再启动线程
            handler.postDelayed(updateThread, 100);
        }
    };
    public void playMusic(String path)
    {
        try {
            if(mediaPlayer.isPlaying())
            {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();;
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
            pause.setImageResource(R.drawable.pause);
            seekBar_music.setMax(mediaPlayer.getDuration());
            handler.post(updateThread);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void nextMusic()
    {
        if(playsort==1)
        {
            if(++currentItem>=audioList.size())
                currentItem=0;
        }
        else if(playsort==0)
        {
            currentItem=new Random().nextInt(audioList.size());
        }
        else{

        }
        playMusic((String) (audioList.get(currentItem).get("url")));
        music_name.setText((String) (audioList.get(currentItem).get("title")));
        singer_name.setText((String) (audioList.get(currentItem).get("artist")));
    }
    public void preMusic()
    {
        if(playsort==1)
        {
            if(--currentItem>=0)
            {
                if(currentItem>=audioList.size())
                    currentItem=0;

            }
            else currentItem=audioList.size()-1;
        }
        else if(playsort==0)
        {
            currentItem=new Random().nextInt(audioList.size());
        }
        else{

        }
        playMusic((String)audioList.get(currentItem).get("url"));
        music_name.setText((String) (audioList.get(currentItem).get("title")));
        singer_name.setText((String) (audioList.get(currentItem).get("artist")));
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    query();
                else Toast.makeText(this,"sorry，你拒绝了联系作者这项权限！",Toast.LENGTH_SHORT).show();
                break;
            default:
        }
    }
    private void query()
    {
        Cursor cursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if(cursor!=null)
        {
            while (cursor.moveToNext())
            {
                String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                Map<String ,Object> map=new HashMap<>();
                map.put("url",url);
                map.put("title",title);
                map.put("artist",artist);
                audioList.add(map);
            }

        }
        if(cursor!=null)
            cursor.close();
    }
    @Override
    protected void onDestroy() {
        if(mediaPlayer.isPlaying())
            mediaPlayer.stop();
        mediaPlayer.release();
        handler.removeCallbacks(updateThread);
        super.onDestroy();

    }
    private void initSpi_one()
    {
        spinner=(Spinner)findViewById(R.id.spi_sort);
        String []arry1={"随机播放","顺序播放","单曲循环"};
        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, arry1);
        spinner.setAdapter(adapter1);
        spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                playsort=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

    }
}
