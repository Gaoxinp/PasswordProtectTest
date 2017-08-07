package com.example.passwordprotecttest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 高信朋 on 2017/8/5.
 */

public class MyRecycleViewAdapter extends RecyclerView.Adapter<MyRecycleViewAdapter.MyViewHolder> {
    private Context context;
    private String[] itemText;
    private int[] itemImg;

    private OnItemClickListener mOnItemClickListener;

    /**
     * 构造方法
     *
     * @param context
     * @param itemImg
     * @param itemText
     */
    public MyRecycleViewAdapter(Context context, int[] itemImg, String[] itemText) {
        this.context = context;
        this.itemImg = itemImg;
        this.itemText = itemText;
    }

    /**
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        /**
         * 第三步，得到item的布局，然后为其设置OnClickListener监听器
         */
        final View itemRoot = LayoutInflater.from(context).inflate(R.layout.recycleview_cell, parent, false);
        MyViewHolder mViewHolder = new MyViewHolder(itemRoot);
        itemRoot.setBackground(context.getResources().getDrawable(R.drawable.recyclerview_item_bg));
        itemRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 第五步，使用getTag方法获取点击的item的position
                 */
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, (int) v.getTag());
                }
            }
        });
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.img.setImageDrawable(context.getResources().getDrawable(itemImg[position]));
        holder.text.setText(itemText[position]);
        /**
         * 第四步，将position保存在itemView的Tag中，以便点击时进行获取
         */
        holder.itemView.setTag(position);
    }


    @Override
    public int getItemCount() {
        return itemImg.length;
    }

    /**
     * 在Activity中设置item点击事件的方法第一步：
     * 第一步，定义接口,在activity里面使用setOnItemClickListener方法并创建此接口的对象、实现其方法
     */
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * 第二步，为Activity提供设置OnItemClickListener的接口
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView text;

        public MyViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img_recycleView);
            text = (TextView) itemView.findViewById(R.id.tv_recycleView);

        }
    }
}
