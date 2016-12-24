package jewoo.idioms;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.design.widget.TabLayout;

import android.support.v4.view.ViewPager;
import android.app.FragmentManager;
//import android.support.v4.app.FragmentManager;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.List;


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
    private ArrayList<PatternsData> mListPatterns;
    private boolean mSelectedMode = false;
    private int mSelectedId =0;

    // level
    private int mCurrentLevelId =1; // default 1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();
        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Idioms");
        spec.setContent(R.id.tabIdioms);
        spec.setIndicator("Idioms");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Patterns");
        spec.setContent(R.id.tabPatterns);
        spec.setIndicator("Patterns");
        host.addTab(spec);

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

        mQuestionTotalCnt = getTotalCnt();

        setPatternsData();
    }
    public void setPatternsData(){
        mListPatterns = new ArrayList<PatternsData>();

        mListPatterns.clear();

        Cursor cursor = mDB.rawQuery("select pa.pattern, pa.meaning, ppa.meaning, ppa.english, ppa.hint from patterns pa , patterns_practice ppa WHERE ppa._id = 1",null);

        int iId;
        String strPattern="";
        String strMeaning="";
        List<String> listPractices = new ArrayList<String>();
        List<String> listAnswer=new ArrayList<String>();
        List<String> listHint=new ArrayList<String>();

        if ( cursor.moveToNext()){
            strPattern = cursor.getString(0);
            strMeaning= cursor.getString(1);
            listPractices.add(cursor.getString(2));
            listAnswer.add(cursor.getString(3));
            listHint.add(cursor.getString(4));
        }

        while(cursor.moveToNext()){

            listPractices.add(cursor.getString(2));
            listAnswer.add(cursor.getString(3));
            listHint.add(cursor.getString(4));

        }
        if (! strPattern.isEmpty()) {
            PatternsData patterns = new PatternsData(strPattern,strMeaning, listPractices, listAnswer, listHint);
            mListPatterns.add(patterns);
            setPPData(patterns);
        }
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
        //Cursor cursor = mDB.rawQuery("select english, meaning from idioms where _id="+_id+" and level = "+mCurrentLevelId+";",null);
        // tt : total table, pt : part table
        Cursor cursor = mDB.rawQuery("select tt.meaning,tt.english "+
                                    "from idioms tt, (select a._id, count(*) idx from idioms a, idioms b "+
                                                    "where a._id >=b._id and b.level = "+mCurrentLevelId+
                                                    " group by a._id ) pt " +
                                    "where tt._id= pt._id and pt.idx ="+_id+";",null);
        while(cursor.moveToNext()){
            setData(cursor.getString(0));
            mCorrectAnswer = cursor.getString(1);
        }
        return _id;
    }

    private int getTotalCnt(){
        Cursor cursor = mDB.rawQuery("SELECT count(_id) FROM idioms where level ="+mCurrentLevelId+" ;",null);
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
    private void setPPData(PatternsData aData){
        //mQuestionTextView.setText(strQuestion);
        FragmentManager fmManager = getFragmentManager();
        PatternsFragment fmPatternss=(PatternsFragment)fmManager.findFragmentById(R.id.fragment2);

        fmPatternss.setPatternsData(aData);
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

            Cursor cursor = mDB.rawQuery("select _id, meaning, english from idioms where lesson="+days[i]+" and level = "+mCurrentLevelId+";",null);

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
            if(mSelectedId == 2) // 3 question a day ,
                 mSelectedId = setSelectedDayQuestion(0);
            else
                mSelectedId = setSelectedDayQuestion(mSelectedId+1);
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

    @Override
    public void onRdBtnChanged(int checkedId) {
        mCurrentLevelId = checkedId;
        mQuestionTotalCnt=getTotalCnt();
        //mCurrentId=0;
        mCurrentId=getQuestion(1);
    }

    private void reset()
    {
        mSelectedMode = false;
        mSelectedId = 0;
        mListIdoms.clear();
    }

}
