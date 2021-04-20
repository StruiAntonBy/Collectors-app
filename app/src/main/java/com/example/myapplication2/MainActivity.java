package com.example.myapplication2;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends ListActivity implements AdapterView.OnItemLongClickListener {

    private ArrayAdapter<String> FolderAdapter,ItemAdapter;
    private DBHelper dbHelper;
    private ArrayList<DirectoryDB> list;
    private ArrayList<ElementDB> Array;
    private DirectoryDB directory;
    private FloatingActionButton fab1,fab2,fab3;
    private boolean isFABOpen=false;
    private SharedPreferences mSettings;
    private boolean Theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSettings = getSharedPreferences(Setting.APP_PREFERENCES, Context.MODE_PRIVATE);
        Theme = mSettings.getBoolean(Setting.APP_PREFERENCES_DARK_THEME, false);

        if(Theme){
            setTheme(R.style.DarkTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper=new DBHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS1, null, null, null, null, null, null);
        list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int folderIndex = cursor.getColumnIndex(DBHelper.KEY_FOLDER);
            do {
                list.add(new DirectoryDB(cursor.getInt(idIndex),cursor.getString(folderIndex)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        dbHelper.close();

        String[] ArrayFolder=new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            DirectoryDB element=list.get(i);
            ArrayFolder[i]=element.getFolder();
        }

        ArrayList<String> FolderList = new ArrayList<>(Arrays.asList(ArrayFolder));

        if(Theme){
            FolderAdapter = new ArrayAdapter<>(this,
                    R.layout.rowlayoutdark, R.id.label, FolderList);

            ItemAdapter = new ArrayAdapter<>(this,
                    R.layout.rowlayiutitemdark,R.id.label,new ArrayList<String>());
        }
        else {
            FolderAdapter = new ArrayAdapter<>(this,
                    R.layout.rowlayout, R.id.label, FolderList);

            ItemAdapter = new ArrayAdapter<>(this,
                    R.layout.rowlayoutitem, R.id.label, new ArrayList<String>());
        }

        setListAdapter(FolderAdapter);

        getListView().setOnItemLongClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab1.setColorFilter(Color.argb(255, 255, 255, 255));
        fab2.setColorFilter(Color.argb(255, 255, 255, 255));
        fab3.setColorFilter(Color.argb(255, 255, 255, 255));
        fab1.hide();
        fab2.hide();
        fab3.hide();

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
                if (getListAdapter() == FolderAdapter){
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else{
                    fab1.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.ic_launcher_home));
                    fab2.hide();
                    fab1.hide();
                    isFABOpen=false;

                    setListAdapter(FolderAdapter);
                    FolderAdapter.notifyDataSetChanged();
                }
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getListAdapter() == FolderAdapter){
                    Intent intent = new Intent(MainActivity.this, Main5Activity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                    intent.putExtra("folder_id",directory.getId());
                    startActivity(intent);
                }
            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (getListAdapter() == FolderAdapter){
            if(Theme){
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast_dark,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText("You have selected " + l.getItemAtPosition(position).toString());

                Toast toast = new Toast(getApplicationContext());
                toast.setView(layout);
                toast.show();
            }
            else {
                Toast.makeText(getApplicationContext(),
                        "You have selected " + l.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
            }

            ItemAdapter.clear();

            directory=list.get(position);
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            Cursor cursor = database.query(DBHelper.TABLE_CONTACTS2, null, DBHelper.KEY_FOLDER_ID+"="+directory.getId(), null, null, null, null);
            Array = new ArrayList<>();
            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                int folder_idIndex = cursor.getColumnIndex(DBHelper.KEY_FOLDER_ID);
                int titleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE);
                int noteIndex = cursor.getColumnIndex(DBHelper.KEY_NOTE);
                int imageIndex = cursor.getColumnIndex(DBHelper.KEY_IMAGE);
                do {
                    Array.add(new ElementDB(cursor.getInt(idIndex),cursor.getInt(folder_idIndex),
                            cursor.getString(titleIndex),cursor.getString(noteIndex),cursor.getBlob(imageIndex)));
                    ItemAdapter.add(cursor.getString(titleIndex));
                } while (cursor.moveToNext());
            }
            cursor.close();
            dbHelper.close();

            fab1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_launcher_return));

            fab1.hide();
            fab2.hide();
            fab3.hide();
            isFABOpen=false;

            setListAdapter(ItemAdapter);
            ItemAdapter.notifyDataSetChanged();
        }
        else {
            if(Theme){
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast_dark,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText("You have selected " + l.getItemAtPosition(position).toString());

                Toast toast = new Toast(getApplicationContext());
                toast.setView(layout);
                toast.show();
            }
            else {
                Toast.makeText(getApplicationContext(),
                        "You have selected " + l.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent(MainActivity.this, Main3Activity.class);
            ElementDB element = Array.get(position);
            intent.putExtra("id", element.getId());
            intent.putExtra("folder_id",element.getFolder_Id());
            intent.putExtra("title", element.getTitle());
            intent.putExtra("note", element.getNote());
            intent.putExtra("image",element.getImage());
            startActivity(intent);
        }
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final String selectedItem = parent.getItemAtPosition(position).toString();
        if (getListAdapter() == FolderAdapter){
            DirectoryDB element=list.get(position);
            Intent intent = new Intent(MainActivity.this, Main6Activity.class);
            intent.putExtra("id", element.getId());
            intent.putExtra("folder",element.getFolder());
            startActivity(intent);
            return true;
        }
        else {
            ElementDB element = Array.get(position);
            final String title = element.getTitle();
            final String note = element.getNote();
            final int Id = element.getId();
            final int folder_id=element.getFolder_Id();
            final byte[] image=element.getImage();

            Snackbar snackbar = Snackbar
                    .make(view, "The "+ selectedItem +" collection was deleted.", Snackbar.LENGTH_LONG)
                    .setAction("Return?", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(DBHelper.KEY_TITLE, title);
                            contentValues.put(DBHelper.KEY_NOTE, note);
                            contentValues.put(DBHelper.KEY_FOLDER_ID, folder_id);
                            contentValues.put(DBHelper.KEY_ID, Id);
                            contentValues.put(DBHelper.KEY_IMAGE,image);
                            db.insert(DBHelper.TABLE_CONTACTS2, null, contentValues);
                            dbHelper.close();

                            ItemAdapter.insert(selectedItem, position);
                            Snackbar snackbar1 = Snackbar.make(view, "Returned!", Snackbar.LENGTH_SHORT);
                            View v = snackbar1.getView();
                            TextView tv = (TextView) v.findViewById(R.id.snackbar_text);
                            tv.setTextColor(Color.WHITE);
                            snackbar1.show();
                        }
                    });
            View v = snackbar.getView();
            TextView tv = (TextView) v.findViewById(R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            if(Theme) {
                snackbar.setActionTextColor(Color.parseColor("#FF8000"));
            }
            snackbar.show();

            ItemAdapter.remove(selectedItem);
            ItemAdapter.notifyDataSetChanged();

            SQLiteDatabase database = dbHelper.getWritableDatabase();
            database.delete(DBHelper.TABLE_CONTACTS2, DBHelper.KEY_ID + "=" + Id, null);
            dbHelper.close();
            return true;
        }
    }

    private void showFABMenu(){
        fab1.show();
        fab2.show();
        if(getListAdapter() == FolderAdapter) {
            fab3.show();
            fab3.animate().translationY(-getResources().getDimension(R.dimen.standard_175));
        }
        isFABOpen = true;
        fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_75));
        fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_125));
    }

    private void closeFABMenu(){
        fab1.hide();
        fab2.hide();
        if(getListAdapter() == FolderAdapter){
            fab3.hide();
            fab3.animate().translationY(0);
        }
        isFABOpen=false;
        fab1.animate().translationY(0);
        fab2.animate().translationY(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(Setting.APP_PREFERENCES_DARK_THEME, Theme);
        editor.apply();
    }

}
