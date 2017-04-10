package com.example.csuer.mshare;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private DrawerLayout drawerLayout;
    private ImageView imageView_music;
    private ImageView imageView_game;
    private ImageView imageView_search;
    private ImageView imageView_chat;
    private NavigationView navigationView;
    private PercentRelativeLayout percentRelativeLayout;
    private View view_head;
    private TextView user_name;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        imageView_music=(ImageView)findViewById(R.id.music);
        imageView_game=(ImageView)findViewById(R.id.game);
        imageView_search=(ImageView)findViewById(R.id.search);
        imageView_chat=(ImageView)findViewById(R.id.chat);
        imageView_music.setOnClickListener(this);
        imageView_game.setOnClickListener(this);
        imageView_search.setOnClickListener(this);
        imageView_chat.setOnClickListener(this);
        navigationView=(NavigationView)findViewById(R.id.nav_view);
        //初始化headview
        view_head=navigationView.getHeaderView(0);
        //初始化sharepreferrence
        preferences= PreferenceManager.getDefaultSharedPreferences(this);
        String temp=preferences.getString("username","简单");
        //用户名
        user_name=(TextView)view_head.findViewById(R.id.user_name);
        user_name.setText(temp);
        navigationView.setCheckedItem(R.id.call);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.call:
                        navigationView.setCheckedItem(R.id.call);
                        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
                            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
                        else call();
                        break;
                    case R.id.download:
                        navigationView.setCheckedItem(R.id.download);
                        Intent intent_download=new Intent(MainActivity.this,DownloadPage.class);
                        startActivity(intent_download);
                        break;
                    case R.id.set:
                        navigationView.setCheckedItem(R.id.set);
                        Intent intent_user=new Intent(MainActivity.this,UserCenter.class);
                        startActivityForResult(intent_user,3);
                        break;
                    case R.id.about:
                        navigationView.setCheckedItem(R.id.about);
                        Intent intent_about=new Intent(MainActivity.this,About.class);
                        startActivity(intent_about);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        percentRelativeLayout=(PercentRelativeLayout) findViewById(R.id.mainpage);
        percentRelativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action =event.getActionMasked();
                switch (action)
                {
                    case MotionEvent.ACTION_DOWN:
                        drawerLayout.openDrawer(GravityCompat.START);
                        break;
                    default:
                }
                return false;
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
//            case R.id.search:
//                Toast.makeText(this,"此功能尚未开放",Toast.LENGTH_SHORT).show();
//                break;
           case R.id.share:
               drawerLayout.openDrawer(GravityCompat.START);
                break;
//            case R.id.more:
//                Toast.makeText(this,"此功能尚未开放",Toast.LENGTH_SHORT).show();
//                break;
            default:
                break;
        }
        return true;
    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.music:
                Intent intent_music=new Intent(MainActivity.this,Music.class);
                startActivity(intent_music);
                break;
            case R.id.game:
                Intent intent_game=new Intent(MainActivity.this,HitMouse.class);
                startActivity(intent_game);
                break;
            case R.id.search:
                Intent intent_search=new Intent(MainActivity.this,Search.class);
                startActivity(intent_search);
                break;
            case  R.id.chat:
                Intent intent_location=new Intent(MainActivity.this,Location.class);
                startActivity(intent_location);
                break;
            default:;
        }
    }
    private  void call()
    {
        AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("温馨提醒");
        dialog.setMessage("你即将打电话给作者，确定吗 ?");
        dialog.setCancelable(false);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    Intent intent=new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:13100250227"));
                    startActivity(intent);
                }
                catch (SecurityException e){
                    e.printStackTrace();
                }
            }
        });
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    call();
                else Toast.makeText(this,"sorry，你拒绝了联系作者这项权限！",Toast.LENGTH_SHORT).show();
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        Bundle bundle = data.getExtras();
        String username3 = (String) bundle.getString("username");
        switch (requestCode) {
            case 3:
                user_name.setText(username3);
                break;
            default:
                break;
        }
    }

    }
