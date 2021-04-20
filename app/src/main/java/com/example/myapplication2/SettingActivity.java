package com.example.myapplication2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;


public class SettingActivity extends AppCompatActivity {

    private SharedPreferences mSettings;
    private Boolean Theme;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSettings = getSharedPreferences(Setting.APP_PREFERENCES, Context.MODE_PRIVATE);
        Theme = mSettings.getBoolean(Setting.APP_PREFERENCES_DARK_THEME, false);

        if(Theme){
            setTheme(R.style.DarkTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Switch swi = (Switch) findViewById(R.id.switch1);

        if(Theme){
            swi.setChecked(true);
        }

        swi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SharedPreferences.Editor editor = mSettings.edit();
                    editor.putBoolean(Setting.APP_PREFERENCES_DARK_THEME, true);
                    editor.apply();

                    Intent intent=new Intent(SettingActivity.this,SettingActivity.class);
                    finish();
                    startActivity(intent);
                }
                else{
                    SharedPreferences.Editor editor = mSettings.edit();
                    editor.putBoolean(Setting.APP_PREFERENCES_DARK_THEME, false);
                    editor.apply();

                    Intent intent=new Intent(SettingActivity.this,SettingActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent=new Intent(SettingActivity.this,MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
