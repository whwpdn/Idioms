package jewoo.idioms;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;


/**
 * Created by jewoo on 2016. 7. 2..
 */
public class IdiomsFragment extends Fragment implements View.OnClickListener , AdapterView.OnItemSelectedListener {
    private View mView;
    private TextView mQuestionTextView;
    private TextView mAnswerTextView;
    private RadioGroup rdGroup;
    private Spinner mSpinnerDays;

    private boolean mIsShow = true;

    //
    private boolean mSelectedMode = false;
    private int mSelectedId =0;
    private int mCurrentId=1;
    private String mCorrectAnswer="";
    private int mQuestionTotalCnt =0;
    private ArrayList<IdiomsData> mListIdoms;
    private int mCurrentLevelId =1; // default 1




    // button event listener
//    public interface OnListener{
//        //public void onBtnNextClicked();
//        //public void onBtnPreClicked();
//        //public void onBtnCheckAnswerClicked();
//        //public void onItemSelected(int position, long id);
//        //public void onRdBtnChanged(int checkedId);
//    }

    // reference activity
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
//        try{
//            mListener = (OnListener) activity;
//        } catch(ClassCastException e){
//            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
//        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View view = inflater.inflate( R.layout.idioms_fragment, container, false );

        Button btnNext = (Button)view.findViewById(R.id.btnNext);
        Button btnAnswer = (Button)view.findViewById(R.id.btnCheckAnswer);
        Button btnPre = (Button)view.findViewById(R.id.btnPre);
        mQuestionTextView = (TextView)view.findViewById(R.id.tvQuestion);
        mAnswerTextView = (TextView)view.findViewById(R.id.tvAnswer);
        rdGroup = (RadioGroup)view.findViewById(R.id.rdLevelGroup);
        //RadioButton rdbtn= (RadioButton)view.findViewById(R.id.rdDreamer);
        mSpinnerDays = (Spinner) view.findViewById(R.id.spinner);

        // setting spinner value from R.array.day ( 1~20)
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.day, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerDays.setAdapter(adapter);

        // button click listener
        btnNext.setOnClickListener(this);
        btnAnswer.setOnClickListener(this);
        btnPre.setOnClickListener(this);

        mListIdoms = new ArrayList<IdiomsData>();

        rdGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                switch (checkedId){
                    case R.id.rdLevel1:
                        mSpinnerDays.setSelection(0);
                        setAnswerBlank();
                        onRdBtnChanged(1);
                        //mListener.onRdBtnChanged(1);
                        break;
                    case R.id.rdLevel2:
                        mSpinnerDays.setSelection(0);
                        setAnswerBlank();
                        onRdBtnChanged(2);
                        //mListener.onRdBtnChanged(2);
                        break;
//                    case R.id.rdImaginerB:
//                        mSpinnerDays.setSelection(0);
//                        setAnswerBlank();
//                        onRdBtnChanged(3);
//                        break;
//                    case R.id.rdImaginerC:
//                        mSpinnerDays.setSelection(0);
//                        setAnswerBlank();
//                       onRdBtnChanged(4);
//                        break;
                }

            }
        });
        mSpinnerDays.setOnItemSelectedListener(this);
        mView=view;
        mQuestionTotalCnt = getTotalCnt();
        return view;

    }

    private void setQuestionText(String text){

        mQuestionTextView.setText(text);
    }

    private void setAnswerText(String text) {
        mAnswerTextView.setText(text);
    }


    @Override
    public void onClick(View view)
    {
        switch(view.getId()) {

            case R.id.btnNext:
            {
                setAnswerBlank();

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
                break;
            case R.id.btnPre:
            {
                setAnswerBlank();
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
                break;
            case R.id.btnCheckAnswer:
            {
                if (mIsShow) break;
                mIsShow = true;
                setAnswerText(mCorrectAnswer);
            }
                break;
            default:
                break;
        }
    }


    private void setAnswerBlank(){
        mIsShow = false;
        mAnswerTextView.setText(" ");
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        setAnswerBlank();
        onItemSelected(position, id);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private int setSelectedDayQuestion(int idx){
        setQuestionText(mListIdoms.get(idx).getmQuestion());
        mCorrectAnswer = mListIdoms.get(idx).getmAnswer();
        return idx;
    }

    private int getQuestion(int _id){
        //Cursor cursor = mDB.rawQuery("select english, meaning from idioms where _id="+_id+" and level = "+mCurrentLevelId+";",null);
        // tt : total table, pt : part table
        SQLiteDatabase db = IdiomsSqliteOpenHelper.getInstance(getActivity().getApplicationContext(), null);

        Cursor cursor = db.rawQuery("select tt.meaning,tt.english "+
                "from idioms tt, (select a._id, count(*) idx from idioms a, idioms b "+
                "where a._id >=b._id and b.level = "+mCurrentLevelId+
                " group by a._id ) pt " +
                "where tt._id= pt._id and pt.idx ="+_id+";",null);

        while(cursor.moveToNext()){
            setQuestionText(cursor.getString(0));
            mCorrectAnswer = cursor.getString(1);
        }
        return _id;
    }

    private void onRdBtnChanged(int checkedId) {
        mCurrentLevelId = checkedId;
        mQuestionTotalCnt=getTotalCnt();
        //mCurrentId=0;
        mCurrentId=getQuestion(1);
    }

    private int getTotalCnt(){
        SQLiteDatabase db = IdiomsSqliteOpenHelper.getInstance(getActivity().getApplicationContext(), null);
        Cursor cursor = db.rawQuery("SELECT count(_id) FROM idioms where level ="+mCurrentLevelId+" ;",null);
        int total =0;
        while( cursor.moveToNext()){
            total = cursor.getInt(0);
        }
        return total;
    }

//    public void onBtnPreClicked(){
//        if(mSelectedMode) {
//            if(mSelectedId ==0)
//                mSelectedId = setSelectedDayQuestion(2);
//            else
//                mSelectedId = setSelectedDayQuestion(mSelectedId-1);
//        }
//        else {
//            if(mCurrentId ==1)
//                mCurrentId = getQuestion(mQuestionTotalCnt); //  when this question is First , go to Last
//            else
//                mCurrentId = getQuestion(mCurrentId-1);
//
//        }
//    }
    // pos - spinner item pos, id - spinner item id
    private void onItemSelected(int pos, long id){

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
    // when lesson selected, add Idoms.
    private void setSelectedDayIdoms(int days[]){
        mListIdoms.clear();
        mSelectedMode = true;
        mSelectedId = 0;
        SQLiteDatabase db = IdiomsSqliteOpenHelper.getInstance(getActivity().getApplicationContext(), null);
        for(int i=0; i<days.length ; i++){

            Cursor cursor = db.rawQuery("select _id, meaning, english from idioms where lesson="+days[i]+" and level = "+mCurrentLevelId+";",null);

            while(cursor.moveToNext()){
                IdiomsData idoms = new IdiomsData(cursor.getInt(0),cursor.getString(1),cursor.getString(2));
                mListIdoms.add(idoms);
            }
        }

        setQuestionText(mListIdoms.get(mSelectedId).getmQuestion());
        mCorrectAnswer = mListIdoms.get(mSelectedId).getmAnswer();
    }

}
