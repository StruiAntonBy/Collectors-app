package com.example.myapplication2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class Main6Activity extends AppCompatActivity implements View.OnClickListener{

    private EditText text;
    private Button button;
    private int Id;
    private String Folder;
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
        setContentView(R.layout.activity_main6);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Folder=getIntent().getExtras().getString("folder");
        Id=getIntent().getExtras().getInt("id");

        button=(Button) findViewById(R.id.button);
        button.setOnClickListener(this);

        text=findViewById(R.id.editText);
        text.setText(Folder);

        if(Theme) {
            button.setBackgroundColor(Color.parseColor("#363636"));
            button.setTextColor(Color.parseColor("#FFFAFA"));
        }

        dbHelper = new DBHelper(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setColorFilter(Color.argb(255, 255, 255, 255));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar
                        .make(view, "Do you want to delete the "+Folder+" folder?", Snackbar.LENGTH_LONG)
                        .setAction("Yes", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                db.delete(DBHelper.TABLE_CONTACTS1, DBHelper.KEY_ID + "=" + Id, null);
                                db.delete(DBHelper.TABLE_CONTACTS2, DBHelper.KEY_FOLDER_ID + "=" + Id, null);
                                dbHelper.close();

                                if(Theme){
                                    LayoutInflater inflater = getLayoutInflater();
                                    View layout = inflater.inflate(R.layout.custom_toast_dark,
                                            (ViewGroup) findViewById(R.id.custom_toast_container));

                                    TextView text = (TextView) layout.findViewById(R.id.text);
                                    text.setText("The " + Folder + " folder was deleted");

                                    Toast toast = new Toast(getApplicationContext());
                                    toast.setView(layout);
                                    toast.show();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),
                                            "The " + Folder + " folder was deleted", Toast.LENGTH_SHORT).show();
                                }

                                Intent intent = new Intent(Main6Activity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });
                if(Theme) {
                    snackbar.setActionTextColor(Color.parseColor("#FF8000"));
                }
                View v = snackbar.getView();
                TextView tv = (TextView) v.findViewById(R.id.snackbar_text);
                tv.setTextColor(Color.WHITE);
                snackbar.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        Folder=text.getText().toString();
        if (Folder.trim().length() == 0) {
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
            database.update(DBHelper.TABLE_CONTACTS1, contentValues, DBHelper.KEY_ID + "=" + Id, null);
            dbHelper.close();

            if(Theme){
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast_dark,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText("The " + Folder + " folder was changed");

                Toast toast = new Toast(getApplicationContext());
                toast.setView(layout);
                toast.show();
            }
            else {
                Toast.makeText(getApplicationContext(),
                        "The " + Folder + " folder was changed", Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent(Main6Activity.this, MainActivity.class);
            startActivity(intent);
        }
    }

}
