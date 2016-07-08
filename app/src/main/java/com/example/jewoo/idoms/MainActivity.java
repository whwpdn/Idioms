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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity implements IdomsFragment.OnButtonListener {

    // sqlite database member
    private IdomsSqliteOpenHelper mDBHelper;
    private String mDBName = "StudyDatabase.db";
    private SQLiteDatabase mDB;
    int miDBVersion =1; // database version
    private static final String TAG_DB = "SQLITE";

    //question member
    private int mCurrentId=1;
    private String mCorrectAnswer="";
    private boolean mIsShowAns = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        // set sqlite ..
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

        //copy db file
        try {
            File outfile = new File("/data/data/com.example.jewoo.idoms/databases/"+mDBName);
            AssetManager assetManager = getResources().getAssets();
            InputStream is = assetManager.open("databases/StudyDatabase.db",AssetManager.ACCESS_BUFFER);
            long fileSize = is.available();
            long test = outfile.length();
            //if(fileSize > test) {
                byte[] tempData = new byte[(int) fileSize];
                is.read(tempData);
                is.close();
                outfile.createNewFile();
                FileOutputStream fo = new FileOutputStream(outfile);
                fo.write(tempData);
                fo.close();
            //}
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // set first question - randomly one in total
        mCurrentId = getQuestion(mCurrentId);


        // set spinner - 1 to 20
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.day, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
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
       FragmentManager fmManager = getFragmentManager();
        IdomsFragment fmIdoms=(IdomsFragment)fmManager.findFragmentById(R.id.fragment);

        fmIdoms.setQuestionText(strQuestion);
        fmIdoms.setAnswerText("");

        mIsShowAns = false;
    }

    private void showCorrectAnswer()
    {
        if(mIsShowAns) return;

        FragmentManager fmManager = getFragmentManager();
        IdomsFragment fmIdoms=(IdomsFragment)fmManager.findFragmentById(R.id.fragment);
        fmIdoms.setAnswerText(mCorrectAnswer);
        mIsShowAns = true;
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

    // IdomsFragment interface 구현
    public void onBtnNextClicked(){
        mCurrentId = getQuestion(mCurrentId);
    }
    public void onBtnCheckAnswerClicked(){ showCorrectAnswer();}


}
