package com.example.csuer.mshare;


import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by csuer on 2017/4/8.
 */

public class DownloadPage extends AppCompatActivity implements View.OnClickListener{
    private Spinner spi_one;
    private Spinner spi_two;
    private EditText editText;
    private DownloadService.DownloadBinder downloadBinder;
    private String myurl="";
    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder=(DownloadService.DownloadBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_download);
        spi_one=(Spinner)findViewById(R.id.spi_one);
        editText=(EditText)findViewById(R.id.et_url);
        initSpi_one();
        Button startD=(Button)findViewById(R.id.start_download);
        Button pauseD=(Button)findViewById(R.id.pause_download);
        Button cancelD=(Button)findViewById(R.id.cancel_download);
        startD.setOnClickListener(this);
        pauseD.setOnClickListener(this);
        cancelD.setOnClickListener(this);
        Intent intent=new Intent(this,DownloadService.class);
        startService(intent);
        bindService(intent,connection,BIND_AUTO_CREATE);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(DownloadPage.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        Toast.makeText(DownloadPage.this,"请注意你的流量",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case 1:
                if(grantResults.length>0&&grantResults[0]!=PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(DownloadPage.this,"拒绝权限，无法下载",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }

    private void initSpi_one()
    {
        List<Map<String, Object>> arrayList=new ArrayList<Map<String,Object>>();
        String []arry1={"",
                "https://codeload.github.com/zhangguom/Mshare/zip/master",
                "https://raw.githubusercontent.com/guolindev/eclipse/master/eclipse-inst-win64.exe",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1491750456800&di=1090df7328036aa3bdfa3ac3aff12e78&imgtype=0&src=http%3A%2F%2Fi0.hdslb.com%2Fbfs%2Farchive%2F38b0a649abd9adba7dc4e157640f88a1032143d6.jpg",
                "尚未找到生活大爆炸第七季第一集的资源"};
        Map<String, Object> map5=new HashMap<String, Object>();
        map5.put("bb",arry1[0]);
        map5.put("bb1", "下载内置资源");
        Map<String, Object> map1=new HashMap<String, Object>();
        map1.put("bb",arry1[4]);
        map1.put("bb1", "生活大爆炸第七季第一集");
        Map<String, Object> map2=new HashMap<String, Object>();
        map2.put("bb",arry1[2] );
        map2.put("bb1", "下载eclipse64位");
        Map<String, Object> map4=new HashMap<String, Object>();
        map4.put("bb",arry1[1] );
        map4.put("bb1", "下载本项目源代码");
        Map<String, Object> map3=new HashMap<String, Object>();
        map3.put("bb",arry1[3] );
        map3.put("bb1", "美女");
        arrayList.add(map5);
        arrayList.add(map4);
        arrayList.add(map2);
        arrayList.add(map3);
        arrayList.add(map1);
//        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, arry1);
        SimpleAdapter adapter1=new SimpleAdapter(this, arrayList, android.R.layout.simple_spinner_dropdown_item, new String[]{"bb1"}, new int[]{android.R.id.text1});
        spi_one.setAdapter(adapter1);
        spi_one.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                Spinner spinner=(Spinner)parent;
                Map<String, Object> map=(Map<String, Object>)spinner.getItemAtPosition(position);
                editText.setText((CharSequence) map.get("bb"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(downloadBinder==null)
            return ;
        switch (v.getId())
        {
            case R.id.start_download:
                String url=editText.getText().toString();
                if(url.contains("/"))
                    downloadBinder.startDownload(url);
               else
                Toast.makeText(DownloadPage.this,"请求资源不存在，下载失败",Toast.LENGTH_SHORT).show();

                break;
            case R.id.pause_download:
                downloadBinder.pauseDownload();
                break;
            case R.id.cancel_download:
                downloadBinder.pauseDownload();
                downloadBinder.cancelDownload();
                Intent intent=new Intent(DownloadPage.this,DownloadService.class);
                stopService(intent);
                Toast.makeText(DownloadPage.this,"取消下载",Toast.LENGTH_SHORT).show();
                finish();

                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
