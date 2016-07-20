package jewoo.idioms;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import android.app.FragmentManager;


public class MainActivity extends AppCompatActivity implements IdiomsFragment.OnListener {

    // sqlite database member
    private IdiomsSqliteOpenHelper mDBHelper;
    private String mDBName = "StudyDatabase.db";
    private SQLiteDatabase mDB;
    int miDBVersion =1; // database version
    private static final String TAG_DB = "SQLITE";

    //question member
    private int mCurrentId=1;
    private String mCorrectAnswer="";
    private int mQuestionTotalCnt =0;

    // Selected Day
    private ArrayList<IdiomsData> mListIdoms;
    private boolean mSelectedMode = false;
    private int mSelectedId =0;

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


        mListIdoms = new ArrayList<IdiomsData>();
        // set sqlite ..
        mDBHelper = new IdiomsSqliteOpenHelper(
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
//
//        //copy db file
//        try {
//            File outfile = new File("/data/data/com.example.jewoo.idioms/databases/"+mDBName);
//            AssetManager assetManager = getResources().getAssets();
//            InputStream is = assetManager.open("databases/StudyDatabase.db",AssetManager.ACCESS_BUFFER);
//            long fileSize = is.available();
//            long test = outfile.length();
//            //if(fileSize > test) {
//                byte[] tempData = new byte[(int) fileSize];
//                is.read(tempData);
//                is.close();
//                outfile.createNewFile();
//                FileOutputStream fo = new FileOutputStream(outfile);
//                fo.write(tempData);
//                fo.close();
//            //}
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }

        // set first question - randomly one in total
        //mCurrentId = getQuestion(mCurrentId);
        mQuestionTotalCnt = getTotalCnt();


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


    private int getQuestion(int _id){
        Cursor cursor = mDB.rawQuery("select english, meaning from idioms where _id="+_id+";",null);
        while(cursor.moveToNext()){
            setData(cursor.getString(1));
            mCorrectAnswer = cursor.getString(0);
        }
        return _id;
    }

    private int getTotalCnt(){
        Cursor cursor = mDB.rawQuery("SELECT count(_id) FROM idioms;",null);
        int total =0;
        while( cursor.moveToNext()){
            total = cursor.getInt(0);
        }
         return total;
    }

    private void setData(String strQuestion)
    {
        //mQuestionTextView.setText(strQuestion);
       FragmentManager fmManager = getFragmentManager();
        IdiomsFragment fmIdoms=(IdiomsFragment)fmManager.findFragmentById(R.id.fragment);

        fmIdoms.setQuestionText(strQuestion);

    }

    private void showCorrectAnswer()
    {
        FragmentManager fmManager = getFragmentManager();
        IdiomsFragment fmIdoms=(IdiomsFragment)fmManager.findFragmentById(R.id.fragment);
        fmIdoms.setAnswerText(mCorrectAnswer);
    }

    // when lesson selected, add Idoms.
    private void setSelectedDayIdoms(int days[]){
        mListIdoms.clear();
        mSelectedMode = true;
        mSelectedId = 0;

        for(int i=0; i<days.length ; i++){

            Cursor cursor = mDB.rawQuery("select _id, meaning, english from idioms where lesson="+days[i]+";",null);

            while(cursor.moveToNext()){
                IdiomsData idoms = new IdiomsData(cursor.getInt(0),cursor.getString(1),cursor.getString(2));
                mListIdoms.add(idoms);
            }
        }

        setData(mListIdoms.get(mSelectedId).getmQuestion());
        mCorrectAnswer = mListIdoms.get(mSelectedId).getmAnswer();
    }

    private int setSelectedDayQuestion(int idx){
        setData(mListIdoms.get(idx).getmQuestion());
        mCorrectAnswer = mListIdoms.get(idx).getmAnswer();
        return idx;
    }

    // IdiomsFragment interface 구현
    public void onBtnNextClicked() {
        if (mSelectedMode) {
            if(mSelectedId == 2)
                 mSelectedId = setSelectedDayQuestion(0);
            else
                mSelectedId = setSelectedDayQuestion(mSelectedId+1);
            Log.i("next", "id = "+mSelectedId);
          }
        else {

            if(mCurrentId ==mQuestionTotalCnt)
                mCurrentId = getQuestion(1); // when this question is Last , go to First
            else
                mCurrentId = getQuestion(mCurrentId+1);
        }
    }
    public void onBtnPreClicked(){
        if(mSelectedMode) {
            if(mSelectedId ==0)
                mSelectedId = setSelectedDayQuestion(2);
            else
                mSelectedId = setSelectedDayQuestion(mSelectedId-1);
            Log.i("pre", "id = "+mSelectedId);
        }
        else {
            if(mCurrentId ==1)
                mCurrentId = getQuestion(mQuestionTotalCnt); //  when this question is First , go to Last
            else
                mCurrentId = getQuestion(mCurrentId-1);


        }
    }

    public void onBtnCheckAnswerClicked(){
        showCorrectAnswer();
    }
    // pos - spinner item pos, id - spinner item id
    public void onItemSelected(int pos, long id){

        if(pos==0)
        {
            reset();
            mCurrentId = getQuestion(mCurrentId);
            //return;
        }
        else {
            int[] days = {pos};
            setSelectedDayIdoms(days);
        }

    }

    private void reset()
    {
        mSelectedMode = false;
        mSelectedId = 0;
        mListIdoms.clear();
    }

}
