package com.qinqi.debugtoolbox.crash;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.qinqi.debugtoolbox.R;
import com.qinqi.debugtoolbox.log.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CrashReportsDetailActivity extends Activity {
    private TextView tvContent;
    private String tempFile;
    String strContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash_reports_detail);

        getActionBar().setTitle("CrashViewer");
        getActionBar().setDisplayHomeAsUpEnabled(true);

        this.tvContent = (TextView) this.findViewById(R.id.textContent);
        strContent = getTvContent();
        this.tvContent.setText(strContent);
    }

    private String getTvContent() {
        tempFile = getIntent().getStringExtra("text");

        File file = new File(tempFile);
        if( file != null && file.exists()){
            try {
                FileInputStream is = new FileInputStream(file);
                byte buffer[]=new byte[(int)file.length()];
                is.read(buffer);
                is.close();
                return new String(buffer);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
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
        menu.add("share info").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                shareLeak();
                return true;
            }
        });
        menu.add("share file").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                shareHeapDump();
                return true;
            }
        });
        return true;
    }

    void shareLeak() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, strContent);
        startActivity(intent);
    }

    void shareHeapDump() {
        Logger.d("CrashReportsDetails","tempFile:" + tempFile);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/octet-stream");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + tempFile));
        startActivity(intent);
    }
}