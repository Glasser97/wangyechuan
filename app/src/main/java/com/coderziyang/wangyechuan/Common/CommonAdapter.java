package com.coderziyang.wangyechuan.Common;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {
    Context mContext;
    List<T> mDataList;

    public CommonAdapter(Context context,List<T> dataList){
        this.mContext=context;
        this.mDataList=dataList;
    }

    public Context getContext(){
        return mContext;
    }
    public List<T> getmDataList(){
        return mDataList;
    }

    /**
     * 添加数据源
     * @param mDataList
     */
    public void addDataList(List<T> mDataList){
        this.mDataList.addAll(mDataList);
        notifyDataSetChanged();
    }
    /**
     * 清除数据
     */
    public void clear(){
        this.mDataList.clear();
    }

    @Override
    public int getCount(){
        return mDataList.size();
    }

    @Override
    public Object getItem(int position){
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        convertView=convertView(position,convertView);
        return convertView;
    }

    /**
     * 需要重写的convertView方法
     * @param position
     * @param convertView
     * @return
     */

    public abstract View convertView(int position,View convertView);

}
