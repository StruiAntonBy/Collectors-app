package com.example.myapplication2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Main4Activity extends AppCompatActivity implements View.OnClickListener{

    private Button button1,button2;
    private EditText TextTitle, TextNote;
    private DBHelper dbHelper;
    private ImageView img;
    private String Title,Note;
    private int Id;
    private int Folder_id;
    private byte[] Image;
    private final int Pick_image = 1;
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
        setContentView(R.layout.activity_main4);

        Title = getIntent().getExtras().getString("title");
        Note = getIntent().getExtras().getString("note");
        Id=getIntent().getExtras().getInt("id");
        Folder_id=getIntent().getExtras().getInt("folder_id");
        Image=getIntent().getExtras().getByteArray("image");

        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextTitle=findViewById(R.id.editText1);
        TextTitle.setText(Title);

        TextNote=findViewById(R.id.editText2);
        TextNote.setText(Note);

        img=findViewById(R.id.imageView1);
        img.setImageBitmap(BitmapWork.getBitmap(Image));
        img.setOnClickListener(v -> {
            if (Image!=null) {
                Intent intent = new Intent(Main4Activity.this, FotoActivity.class);
                intent.putExtra("image", Image);
                startActivity(intent);
            }
        });

        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);

        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);

        if(Theme) {
            button1.setBackgroundColor(Color.parseColor("#363636"));
            button1.setTextColor(Color.parseColor("#FFFAFA"));
            button2.setBackgroundColor(Color.parseColor("#363636"));
            button2.setTextColor(Color.parseColor("#FFFAFA"));
        }

        dbHelper = new DBHelper(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setColorFilter(Color.argb(255, 255, 255, 255));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextTitle=findViewById(R.id.editText1);
                TextNote=findViewById(R.id.editText2);
                String title = TextTitle.getText().toString();
                String note = TextNote.getText().toString();

                if(title.trim().length() == 0){
                    if(Theme){
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.custom_toast_dark,
                                (ViewGroup) findViewById(R.id.custom_toast_container));

                        TextView text = (TextView) layout.findViewById(R.id.text);
                        text.setText("Enter a title!");

                        Toast toast = new Toast(getApplicationContext());
                        toast.setView(layout);
                        toast.show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                "Enter a title!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    SQLiteDatabase database = dbHelper.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBHelper.KEY_TITLE, title);
                    contentValues.put(DBHelper.KEY_NOTE, note);
                    contentValues.put(DBHelper.KEY_FOLDER_ID, Folder_id);
                    contentValues.put(DBHelper.KEY_IMAGE, Image);
                    database.update(DBHelper.TABLE_CONTACTS2, contentValues, DBHelper.KEY_ID + "= ?", new String[]{String.valueOf(Id)});
                    dbHelper.close();
                    if(Theme){
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.custom_toast_dark,
                                (ViewGroup) findViewById(R.id.custom_toast_container));

                        TextView text = (TextView) layout.findViewById(R.id.text);
                        text.setText("The " + Title + " was corrected");

                        Toast toast = new Toast(getApplicationContext());
                        toast.setView(layout);
                        toast.show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                "The " + Title + " was corrected", Toast.LENGTH_SHORT).show();
                    }

                    Intent intent = new Intent(Main4Activity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                if(Theme){
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toast_dark,
                            (ViewGroup) findViewById(R.id.custom_toast_container));

                    TextView text = (TextView) layout.findViewById(R.id.text);
                    text.setText("Select a photo");

                    Toast toast = new Toast(getApplicationContext());
                    toast.setView(layout);
                    toast.show();
                }
                else {
                    Toast.makeText(getApplicationContext(),
                            "Select a photo", Toast.LENGTH_SHORT).show();
                }
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, Pick_image);
                break;
            case R.id.button2:
                if(Theme){
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toast_dark,
                            (ViewGroup) findViewById(R.id.custom_toast_container));

                    TextView text = (TextView) layout.findViewById(R.id.text);
                    text.setText("The photo was deleted");

                    Toast toast = new Toast(getApplicationContext());
                    toast.setView(layout);
                    toast.show();
                }
                else {
                    Toast.makeText(getApplicationContext(),
                            "The photo was deleted", Toast.LENGTH_SHORT).show();
                }
                Image=null;
                img.setImageBitmap(null);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case Pick_image:
                if(resultCode == RESULT_OK){
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        Image=BitmapWork.getArrayByte(selectedImage);
                        img.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
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
}
