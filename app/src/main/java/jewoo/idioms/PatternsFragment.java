package jewoo.idioms;

//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by jewoo on 2016. 7. 31..
 */
public class PatternsFragment extends Fragment {

    public ExpandableListView expandableListView; // ExpandableListView 변수 선언
    public BaseExpandableAdapter mCustomExpListViewAdapter; // 위 ExpandableListView를 받을 CustomAdapter(2번 class에 해당)를 선언

    private ArrayList<String> mGroupList = null;
    private ArrayList<ArrayList<String>> mChildList = null;
    private ArrayList<String> mChildListContent = null;
    Context c;
    View v ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        v= inflater.inflate( R.layout.patterns_fragment, container, false );

        return v;

    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        super.onActivityCreated(savedInstanceState);
        mGroupList = new ArrayList<String>();
        mChildList = new ArrayList<ArrayList<String>>();
        mChildListContent = new ArrayList<String>();

        expandableListView = (ExpandableListView)v.findViewById(R.id.elistPatterns);
        mGroupList.add("가위");
        mGroupList.add("바위");
        mGroupList.add("보");

        mChildListContent.add("1");
        mChildListContent.add("2");
        mChildListContent.add("3");

        mChildList.add(mChildListContent);
        mChildList.add(mChildListContent);
        mChildList.add(mChildListContent);

        //here setting all the values to Parent and child classes
        c=getActivity();


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

    private class BaseExpandableAdapter extends BaseExpandableListAdapter{

        private ArrayList<String> groupList = null;
        private ArrayList<ArrayList<String>> childList = null;
        private LayoutInflater inflater = null;
        private ViewHolder viewHolder = null;


        public BaseExpandableAdapter(Context c, ArrayList<String> groupList,
                                     ArrayList<ArrayList<String>> childList){
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
                v = inflater.inflate(R.layout.list_row, parent , false);
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
            return childList.get(groupPosition).get(childPosition);
        }

        // 차일드뷰 사이즈를 반환한다.
        @Override
        public int getChildrenCount(int groupPosition) {
            return childList.get(groupPosition).size();
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
}