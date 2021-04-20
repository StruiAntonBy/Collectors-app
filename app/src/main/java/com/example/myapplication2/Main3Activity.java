package com.example.myapplication2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Main3Activity extends AppCompatActivity {

    private TextView text1,text2;
    private ImageView img;
    private String Title;
    private String Note;
    private int Folder_id;
    private int Id;
    private byte[] Image;
    private FloatingActionButton fab1,fab2;
    private boolean isFABOpen=false;
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
        setContentView(R.layout.activity_main3);

        Title = getIntent().getExtras().getString("title");
        Note = getIntent().getExtras().getString("note");
        Id=getIntent().getExtras().getInt("id");
        Folder_id=getIntent().getExtras().getInt("folder_id");
        Image=getIntent().getExtras().getByteArray("image");

        text1=findViewById(R.id.textView1);
        text1.setText("Title:"+Title);

        text2=findViewById(R.id.textView2);
        text2.setText("Note:"+Note);

        img=findViewById(R.id.imageView);
        img.setImageBitmap(BitmapWork.getBitmap(Image));
        img.setOnClickListener(v -> {
            if(Image!=null) {
                Intent intent = new Intent(Main3Activity.this, FotoActivity.class);
                intent.putExtra("image", Image);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setColorFilter(Color.argb(255, 255, 255, 255));
        fab1.hide();
        fab2.hide();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main3Activity.this, Main4Activity.class);
                intent.putExtra("id", Id);
                intent.putExtra("title", Title);
                intent.putExtra("note", Note);
                intent.putExtra("folder_id",Folder_id);
                intent.putExtra("image",Image);
                startActivity(intent);
            }
        });
    }

    private void showFABMenu(){
        fab1.show();
        fab2.show();
        isFABOpen=true;
        fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_75));
        fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_125));
    }

    private void closeFABMenu(){
        fab1.hide();
        fab2.hide();
        isFABOpen=false;
        fab1.animate().translationY(0);
        fab2.animate().translationY(0);
    }

}
