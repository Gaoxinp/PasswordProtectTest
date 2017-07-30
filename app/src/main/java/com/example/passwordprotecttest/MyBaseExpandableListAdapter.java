package com.example.passwordprotecttest;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**
 * Created by 高信朋 on 2016/11/9.
 */

public class MyBaseExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private LayoutInflater inflater;
//    第一级分级（组列表）的名称
    private String[] groups;
//    第二级分级（子列表）中item的名字
    private String[][] childs_name;
//    第二级分级（子列表）中item的账号
    private String[][] childs_account;


    public MyBaseExpandableListAdapter(Context context, String[] groups, String[][] childs_name, String[][] childs_account) {
        this.context = context;
        inflater = ((Activity) context).getLayoutInflater();
        this.groups = groups;
        this.childs_name = childs_name;
        this.childs_account = childs_account;
    }

    @Override
    public int getGroupCount() {
        return groups.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int i;
        if (childs_name[groupPosition] == null) {
            i = 0;

        } else {
            i = childs_name[groupPosition].length;
        }
        return i;
    }

    @Override
    public String getGroup(int groupPosition) {
        return groups[groupPosition];
    }

    @Override
    public String getChild(int groupPosition, int childPosition) {
        return childs_name[groupPosition][childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     *在调用invalidateView时，ListView会刷新显示的内容，如果内容的id是有效的，系统会根据id来确定当前该县是哪条内容
     * 也就是firstVisibleChild的位置。id是否有效通过hasStableIds方法来判断，
     * 也就是说，该方法是来判断item的id是否稳定，如果有自己的id也就是true，那就是稳定，则根据item位置来确定id
     * @return
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TextView type_tv;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.group, parent, false);
        }
        type_tv = (TextView) convertView.findViewById(R.id.textGroup);
        type_tv.setText(groups[groupPosition]);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TextView name_tv;
        TextView account_tv;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child, parent, false);
        }
        name_tv = (TextView) convertView.findViewById(R.id.textContentName);
        account_tv = (TextView) convertView.findViewById(R.id.textContentAccount);
        if ("".equals(childs_name[groupPosition][childPosition])) {
            name_tv.setText("无");
        } else {
            name_tv.setText(childs_name[groupPosition][childPosition]);
        }


        if ("".equals(childs_account[groupPosition][childPosition])) {

            account_tv.setText("无");
        } else {

            account_tv.setText(childs_account[groupPosition][childPosition]);
        }


        return convertView;
    }

    /**
     * 子列表的item可否点击
     * @return true：可以点击    false：不能点击
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
