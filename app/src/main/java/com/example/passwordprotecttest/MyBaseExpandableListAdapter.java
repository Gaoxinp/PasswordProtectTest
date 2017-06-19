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
    private String[] groups;
    private String[][] childs_name;
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

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
