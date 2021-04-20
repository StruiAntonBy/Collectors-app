package com.example.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Main5Activity extends AppCompatActivity implements View.OnClickListener{

    private Button button;
    private EditText Text;
    private DBHelper dbHelper;
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
        setContentView(R.layout.activity_main5);

        dbHelper=new DBHelper(this);

        Text=findViewById(R.id.editText);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

        if(Theme) {
            button.setBackgroundColor(Color.parseColor("#363636"));
            button.setTextColor(Color.parseColor("#FFFAFA"));
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setColorFilter(Color.argb(255, 255, 255, 255));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main5Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        String Folder=Text.getText().toString();
        if (Folder.trim().length() == 0){
            if(Theme){
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast_dark,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText("Enter the folder name!");

                Toast toast = new Toast(getApplicationContext());
                toast.setView(layout);
                toast.show();
            }
            else {
                Toast.makeText(getApplicationContext(),
                        "Enter the folder name!", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.KEY_FOLDER, Folder);
            database.insert(DBHelper.TABLE_CONTACTS1, null, contentValues);
            dbHelper.close();
            if(Theme){
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast_dark,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText("The " + Folder + " folder was added");

                Toast toast = new Toast(getApplicationContext());
                toast.setView(layout);
                toast.show();
            }
            else {
                Toast.makeText(getApplicationContext(),
                        "The " + Folder + " folder was added", Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent(Main5Activity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
