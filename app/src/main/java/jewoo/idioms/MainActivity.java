package jewoo.idioms;


import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements IdiomsFragment.OnListener, PatternsFragment.OnListener {

    // sqlite database member
    private IdiomsSqliteOpenHelper mDBHelper;
    private String mDBName = "StudyDatabase.db";
    private SQLiteDatabase mDB;
    int miDBVersion =1; // database version
    private static final String TAG_DB = "SQLITE";

    //
    private static final String SAMPLE_PATH = "Samples";

    //idioms question member
    private int mCurrentId=1;
    private String mCorrectAnswer="";
    private int mQuestionTotalCnt =0;
    private ArrayList<IdiomsData> mListIdoms;

    // patterns
    private ArrayList<PatternsData> mListPatterns;
    // Selected Day
    private boolean mSelectedMode = false;
    private int mSelectedId =0;

    private SharedPreferences spf;
    // level
    private int mCurrentLevelId =1; // default 1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        checkFirstRun();
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

        //tab 3
        spec = host.newTabSpec("Listening");
        spec.setContent(R.id.tabListening);
        spec.setIndicator("Listening");
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
        mListPatterns = new ArrayList<PatternsData>();
        setPatternsData(0);
        initListeningData();
    }
    public void setPatternsData(int day){
        mListPatterns.clear();

        //Cursor cursor = mDB.rawQuery("select pa.pattern, pa.meaning, ppa.meaning, ppa.english, ppa.hint from patterns pa , patterns_practice ppa WHERE ppa._id = 1",null);
        Cursor cursor = mDB.rawQuery("select ppa.meaning, ppa.english, ppa.hint from patterns_practice ppa WHERE ppa._id = "+(++day),null);

        int iId;
        String strPattern="";
        String strMeaning="";
        List<String> listPractices = new ArrayList<String>();
        List<String> listAnswer=new ArrayList<String>();
        List<String> listHint=new ArrayList<String>();

        while(cursor.moveToNext()){

            listPractices.add(cursor.getString(0));
            listAnswer.add(cursor.getString(1));
            listHint.add(cursor.getString(2));

        }

        cursor = mDB.rawQuery("select pa.pattern, pa.meaning from patterns pa WHERE pa._id = "+(day),null);
        if( cursor.moveToNext()){
            strPattern = cursor.getString(0);
            strMeaning = cursor.getString(1);
        }
        if (! strPattern.isEmpty()) {
            PatternsData patterns = new PatternsData(strPattern,strMeaning, listPractices, listAnswer, listHint);
            mListPatterns.add(patterns);
            setPPData(patterns);
        }
        else{
            // error
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

    private void initListeningData(){
        FragmentManager fmManager = getFragmentManager();
        ListeningFragment fmListening=(ListeningFragment)fmManager.findFragmentById(R.id.fragment3);

        fmListening.initFileList();
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

    // Patterns Fragment Interface
    public void onPatternDayItemSelected(int pos, long id){

        setPatternsData(pos);
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

    // copy sample files
    private void CopyAssets() {
        AssetManager assetManager = getResources().getAssets();//getAssets();

        String[] files = null;
        String mkdir = null ;
        try {
            files = assetManager.list(SAMPLE_PATH);

            //이미지만 가져올때 files = assetManager.list("image");

        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        for(int i=0; i<files.length; i++) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(SAMPLE_PATH+"/"+files[i]);
                //in = assetManager.open(files[i]);

                //폴더생성
                String str = Environment.getExternalStorageState();
                if ( str.equals(Environment.MEDIA_MOUNTED)) {
                    mkdir = getExternalFilesDir(null).getAbsolutePath()+"/"+SAMPLE_PATH+"/";
                    //mkdir = "/sdcard/elecgal/templet/" ;
                } else {
                    Environment.getExternalStorageDirectory();
                }
                File mpath = new File(mkdir);
                if(! mpath.isDirectory()) {
                    mpath.mkdirs();
                }
                //

                //out = new FileOutputStream("/sdcard/elecgal/templet/" + files[i]);
                out = new FileOutputStream(mkdir + files[i]);
                copyFile(in, out);
            } catch(Exception e) {
                Log.e("tag", e.getMessage());
            }
            finally{
                if(in != null){
                    try{
                        in.close();
                        in = null;
                    }
                    catch(IOException e){

                    }
                }
                if(out != null){
                    try{
                        out.flush();
                        out.close();
                        out = null;
                    }catch(IOException e){

                    }
                }
            }

        }
    }

    // copy file method
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }

    }

    //
    private void checkFirstRun() {

        final String PREFS_NAME = "installed";
        final String PREF_VERSION_CODE_KEY = "4";
        final int DOESNT_EXIST = -1;


        // Get current version code
        int currentVersionCode = 0;
        try {
            currentVersionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            // handle exception
            e.printStackTrace();
            return;
        }

        // Get saved version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {

            // This is just a normal run
            return;

        } else if (savedVersionCode == DOESNT_EXIST) {

            // TODO This is a new install (or the user cleared the shared preferences)
            CopyAssets();

        } else if (currentVersionCode > savedVersionCode) {

            // TODO This is an upgrade
            CopyAssets();

        }

        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).commit();

    }
}
