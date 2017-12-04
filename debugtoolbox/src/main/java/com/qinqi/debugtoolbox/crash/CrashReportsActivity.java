package com.qinqi.debugtoolbox.crash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.qinqi.debugtoolbox.R;
import com.qinqi.debugtoolbox.manager.DebugToolBoxManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CrashReportsActivity extends Activity implements AdapterView.OnItemClickListener {
    private ListView mShowList;
    private List<File> fileList = new ArrayList<>();
    private CrashReportsAdapter fileAdapter = new CrashReportsAdapter(this);
    private static final String SD_CARD = Environment.getExternalStorageDirectory().getPath();
    private static final String strFolder = SD_CARD + "/Android/data/" + DebugToolBoxManager.getHostContext().getPackageName() + "/crash";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash_reports_list);

        setTitle("CrashList");
        getActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
        initMembers();
    }

    private void initView() {
        mShowList = (ListView) findViewById(R.id.lv_show);
    }

    private void initMembers() {
        File file = new File(strFolder);
        if (!file.exists()) {
            Toast.makeText(this, "还没有崩溃过!", Toast.LENGTH_SHORT).show();
        }
        scanFiles(strFolder);
        fileAdapter.setData(fileList);
        mShowList.setAdapter(fileAdapter);
        mShowList.setOnItemClickListener(this);
    }

    private void scanFiles(String path) {
        fileList.clear();
        File dir = new File(path);
        File[] subFiles = dir.listFiles();
        if (subFiles != null){
            for (File f : subFiles) {
                fileList.add(f);
            }
        }
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o2.getName().compareTo(o1.getName());

            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        File f = fileList.get(i);
        if (f.isDirectory()) {
            //da.scanFiles(f.getPath());
        } else {
            Intent intent = new Intent(this, CrashReportsDetailActivity.class);
            intent.putExtra("text", f.getAbsolutePath());
            startActivity(intent);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if( fileList.size() > 0){
            MenuItem mi = menu.add("Delete All");
            mi.setIcon(android.R.drawable.ic_delete);
            mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            mi.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    try{
                        for( File file: fileList){
                            file.delete();
                        }
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                    initMembers();
                    return true;
                }
            });
        }
        return true;
    }
}
