package com.shengma.lanjing.mediarecorder.util;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Author: luqihua
 * Time: 2017/12/4
 * Description: FileUtil
 */

public class FileUtil {
    private static final String ROOT_DIR = "lanjingmp4";//以aaa开头容易查找
    private static String sRootPath = "";
    private static boolean hasInitialize = false;

    public static void init(Context context) {
        if (hasInitialize) return;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            sRootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + ROOT_DIR;
        } else {
            sRootPath = context.getCacheDir().getAbsolutePath() + "/" + ROOT_DIR;
        }
        File file = new File(sRootPath);
        if (!file.exists()) {
            boolean success = file.mkdirs();
            if (!success) {
                throw new RuntimeException("create file failed");
            }
        }
        hasInitialize = true;
    }


    public static File newMp4File() {
        SimpleDateFormat format = new SimpleDateFormat("MM_dd_HH_mm_ss", Locale.CHINA);
        return new File(sRootPath, "mp4_" + format.format(new Date()) + ".mp4");
    }

    public static File newAccFile() {
        SimpleDateFormat format = new SimpleDateFormat("MM_dd_HH_mm_ss", Locale.CHINA);
        return new File(sRootPath, "acc_" + format.format(new Date()) + ".acc");
    }
}
