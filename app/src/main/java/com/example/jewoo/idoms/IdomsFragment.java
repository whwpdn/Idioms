package com.example.jewoo.idoms;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by jewoo on 2016. 7. 2..
 */
public class IdomsFragment extends Fragment implements View.OnClickListener , AdapterView.OnItemSelectedListener {
    private View mView;
    private TextView mQuestionTextView;
    private TextView mAnswerTextView;
   // private Button mNextButton;
    OnListener mListener;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mListener.onItemSelected(position, id);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // button event listener
    public interface OnListener{
        public void onBtnNextClicked();
        public void onBtnCheckAnswerClicked();
        public void onItemSelected(int position, long id);
    }

    // reference activity
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            mListener = (OnListener) activity;
        } catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View view = inflater.inflate( R.layout.idoms_fragment, container, false );

        Button btnNext = (Button)view.findViewById(R.id.btnNext);
        Button btnAnswer = (Button)view.findViewById(R.id.btnCheckAnswer);
        mQuestionTextView = (TextView)view.findViewById(R.id.textView);
        mAnswerTextView = (TextView)view.findViewById(R.id.textView2);

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        // button click listener
        btnNext.setOnClickListener(this);
        btnAnswer.setOnClickListener(this);
        mView=view;
        return view;
    }

    public void setQuestionText(String text){

        mQuestionTextView.setText(text);
    }
    public void setAnswerText(String text) {
        ((TextView)mView.findViewById(R.id.textView2)).setText(text);
        //mAnswerTextView.setText(text);

    }


    @Override
    public void onClick(View view)
    {
        switch(view.getId()) {

            case R.id.btnNext:
                mListener.onBtnNextClicked(); // call back
                break;
            case R.id.btnCheckAnswer:
                mListener.onBtnCheckAnswerClicked();
                break;
            default:
                break;
        }
    }
}
