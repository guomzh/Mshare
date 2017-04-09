package com.example.csuer.mshare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by csuer on 2017/4/9.
 */

public class UserCenter extends AppCompatActivity {
    private FloatingActionButton button;
    private EditText editText_username;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lyaout_usercenter);
        button=(FloatingActionButton)findViewById(R.id.fab);
        editText_username=(EditText)findViewById(R.id.change_username);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(UserCenter.this);
        editor=sharedPreferences.edit();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username=editText_username.getText().toString();
                if(username.equals(""))
                    Toast.makeText(UserCenter.this,"用户名不能为空",Toast.LENGTH_SHORT).show();
                else{
                    editor.putString("username",username);
                    editor.apply();
                    Toast.makeText(UserCenter.this,"更改成功",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(UserCenter.this,MainActivity.class);
                    intent.putExtra("username", username);
                    setResult(0, intent);
                    finish();
                }

            }
        });

    }
}
