package com.example.jewoo.idoms;

import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity {

    private static final String TAG_ID="id";
    private static final String TAG_NAME="name";

    private IdomsSqliteOpenHelper mDBHelper;
    private String mDBName = "StudyDatabase.db";
    int miDBVersion =1; // database version
    private SQLiteDatabase mDB;
    private static final String TAG_DB = "SQLITE";
    public static TextView mQuestionTextView =null;
    private int mCurrentId=1;
    private String mCorrectAnswer="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        // sqlite ..
        mDBHelper = new IdomsSqliteOpenHelper(
                this,mDBName,null // cursorFactory null : standart cursor
                , miDBVersion) ;

        try{
            mDB = mDBHelper.getWritableDatabase();
            //getReadableDatabase() // only read

        } catch(SQLiteException e){
            e.printStackTrace();
            Log.e(TAG_DB, " can't get database");
            finish();
        }
        //select();
        try {
            File outfile = new File("/data/data/com.example.jewoo.idoms/databases/"+mDBName);
            AssetManager assetManager = getResources().getAssets();
            InputStream is = assetManager.open("databases/StudyDatabase.db",AssetManager.ACCESS_BUFFER);
            long fileSize = is.available();
            long test = outfile.length();
            if(fileSize > test) {
                byte[] tempData = new byte[(int) fileSize];
                is.read(tempData);
                is.close();
                outfile.createNewFile();
                FileOutputStream fo = new FileOutputStream(outfile);
                fo.write(tempData);
                fo.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        mCurrentId = getQuestion(mCurrentId);
        ///




    }
    private int getQuestion(int _id){
        Cursor cursor = mDB.rawQuery("select english, meaning from idoms where _id="+_id+";",null);
        //Cursor cursor = mDB.rawQuery("select * from idoms;",null);
        //while(cursor.moveToNext())
        //{
        //    String sss = cursor.getString(1);
        //    int iadf = cursor.getInt(0);
        //}
        _id++;
        while(cursor.moveToNext()){
            setData(cursor.getString(1));
            mCorrectAnswer = cursor.getString(0);
        }
        return _id;
    }
    private void setData(String strQuestion)
    {
        //mQuestionTextView.setText(strQuestion);
     //  FragmentManager fmManager = getFragmentManager();
      //  IdomsFragment fmIdoms=(IdomsFragment)fmManager.findFragmentById(R.layout.fragment_idoms);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
