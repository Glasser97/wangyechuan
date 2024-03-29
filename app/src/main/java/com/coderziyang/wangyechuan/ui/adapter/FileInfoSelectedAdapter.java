package com.coderziyang.wangyechuan.ui.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coderziyang.wangyechuan.AppContext;
import com.coderziyang.wangyechuan.Constant;
import com.coderziyang.wangyechuan.R;
import com.coderziyang.wangyechuan.core_func.utils.FileUtils;
import com.coderziyang.wangyechuan.entity.FileInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FileInfoSelectedAdapter extends BaseAdapter {

    private Context mContext;
    private Map<String, FileInfo> mdataHashMap;
    private String[] mKeys;

    List<Map.Entry<String, FileInfo>> fileInfoMapList;
    OnDataListChangedListener mOnDataListChangedListener;

    public FileInfoSelectedAdapter(Context mContext){
        this.mContext = mContext;
        mdataHashMap = AppContext.getAppContext().getFileInfoMap();
        fileInfoMapList = new ArrayList<>(mdataHashMap.entrySet());
        //排序
        Collections.sort(fileInfoMapList, Constant.DEFAULT_COMPARATOR);
    }

    public void setOnDataListChangedListener(OnDataListChangedListener onDataListChangedListener) {
        this.mOnDataListChangedListener = onDataListChangedListener;
    }

    @Override
    public void notifyDataSetChanged(){
        mdataHashMap = AppContext.getAppContext().getFileInfoMap();
        fileInfoMapList = new ArrayList<>(mdataHashMap.entrySet());
        Collections.sort(fileInfoMapList, Constant.DEFAULT_COMPARATOR);
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return fileInfoMapList.size();
    }

    @Override
    public Object getItem(int position) {
        return fileInfoMapList.get(position).getValue();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final FileInfo fileInfo = (FileInfo) getItem(position);

        FileSenderHolder viewHolder = null;
        if(convertView == null){
            convertView = View.inflate(mContext, R.layout.item_transfer, null);
            viewHolder = new FileSenderHolder();
            viewHolder.iv_shortcut = (ImageView) convertView.findViewById(R.id.iv_shortcut);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_progress = (TextView) convertView.findViewById(R.id.tv_progress);
            viewHolder.pb_file = (ProgressBar) convertView.findViewById(R.id.pb_file);
            viewHolder.btn_operation = (Button) convertView.findViewById(R.id.btn_operation);
            viewHolder.iv_tick = (ImageView) convertView.findViewById(R.id.iv_tick);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (FileSenderHolder) convertView.getTag();
        }

        if(fileInfo != null){
            //初始化
            viewHolder.pb_file.setVisibility(View.INVISIBLE);
            viewHolder.btn_operation.setVisibility(View.INVISIBLE);
            viewHolder.iv_tick.setVisibility(View.VISIBLE);
            viewHolder.iv_tick.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_del));

            if(FileUtils.isApkFile(fileInfo.getFilePath()) || FileUtils.isMp4File(fileInfo.getFilePath())){ //Apk格式 或者MP4格式需要 缩略图
                viewHolder.iv_shortcut.setImageBitmap(fileInfo.getBitmap());
            }else if(FileUtils.isJpgFile(fileInfo.getFilePath())){//图片格式
                Glide.with(mContext)
                        .load(fileInfo.getFilePath())
                        .centerCrop()
                        .placeholder(R.mipmap.icon_jpg)
                        .into(viewHolder.iv_shortcut);
            }else if(FileUtils.isMp3File(fileInfo.getFilePath())){//音乐格式
                viewHolder.iv_shortcut.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_mp3,null));
            }

            viewHolder.tv_name.setText(fileInfo.getFilePath());
            viewHolder.tv_progress.setText(FileUtils.getFileSize(fileInfo.getFileSize()));

            viewHolder.iv_tick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppContext.getAppContext().getFileInfoMap().remove(fileInfo.getFilePath());
                    notifyDataSetChanged();
                    if (mOnDataListChangedListener != null)
                        mOnDataListChangedListener.onDataChanged();
                }
            });
        }

        return convertView;
    }

    static class FileSenderHolder {
        ImageView iv_shortcut;
        TextView tv_name;
        TextView tv_progress;
        ProgressBar pb_file;

        Button btn_operation;
        ImageView iv_tick;
    }

    /**
     * 数据改变监听
     */
    public interface OnDataListChangedListener{
        void onDataChanged();
    }
}
