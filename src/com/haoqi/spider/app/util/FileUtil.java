package com.haoqi.spider.app.util;

import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class FileUtil {
    public static File createLogFile() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            String name = new Date().toLocaleString() + ".txt";
            File parent = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "dwb"
                    + File.separator + "log");
            if (!parent.exists()) {
                parent.mkdirs();
            }
            File log = new File(parent.getAbsolutePath(), name);
            try {
                log.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return log;
        } else {
            return null;
        }

    }

    public static String toFile(byte[] bfile, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            // int len = bfile.length;
            file = new File(fileName);
            file.getParentFile().mkdir();
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return null;
    }

    public static byte[] getBytes(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public static boolean isExist(String localPath) {
        if (TextUtils.isEmpty(localPath)) {
            return false;
        } else if (new File(localPath).exists()) {
            return true;
        }
        return false;
    }
}
