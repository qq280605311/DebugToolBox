package com.qinqi.debugtoolbox.log;

import android.content.Context;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qinqi.debugtoolbox.R;
import com.qinqi.debugtoolbox.util.DebugToolBoxUtils;
import com.qinqi.debugtoolbox.view.AmazingAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by qinqi on 2016/11/23.
 */

public class LogViewerAdapter extends AmazingAdapter<DebugBoxLog> {
    private Context mContext;
    private List<Pair<Long, List<DebugBoxLog>>> mList;

    public LogViewerAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
    }

    public void setData(List<DebugBoxLog> list) {
        mList.clear();

        if (DebugToolBoxUtils.isEmpty(list)) {
            return;
        }
        Map<Long, List<DebugBoxLog>> logMap = new TreeMap<>();
        for (DebugBoxLog log : list) {
            long time = DebugToolBoxUtils.getDayStartTime(log.getTime());
            List<DebugBoxLog> logList = logMap.get(time);
            if (DebugToolBoxUtils.isEmpty(logList)) {
                logList = new ArrayList<>();
                logMap.put(time, logList);
            }

            logList.add(log);
        }

        Set<Map.Entry<Long, List<DebugBoxLog>>> set = logMap.entrySet();
        Iterator<Map.Entry<Long, List<DebugBoxLog>>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, List<DebugBoxLog>> entry = iterator.next();
            mList.add(new Pair<Long, List<DebugBoxLog>>(entry.getKey(), entry.getValue()));
        }
        timeSort(mList);

        notifyDataSetChanged();
    }

    /**
     * 按时间排序
     *
     * @param list 数据源
     */
    private void timeSort(List<Pair<Long, List<DebugBoxLog>>> list) {
        Collections.sort(list, new Comparator<Pair<Long, List<DebugBoxLog>>>() {
            @Override
            public int compare(Pair<Long, List<DebugBoxLog>> pair1, Pair<Long, List<DebugBoxLog>> pair2) {
                long time1 = pair1.first;
                long time2 = pair2.first;

                if(time1 < time2){
                    return 1;
                }else {
                    return -1;
                }
            }
        });
    }

    @Override
    public List<Pair<Long, List<DebugBoxLog>>> getData() {
        return mList;
    }

    @Override
    protected void bindSectionHeader(View view, int position, boolean displaySectionHeader) {
        if (displaySectionHeader) {
            view.findViewById(R.id.ll_pinned_header).setVisibility(View.VISIBLE);
            TextView timeTV = (TextView) view.findViewById(R.id.tv_pinned_header);
            long time = getSections()[getSectionForPosition(position)];
            timeTV.setText(DebugToolBoxUtils.formatDateString(new Date(time), "yyyy-MM-dd"));
        } else {
            view.findViewById(R.id.ll_pinned_header).setVisibility(View.GONE);
        }
    }

    @Override
    public View getAmazingView(int position, View convertView, ViewGroup parent) {
        ImageView iconIV;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_log, parent, false);
            iconIV = (ImageView) convertView.findViewById(R.id.iv_log_state);
            convertView.setTag(iconIV);
        }else {
            iconIV = (ImageView) convertView.getTag();
        }

        TextView timeTV = (TextView) convertView.findViewById(R.id.tv_log_time);
        TextView pidTV = (TextView) convertView.findViewById(R.id.tv_log_pid);
        TextView tagTV = (TextView) convertView.findViewById(R.id.tv_log_tag);
        TextView msgTV = (TextView) convertView.findViewById(R.id.tv_log_msg);

        if (!DebugToolBoxUtils.isEmpty(getItem(position))) {
            DebugBoxLog log = getItem(position);
            if (log.getState() == DebugBoxLog.STATE_D) {
                iconIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.drw_log_d));
                timeTV.setTextColor(mContext.getResources().getColor(R.color.color_006fba));
                pidTV.setTextColor(mContext.getResources().getColor(R.color.color_006fba));
                tagTV.setTextColor(mContext.getResources().getColor(R.color.color_006fba));
                msgTV.setTextColor(mContext.getResources().getColor(R.color.color_006fba));
            } else if (log.getState() == DebugBoxLog.STATE_E) {
                iconIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.drw_log_e));
                timeTV.setTextColor(mContext.getResources().getColor(R.color.color_ff0000));
                pidTV.setTextColor(mContext.getResources().getColor(R.color.color_ff0000));
                tagTV.setTextColor(mContext.getResources().getColor(R.color.color_ff0000));
                msgTV.setTextColor(mContext.getResources().getColor(R.color.color_ff0000));
            } else if (log.getState() == DebugBoxLog.STATE_I) {
                iconIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.drw_log_i));
                timeTV.setTextColor(mContext.getResources().getColor(R.color.color_000000));
                pidTV.setTextColor(mContext.getResources().getColor(R.color.color_000000));
                tagTV.setTextColor(mContext.getResources().getColor(R.color.color_000000));
                msgTV.setTextColor(mContext.getResources().getColor(R.color.color_000000));
            } else if (log.getState() == DebugBoxLog.STATE_V) {
                iconIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.drw_log_v));
                timeTV.setTextColor(mContext.getResources().getColor(R.color.color_48bb31));
                pidTV.setTextColor(mContext.getResources().getColor(R.color.color_48bb31));
                tagTV.setTextColor(mContext.getResources().getColor(R.color.color_48bb31));
                msgTV.setTextColor(mContext.getResources().getColor(R.color.color_48bb31));
            } else if (log.getState() == DebugBoxLog.STATE_W) {
                iconIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.drw_log_w));
                timeTV.setTextColor(mContext.getResources().getColor(R.color.color_ec732b));
                pidTV.setTextColor(mContext.getResources().getColor(R.color.color_ec732b));
                tagTV.setTextColor(mContext.getResources().getColor(R.color.color_ec732b));
                msgTV.setTextColor(mContext.getResources().getColor(R.color.color_ec732b));
            } else {//默认为 log.I
                iconIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.drw_log_i));
                timeTV.setTextColor(mContext.getResources().getColor(R.color.color_000000));
                pidTV.setTextColor(mContext.getResources().getColor(R.color.color_000000));
                tagTV.setTextColor(mContext.getResources().getColor(R.color.color_000000));
                msgTV.setTextColor(mContext.getResources().getColor(R.color.color_000000));
            }

            String message = log.getMessage();
            if( !TextUtils.isEmpty(message)){
                if(message.length() > 70){
                    message = message.substring(0,70);
                }
                msgTV.setText(message);
            }

            timeTV.setText("time:" + DebugToolBoxUtils.formatDateString(new Date(log.getTime()),"HH:mm:ss:SSS"));
            pidTV.setText("pid:" + log.getPid());
            tagTV.setText(log.getTag());
        }

        return convertView;
    }

    @Override
    public void configurePinnedHeader(View header, int position, int alpha) {
        TextView timeTV = (TextView) header.findViewById(R.id.tv_pinned_header);
        long time = getSections()[getSectionForPosition(position)];
        timeTV.setText(DebugToolBoxUtils.formatDateString(new Date(time),"yyyy-MM-dd"));
    }

    @Override
    public int getPositionForSection(int section) {
        if(section < 0){
            section = 0;
        }
        if(section>= mList.size()){
            section = mList.size() - 1;
        }
        int c = 0;
        for(int i = 0;i < mList.size();i++){
            if(section == i){
                return c;
            }
            c += mList.get(i).second.size();
        }
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        int c = 0;
        for(int i = 0;i < mList.size();i++){
            if(position >= c && position < c + mList.get(i).second.size()){
                return i;
            }
            c += mList.get(i).second.size();
        }
        return -1;
    }

    @Override
    public Long[] getSections() {
        Long[] res = new Long[mList.size()];
        for (int i = 0; i < mList.size(); i++) {
            res[i] = mList.get(i).first;
        }
        return res;
    }

    @Override
    public int getCount() {
        int res = 0;
        for (int i = 0; i < mList.size(); i++) {
            res += mList.get(i).second.size();
        }
        return res;
    }

    @Override
    public DebugBoxLog getItem(int position) {
        int c = 0;
        for (int i = 0; i < mList.size(); i++) {
            if (position >= c && position < c + mList.get(i).second.size()) {
                return mList.get(i).second.get(position - c);
            }
            c += mList.get(i).second.size();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
