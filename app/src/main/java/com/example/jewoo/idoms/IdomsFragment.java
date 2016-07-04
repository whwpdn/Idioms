package com.example.jewoo.idoms;

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
public class IdomsFragment extends Fragment {
    TextView resultTextView;
    Button button;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View view = inflater.inflate( R.layout.idoms_fragment, container, false );

         button = (Button)view.findViewById(R.id.button);
        resultTextView = (TextView)view.findViewById(R.id.textView);
        //버튼 이벤트 추가
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultTextView.setText("dfdfee134");
            }
        });
        return view;
    }
    public void setText(String text){
        TextView textView = (TextView) getView().findViewById(R.id.textView);
        textView.setText(text);
    }

}
