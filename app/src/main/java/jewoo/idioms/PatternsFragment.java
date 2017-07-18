package jewoo.idioms;

//import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jewoo on 2016. 7. 31..
 */
public class PatternsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    public ExpandableListView expandableListView; // ExpandableListView 변수 선언
    public BaseExpandableAdapter mCustomExpListViewAdapter; // 위 ExpandableListView를 받을 CustomAdapter(2번 class에 해당)를 선언

    private ArrayList<String> mGroupList = null;
    //private ArrayList<ArrayList<String>> mChildList = null;
    private ArrayList<String> mChildList = null;
    //private ArrayList<String> mChildListContent = null;
    private String mChildListContent = null;
    private TextView mtvPattern = null;
    private TextView mtvMeaning = null;
    private Spinner mSpinnerDays;
    Context c;
    View v ;
    //OnListener mListener;

    private ArrayList<PatternsData> mListPatterns;


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
        v= inflater.inflate( R.layout.patterns_fragment, container, false );

        mtvPattern = (TextView)v.findViewById(R.id.tvPattern);

        mtvMeaning = (TextView)v.findViewById(R.id.tvMeaning);
        mGroupList = new ArrayList<String>();
        //mChildList = new ArrayList<ArrayList<String>>();
        mChildList = new ArrayList<String>();


        mSpinnerDays = (Spinner) v.findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.patterns_day, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerDays.setAdapter(adapter);
        mSpinnerDays.setOnItemSelectedListener(this);
        //mChildListContent = new ArrayList<String>();
        mListPatterns = new ArrayList<PatternsData>();

        setPatternsData(0);
        return v;

    }


    public void setPatternsData(PatternsData aPattern){

        mChildList.clear();
        mGroupList.clear();
        if(mCustomExpListViewAdapter !=null)
            mCustomExpListViewAdapter.notifyDataSetChanged();

        mtvPattern.setText(aPattern.getmPattern());
        mtvMeaning.setText(aPattern.getmMeaning());
        List<String> listPractices = aPattern.getmQuestion();
        List<String> listAnswers = aPattern.getmAnswer();

        for(int i =0 ; i <listPractices.size() ; i++) {
            mGroupList.add(listPractices.get(i));
            mChildListContent = listAnswers.get(i);
            mChildList.add(mChildListContent);

        }


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        super.onActivityCreated(savedInstanceState);
        //mGroupList = new ArrayList<String>();

        expandableListView = (ExpandableListView)v.findViewById(R.id.elistPatterns);

        //here setting all the values to Parent and child classes
        //c=getActivity();


        mCustomExpListViewAdapter = new BaseExpandableAdapter(getActivity(), mGroupList, mChildList);

        expandableListView.setAdapter(mCustomExpListViewAdapter);


        // 그룹 클릭 했을 경우 이벤트
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {

                return false;
            }
        });

        // 차일드 클릭 했을 경우 이벤트
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                return false;
            }
        });

        // 그룹이 닫힐 경우 이벤트
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        // 그룹이 열릴 경우 이벤트
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        setPatternsData(position);
        //mListener.onPatternDayItemSelected(position,id);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // button event listener
//    public interface OnListener{
//
//        public void onPatternDayItemSelected(int position, long id);
//
//    }

    private class BaseExpandableAdapter extends BaseExpandableListAdapter{

        private ArrayList<String> groupList = null;
        private ArrayList<String> childList = null;
        private LayoutInflater inflater = null;
        private ViewHolder viewHolder = null;


        public BaseExpandableAdapter(Context c, ArrayList<String> groupList,
                                     ArrayList<String> childList){
            super();
            this.inflater = LayoutInflater.from(c);
            this.groupList = groupList;
            this.childList = childList;
        }

        // 그룹 포지션을 반환한다.
        @Override
        public String getGroup(int groupPosition) {
            return groupList.get(groupPosition);
        }

        // 그룹 사이즈를 반환한다.
        @Override
        public int getGroupCount() {
            return groupList.size();
        }

        // 그룹 ID를 반환한다.
        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        // 그룹뷰 각각의 ROW
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {

            View v = convertView;

            if(v == null){
                viewHolder = new ViewHolder();
                v = inflater.inflate(R.layout.list_group, parent , false);
                viewHolder.tv_groupName = (TextView) v.findViewById(R.id.tvparent);
                v.setTag(viewHolder);

            }else{
                viewHolder = (ViewHolder)v.getTag();
            }

//            // 그룹을 펼칠때와 닫을때 아이콘을 변경해 준다.
//            if(isExpanded){
//                viewHolder.iv_image.setBackgroundColor(Color.GREEN);
//            }else{
//                viewHolder.iv_image.setBackgroundColor(Color.WHITE);
//            }
            //viewHolder.tv_groupName = (TextView) v.findViewById(R.id.tvparent);
            viewHolder.tv_groupName.setText(getGroup(groupPosition));
            //viewHolder.tv_groupName.setText("dfdf");
            return v;
        }

        // 차일드뷰를 반환한다.
        @Override
        public String getChild(int groupPosition, int childPosition) {
            //return childList.get(groupPosition).get(childPosition);
            return childList.get(groupPosition);
        }

        // 차일드뷰 사이즈를 반환한다.
        @Override
        public int getChildrenCount(int groupPosition) {
            //return childList.get(groupPosition).size();
            return 1;
        }

        // 차일드뷰 ID를 반환한다.
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        // 차일드뷰 각각의 ROW
        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            View v = convertView;

            if(v == null){
                viewHolder = new ViewHolder();
                v = inflater.inflate(R.layout.list_row,parent,false);
                viewHolder.tv_childName = (TextView) v.findViewById(R.id.tvchild);
                v.setTag(viewHolder);

            }else{
                viewHolder = (ViewHolder)v.getTag();
            }

            viewHolder.tv_childName.setText(getChild(groupPosition, childPosition));
            //viewHolder.tv_childName.setText(getChild(groupPosition, childPosition));
            return v;
        }

        @Override
        public boolean hasStableIds() { return true; }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) { return true; }

        class ViewHolder {

            public TextView tv_groupName;
            public TextView tv_childName;
        }

    }

    public void setPatternsData(int day){
        mListPatterns.clear();

        SQLiteDatabase db = IdiomsSqliteOpenHelper.getInstance(getActivity().getApplicationContext(), null);
        //Cursor cursor = mDB.rawQuery("select pa.pattern, pa.meaning, ppa.meaning, ppa.english, ppa.hint from patterns pa , patterns_practice ppa WHERE ppa._id = 1",null);
        Cursor cursor = db.rawQuery("select ppa.meaning, ppa.english, ppa.hint from patterns_practice ppa WHERE ppa._id = "+(++day),null);


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

        cursor = db.rawQuery("select pa.pattern, pa.meaning from patterns pa WHERE pa._id = "+(day),null);
        if( cursor.moveToNext()){
            strPattern = cursor.getString(0);
            strMeaning = cursor.getString(1);
        }
        if (! strPattern.isEmpty()) {
            PatternsData patterns = new PatternsData(strPattern,strMeaning, listPractices, listAnswer, listHint);
            mListPatterns.add(patterns);
            setPatternsData(patterns);
        }
        else{
            // error
        }
    }

}
