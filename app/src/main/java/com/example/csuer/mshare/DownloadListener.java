package com.example.csuer.mshare;

/**
 * Created by csuer on 2017/4/8.
 */

public interface DownloadListener {
    void onProgress (int progress);
    void onSuccess();
    void onPause();
    void onFailed();
    void onCanceled();
}
