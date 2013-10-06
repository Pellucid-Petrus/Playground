package org.gnuton.newshub.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * from http://stackoverflow.com/questions/10185898/using-disklrucache-in-android-4-0-does-not-provide-for-opencache-method
 */

public class Utils {
    public static final int IO_BUFFER_SIZE = 8 * 1024;

    private Utils() {}

    public static boolean isExternalStorageRemovable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    public static File getExternalCacheDir(Context context) {
        if (hasExternalCacheDir()) {
            return context.getExternalCacheDir();
        }

        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    public static boolean hasExternalCacheDir() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    // Generates color in the range 0xff000000 (BLACK) -> 0xffffffff (WHITE)
    public static int generateColor(final String link) {
        int[] baseColors = new int[] {
                 0xFFFFFFFF
                ,0xFFED1C24
                ,0xFFF26522
                ,0xFFF7941D
                ,0xFFFFF200
                ,0xFF8DC73F
                ,0xFF39B54A
                ,0xFF00A651
                ,0xFF00A99D
                ,0xFF00AEEF
                ,0xFF0072BC
                ,0xFF0054A6
                ,0xFF2E3192
                ,0xFF662D91
                ,0xFF92278F
                ,0xFFEC008C
                ,0xFFED145B
        };

        if (link == null || link == "")
            return baseColors[0];

        byte[] b;
        try {
            b = link.substring(0,2).getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return baseColors[1];
        }
        int hash = 0;
        for (byte i : b)
            hash += i;

        return baseColors[hash % baseColors.length];
    }
}