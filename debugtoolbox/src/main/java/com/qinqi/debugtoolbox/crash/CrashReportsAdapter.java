package com.qinqi.debugtoolbox.crash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qinqi.debugtoolbox.R;
import com.qinqi.debugtoolbox.util.DebugToolBoxUtils;
import com.qinqi.debugtoolbox.util.ViewHolder;

import java.io.File;
import java.util.List;


public class CrashReportsAdapter extends BaseAdapter {
    private Context context;
    private List<File> list;

    public int getCount() {
        return list.size();
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View arg1, ViewGroup arg2) {
        if (arg1 == null) {
            arg1 = LayoutInflater.from(context).inflate(R.layout.item_crash_reports_file, null);
        }
        TextView Txt_Name = ViewHolder.get(arg1, R.id.txt_Name);
        TextView Txt_Size = ViewHolder.get(arg1, R.id.txt_Size);
        //ImageView img = (ImageView) ViewHolder.get(arg1, R.id.image_ico);
        File f = list.get(position);
        Txt_Name.setText(f.getName());
        Txt_Size.setText(getFilesSize(f));
//        if (f.isDirectory())
//            img.setImageBitmap(bmp_folder);
//        else
//            img.setImageBitmap(bmp_file);
        return arg1;
    }

    public void setData(List<File> fileList){
        this.list = fileList;
    }

    public CrashReportsAdapter(Context context) {
        this.context = context;
    }

    public static String getFilesSize(File f) {

        if (f.isFile()) {
            long length = f.length();
            return DebugToolBoxUtils.getLength(length);
        }
        return "";
    }
}

