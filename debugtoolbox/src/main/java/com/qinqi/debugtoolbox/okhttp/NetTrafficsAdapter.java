package com.qinqi.debugtoolbox.okhttp;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import me.codeboy.android.aligntextview.AlignTextView;

public class NetTrafficsAdapter extends AmazingAdapter<NetTraffics> {
    private Context mContext;
    private List<Pair<Long, List<NetTraffics>>> mList;

    public NetTrafficsAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
    }

    public void setData(List<NetTraffics> list) {
        mList.clear();

        if (DebugToolBoxUtils.isEmpty(list)) {
            return;
        }

        Map<Long, List<NetTraffics>> trafficsMap = new TreeMap<>();

        for (NetTraffics net : list) {
            long time = DebugToolBoxUtils.getDayStartTime(net.getTime());

            List<NetTraffics> netTrafficsList = trafficsMap.get(time);
            if (netTrafficsList == null) {
                netTrafficsList = new ArrayList<>();
                trafficsMap.put(time, netTrafficsList);
            }
            netTrafficsList.add(net);
        }

        Set<Map.Entry<Long, List<NetTraffics>>> set = trafficsMap.entrySet();
        Iterator<Map.Entry<Long, List<NetTraffics>>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, List<NetTraffics>> entry = iterator.next();
            mList.add(new Pair<Long, List<NetTraffics>>(entry.getKey(), entry.getValue()));
        }
        timeSort(mList);
        notifyDataSetChanged();
    }

    /**
     * 按时间排序
     *
     * @param list 数据源
     */
    private void timeSort(List<Pair<Long, List<NetTraffics>>> list) {
        Collections.sort(list, new Comparator<Pair<Long, List<NetTraffics>>>() {
            @Override
            public int compare(Pair<Long, List<NetTraffics>> pair1, Pair<Long, List<NetTraffics>> pair2) {
                long time1 = pair1.first;
                long time2 = pair2.first;

                if (time1 < time2) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
    }

    @Override
    public int getCount() {
//        return mList == null ? 0 : mList.size();
        int res = 0;
        for (int i = 0; i < mList.size(); i++) {
            res += mList.get(i).second.size();
        }
        return res;
    }

    @Override
    public NetTraffics getItem(int position) {
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

    @Override
    public List<Pair<Long, List<NetTraffics>>> getData() {
        return mList;
    }

    @Override
    protected void bindSectionHeader(View view, int position, boolean displaySectionHeader) {
        if (displaySectionHeader) {
            view.findViewById(R.id.ll_pinned_header).setVisibility(View.VISIBLE);
            TextView timeTV = (TextView) view.findViewById(R.id.tv_pinned_header);
            long time = getSections()[getSectionForPosition(position)];
            timeTV.setText(DebugToolBoxUtils.formatDateString(new Date(time), "yyyy-MM-dd "));
        } else {
            view.findViewById(R.id.ll_pinned_header).setVisibility(View.GONE);
        }
    }

    @Override
    public View getAmazingView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_network_traffics, parent, false);
        }

        TextView index = (TextView) convertView.findViewById(R.id.tv_nt_index);
        TextView time = (TextView) convertView.findViewById(R.id.tv_nt_time);
        TextView code = (TextView) convertView.findViewById(R.id.tv_nt_code);
        TextView length = (TextView) convertView.findViewById(R.id.tv_nt_length);
        AlignTextView url = (AlignTextView) convertView.findViewById(R.id.tv_nt_url);
        TextView elapsedTime = (TextView) convertView.findViewById(R.id.tv_nt_elapsed_time);

        if (!DebugToolBoxUtils.isEmpty(getItem(position))) {
            NetTraffics traffics = getItem(position);
            index.setText(String.valueOf(position + 1));
            time.setText(DebugToolBoxUtils.formatDateString(new Date(traffics.getTime()), "HH:mm:ss:SSS"));
            code.setText("code:" + traffics.getCode());
            if (traffics.getResponseContentLength() == -1) {
                length.setText("length:-1");
            } else {
                length.setText("length:" + DebugToolBoxUtils.getLength(traffics.getResponseContentLength()));
            }
            String urlStr = traffics.getMethod().toUpperCase() + " " + traffics.getUrl();
            url.setText(urlStr);
            elapsedTime.setText(DebugToolBoxUtils.getElapsedTime(traffics.getElapsedTime()));

            if(traffics.getCode() != 200){
                index.setTextColor(mContext.getResources().getColor(R.color.color_ff0000));
                time.setTextColor(mContext.getResources().getColor(R.color.color_ff0000));
                code.setTextColor(mContext.getResources().getColor(R.color.color_ff0000));
                length.setTextColor(mContext.getResources().getColor(R.color.color_ff0000));
                url.setTextColor(mContext.getResources().getColor(R.color.color_ff0000));
                elapsedTime.setTextColor(mContext.getResources().getColor(R.color.color_ff0000));
            }
        } else {
            index.setText(String.valueOf(position));
            time.setText("数据出错");
            url.setText("数据出错");
        }

        return convertView;
    }

    @Override
    public void configurePinnedHeader(View header, int position, int alpha) {
        TextView timeTV = (TextView) header.findViewById(R.id.tv_pinned_header);
        long time = getSections()[getSectionForPosition(position)];
        timeTV.setText(DebugToolBoxUtils.formatDateString(new Date(time), "yyyy-MM-dd "));
    }

    @Override
    public int getPositionForSection(int section) {
        if (section < 0) {
            section = 0;
        }
        if (section >= mList.size()) {
            section = mList.size() - 1;
        }
        int c = 0;
        for (int i = 0; i < mList.size(); i++) {
            if (section == i) {
                return c;
            }
            c += mList.get(i).second.size();
        }
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        int c = 0;
        for (int i = 0; i < mList.size(); i++) {
            if (position >= c && position < c + mList.get(i).second.size()) {
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
}

