package com.example.passwordprotecttest;


import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import static com.example.passwordprotecttest.StaticValues.useWhat;

public class GuideActivity extends ExpandableListActivity {
    private ExpandableListView exList = null;
    private ListView listView = null;
    private Context context = GuideActivity.this;
    private MyBaseExpandableListAdapter adapter;
    private DrawerLayout drawerLayout = null;
    private MyBaseListViewAdapter listAdapter;
    private String[] groups = new String[]{"网址网站", "聊天工具", "游戏账号", "电子邮箱", "支付账号", "软件账号", "其他"};
    private String[] type = new String[]{"station", "chat", "game", "mail", "pay", "software", "other"};
    private int[][] childs_id = new int[groups.length][];
    private String[][] childs_name = new String[groups.length][];
    private String[][] childs_account = new String[groups.length][];
    private Cursor cursor;
    private String type_result;
    /**
     * 为ExpandableListView添加子列表点击事件
     */
    private ExpandableListView.OnChildClickListener onItemClickListener = new ExpandableListView.OnChildClickListener() {
        /**
         * 当子列表被点击时调用
         * @param parent
         * @param v
         * @param groupPosition
         * @param childPosition
         * @param id
         * @return
         */
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            Db db = new Db(context);
            SQLiteDatabase dbRead = db.getReadableDatabase();
            cursor = dbRead.query(type[groupPosition], null, "_id=" + childs_id[groupPosition][childPosition], null, null, null, null);
            System.out.println("" + groupPosition + "->" + childPosition + "->" + childs_id[groupPosition][childPosition]);
            System.out.println("" + childs_id[groupPosition][childPosition]);
            if (cursor.moveToFirst()) {

                String name = cursor.getString(cursor.getColumnIndex("name"));
                String url = cursor.getString(cursor.getColumnIndex("url"));
                String account = cursor.getString(cursor.getColumnIndex("account"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                String comment = cursor.getString(cursor.getColumnIndex("comment"));
                cursor.close();
                dbRead.close();

                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("groupIndex", groupPosition);
                intent.putExtra("childID", childs_id[groupPosition][childPosition]);

                intent.putExtra("type", groupPosition);
                intent.putExtra("name", name);
                intent.putExtra("url", url);
                intent.putExtra("account", account);
                intent.putExtra("password", password);
                intent.putExtra("comment", comment);
                startActivity(intent);
            } else {
                cursor.close();
                dbRead.close();
                Toast.makeText(context, "未找到记录", Toast.LENGTH_SHORT).show();
            }


            return false;
        }


    };
    /**
     * 为ExpandableListView设置子列表被长按的监听事件
     */
    private AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            final long packedPosition = exList.getExpandableListPosition(position);
            final int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
            final int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);
            //长按的是group的时候，childPosition = -1
            if (childPosition != -1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("警告");
                builder.setMessage("您正在试图删除该条数据，确定删除吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Db db = new Db(context);
                        SQLiteDatabase dbWrite = db.getWritableDatabase();
                        int result = dbWrite.delete(type[groupPosition], "_id=?", new String[]{"" + childs_id[groupPosition][childPosition]});
                        dbWrite.close();
                        if (result == 1) {
                            refrash();
                            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                        }
                        exList.expandGroup(groupPosition);
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
            }
            return true;
        }
    };
    /**
     * 设置一组展开时，其他组关闭
     */
    private ExpandableListView.OnGroupExpandListener onGroupExpandListener = new ExpandableListView.OnGroupExpandListener() {
        @Override
        public void onGroupExpand(int groupPosition) {
            int count = adapter.getGroupCount();
            for (int index = 0; index < count; index++) {
                if (index != groupPosition) {
                    //设置 组 不展开
                    exList.collapseGroup(index);
                }
            }
        }
    };
    /**
     * 为侧滑菜单里面的ListView设置item的点击事件
     */
    private AdapterView.OnItemClickListener onClickListener_listView = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent();
            switch (position) {
                case 0:
                    intent.setClass(context, SafePageActivity.class);
                    break;
                case 1:
                    intent.setClass(context, SysSetActivity.class);
                    break;
            }
            drawerLayout.closeDrawer(Gravity.RIGHT);
            startActivity(intent);
        }
    };

    /**
     * 从本地通过SQLite获取子列表的内容
     */
    public void getChildsContent() {
        Db db = new Db(context);
        SQLiteDatabase dbRead = db.getReadableDatabase();
        for (int i = 0; i < type.length; i++) {
            cursor = dbRead.query(type[i], null, null, null, null, null, null);
            childs_id[i] = new int[cursor.getCount()];
            childs_name[i] = new String[cursor.getCount()];
            childs_account[i] = new String[cursor.getCount()];
            if (cursor.moveToFirst()) {

                childs_id[i][0] = cursor.getInt(cursor.getColumnIndex("_id"));
                childs_name[i][0] = cursor.getString(cursor.getColumnIndex("name"));
                childs_account[i][0] = cursor.getString(cursor.getColumnIndex("account"));
                int index = 1;
                while (cursor.moveToNext()) {
                    childs_id[i][index] = cursor.getInt(cursor.getColumnIndex("_id"));
                    childs_name[i][index] = cursor.getString(cursor.getColumnIndex("name"));
                    childs_account[i][index] = cursor.getString(cursor.getColumnIndex("account"));
                    index++;
                }
            }

        }
        dbRead.close();
    }


    /**
     * 设置列表里面的内容
     */
    public void setListData() {
        adapter = new MyBaseExpandableListAdapter(context, groups, childs_name, childs_account);
        setListAdapter(adapter);

    }

    /**
     * 刷新列表内容
     */
    public void refrash() {
        getChildsContent();
//        adapter.notifyDataSetChanged();
        setListData();
        for (int i = 0; i < type.length; i++) {
            if (type[i].equals(type_result)) {
                Toast.makeText(context, "" + type[i].equals(type_result), Toast.LENGTH_SHORT).show();
                exList.expandGroup(i);
            }
        }
    }

    /**
     * 用于实现当从其他activity返回到该activity时，刷新该activity的列表内容
     */
    @Override
    protected void onResume() {
        super.onResume();
        refrash();
    }


    /**
     * 跳转到添加记录页面
     *
     * @param view
     */
    public void recordNewItem(View view) {

        Intent intent = new Intent(context, RecordNewItemActivity.class);
        startActivityForResult(intent, 0);////第二个参数可以是大于等于0的任意值

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (resultCode) {
            case RESULT_OK:

                Bundle bundle = data.getExtras();
                type_result = bundle.getString("type");
                break;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        exList = getExpandableListView();
        listView = (ListView) findViewById(R.id.listView);
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_guide);
        getChildsContent();
        setListData();
        setListView();
        //添加子列表点击事件
        exList.setOnChildClickListener(onItemClickListener);
        //添加子列表长按事件
        exList.setOnItemLongClickListener(onItemLongClickListener);
        //添加 组 展开事件
        exList.setOnGroupExpandListener(onGroupExpandListener);
        listView.setOnItemClickListener(onClickListener_listView);

        //监听锁屏状态 和 退出程序信号
        ScreenListener listener = new ScreenListener(context);
        listener.begin(new ScreenListener.ScreenStateListener() {
            @Override
            public void onScreenOn() {

            }

            @Override
            public void onScreenoff() {

            }

            @Override
            public void onUserPrisent() {
                if (useWhat != 0) {

                    Intent intent = new Intent();
                    switch (useWhat) {

                        case 1:
                            intent.setClass(context, DLockActivity.class);
                            break;
                        case 2:
                            intent.setClass(context, PLockActivity.class);
                            break;
                        case 3:
                            intent.setClass(context, DIYLockActivity.class);
                            break;
                    }
                    intent.putExtra("flag", "openScreen");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void exitAPP() {
                finish();
            }
        });

    }

    /**
     * 设置侧滑页面的ListView
     */
    private void setListView() {
        listAdapter = new MyBaseListViewAdapter(context);
        listView.setAdapter(listAdapter);
    }


}
