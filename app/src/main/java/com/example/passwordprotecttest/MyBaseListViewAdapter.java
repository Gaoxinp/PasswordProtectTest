package com.example.passwordprotecttest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by 高信朋 on 2016/11/13.
 */

public class MyBaseListViewAdapter extends BaseAdapter {
    private int[] icons = new int[]{R.drawable.icon_safe, R.drawable.icon_set};
    private String[] items = new String[]{"安全设置", "设置"};
    private Context context = null;
    private ImageView imageView = null;
    private TextView textView = null;


    public MyBaseListViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public String getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //直接用R.layout.listview_cell里面的LinerLayout来实现这是第一种方法
        LinearLayout linearLayout = null;
        /*
        * 回收机制
        * */
        if (convertView != null) {
            linearLayout = (LinearLayout) convertView;
        } else {
            linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.listview_cell, null);
        }
        imageView = (ImageView) linearLayout.findViewById(R.id.ib_listview_cell);
        textView = (TextView) linearLayout.findViewById(R.id.tv_listview_cell);
        imageView.setImageResource(icons[position]);
        textView.setText(items[position]);


//        //通过LayoutInflater实现是第二种方法
//        LayoutInflater inflater = LayoutInflater.from(context);
//        if(convertView == null){
//            convertView = inflater.inflate(R.layout.listview_cell,null);
//            imageButton = (ImageButton) convertView.findViewById(R.id.ib_listview_cell);
//            textView = (TextView) convertView.findViewById(R.id.tv_listview_cell);
//        }
//        imageButton.setImageResource(icons[position]);
//        textView.setText(items[position]);


        return linearLayout;
    }
}
