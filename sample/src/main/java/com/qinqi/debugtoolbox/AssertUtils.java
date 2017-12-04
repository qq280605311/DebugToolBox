package com.qinqi.debugtoolbox;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by pscj on 2016/5/31.
 */
public class AssertUtils {
    public static void copyAssertFile(Context context, String srcPathFileName, String dstFilePath, String dstFileName){
        if ( !(new File(dstFilePath + File.separator + dstFileName)).exists() ){
            File f = new File(dstFilePath);
            if (!f.exists()) {
                f.mkdir();
            }

            try {
                InputStream is = context.getAssets().open(srcPathFileName);
                OutputStream os = new FileOutputStream(dstFilePath + File.separator + dstFileName);

                byte[] buffer = new byte[1024 * 64];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                os.flush();
                os.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static public void copyAssetLogoFiles(Context context) {
        String[] logoFileList = null;
        AssetManager manager = context.getAssets();
        File filesDir = new File(context.getFilesDir(), "logo");
        //没有logo目录，视作是第一次需要复制文件
        if( !filesDir.exists()){
            try {
                logoFileList = manager.list("logo");
                for (String file : logoFileList) {
                    copyAssertFile(context, "logo/"+file,filesDir.getPath(), file);
                }
                logoFileList = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
