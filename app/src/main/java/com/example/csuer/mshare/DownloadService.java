package com.example.csuer.mshare;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import java.io.File;

public class DownloadService extends Service {
    private  DownloadTask downloadTask;
    private String downloadUrl;
    private DownloadListener listener=new DownloadListener() {
        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1,getNotification("下载中...",progress));
        }

        @Override
        public void onSuccess() {
            downloadTask=null;
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("下载完成",-1));
            Toast.makeText(DownloadService.this,"下载成功",Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onCanceled() {
            downloadTask=null;
            stopForeground(true);
            Toast.makeText(DownloadService.this,"您取消下载了",Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onPause() {
            downloadTask=null;
            Toast.makeText(DownloadService.this,"暂停下载",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onFailed() {
             downloadTask =null;
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("下载失败,请重新下载或检查url地址",-1));
            Toast.makeText(DownloadService.this,"下载失败,请重新下载或检查url地址",Toast.LENGTH_LONG).show();
        }


    };
    private DownloadBinder mBinder=new DownloadBinder();
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }
    class DownloadBinder extends Binder{
        public void startDownload(String url){
            if(downloadTask==null)
            {
                downloadUrl =url;
                downloadTask=new DownloadTask(listener);
                downloadTask.execute(downloadUrl);
                startForeground(1,getNotification("下载中...",0));
               Toast.makeText(DownloadService.this,"开始下载",Toast.LENGTH_LONG).show();
            }
        }
        public void pauseDownload(){
            if(downloadTask!=null)
            {
                downloadTask.pauseDownload();
            }
        }
        public void cancelDownload(){}{
            if(downloadTask!=null){
                downloadTask.cancelDownload();
            }
            else{
                try{
                    if(downloadUrl!=null){
                        String filename=downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                        String directory= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                        File file=new File(directory+filename);
                        if(file.exists())
                            file.delete();
                        getNotificationManager().cancel(1);
                        stopForeground(true);
                        Toast.makeText(DownloadService.this,"您取消了下载",Toast.LENGTH_SHORT).show();
                    }

                }
                catch (Exception e)
                {

                }
            }
        }

    }
    private NotificationManager getNotificationManager(){
        return (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    }
    private Notification getNotification(String title,int progress){
        Intent intent=new Intent(this,DownloadPage.class);
        PendingIntent pi=PendingIntent.getActivity(this,0,intent,0);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.home_title_android);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.home_title_android));
        builder.setContentIntent(pi);
        builder.setContentTitle(title);
        if(progress>0){
            builder.setContentText(progress+"%");
            builder.setProgress(100,progress,false);
        }
        return builder.build();
    }

}
