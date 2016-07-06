package com.example.jewoo.idoms;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by jewoo on 2016. 7. 2..
 */
public class IdomsFragment extends Fragment implements View.OnClickListener{
    private TextView mQuestionTextView;
    private Button mNextButton;
    OnButtonListener mListener;

    // button event listener
    public interface OnButtonListener{
        public void onButtonClicked();
    }

    // reference activity
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            mListener = (OnButtonListener) activity;
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

        mNextButton = (Button)view.findViewById(R.id.button);
        mQuestionTextView = (TextView)view.findViewById(R.id.textView);
        // button click listener
        mNextButton.setOnClickListener(this);

        return view;
    }
    public void setQuestionText(String text){
        mQuestionTextView.setText(text);
    }
    public void setNextQuestion()
    {

    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId()) {

            case R.id.button:
                mListener.onButtonClicked(); // call back
                break;
            default:
                break;
        }
    }
}
