package jewoo.idioms;

//import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;




/**
 * Created by jewoo on 2017. 2. 28..
 */
public class ListeningFragment extends Fragment  {

    //public ExpandableListView expandableListView; // ExpandableListView 변수 선언
    //public BaseExpandableAdapter mCustomExpListViewAdapter; // 위 ExpandableListView를 받을 CustomAdapter(2번 class에 해당)를 선언

    //private ArrayList<String> mGroupList = null;
    //private ArrayList<ArrayList<String>> mChildList = null;
    //private ArrayList<String> mChildList = null;
    //private ArrayList<String> mChildListContent = null;
    //private String mChildListContent = null;
    //private TextView mtvPattern = null;
    //private TextView mtvMeaning = null;
    //private Spinner mSpinnerDays;
    private ListView mListeningList;
    Context c;
    View v ;
    private ArrayAdapter<String> TestAdapter;
//    OnListener mListener;
//
//    @Override
//    public void onAttach(Activity activity){
//        super.onAttach(activity);
//        try{
//            mListener = (OnListener) activity;
//        } catch(ClassCastException e){
//            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
//        }
//
//    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        v= inflater.inflate( R.layout.listening_fragment, container, false );

        //String[] TEMP_LIST_ITEM={"test1", "test2","test2"};
        //SimpleAdapter aaa = new SimpleAdapter(getActivity(),TEMP_LIST_ITEM,R.layout.list_group);

//        mtvPattern = (TextView)v.findViewById(R.id.tvPattern);
//
//        mtvMeaning = (TextView)v.findViewById(R.id.tvMeaning);
//        mGroupList = new ArrayList<String>();
//        //mChildList = new ArrayList<ArrayList<String>>();
//        mChildList = new ArrayList<String>();
//
//
//        mSpinnerDays = (Spinner) v.findViewById(R.id.spinner2);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.patterns_day, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mSpinnerDays.setAdapter(adapter);
//        mSpinnerDays.setOnItemSelectedListener(this);
        //mChildListContent = new ArrayList<String>();
        ArrayList<String> ttt = new ArrayList<String>();

        TestAdapter= new ArrayAdapter<String>(getActivity(), R.layout.list_group,R.id.tvparent,ttt);

        //String path = Environment.getExternalStorageDirectory().toString()+"/Pictures";
        //String path = getActivity().getApplicationInfo().dataDir;
        String path = getActivity().getExternalFilesDir(null).getAbsolutePath()+"/samples/";
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();Log.d("Files", "Size: "+ files.length);
        for (int i = 0; i < files.length; i++)
        {
            ttt.add(files[i].getName());
        }
        mListeningList = (ListView)v.findViewById(R.id.listeningList);



        mListeningList.setAdapter(TestAdapter);
        //TestAdapter.add("Test1");
        //TestAdapter.add("Test2");
        //TestAdapter.add("Test3");

        return v;

    }
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        super.onActivityCreated(savedInstanceState);
        //mGroupList = new ArrayList<String>();



        //here setting all the values to Parent and child classes


    }


//    // button event listener
//    public interface OnListener{
//
//        public void onPatternDayItemSelected(int position, long id);
//
//    }


}
