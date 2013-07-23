package org.gnuton.newshub.utils;

/**
 * Created by gnuton on 6/16/13.
 */
public class TextUtils {
    static String removeNonPrintableChars(String s) {
        //s = s.replaceAll("[\\x00-\\x1F]","");
        s = s.replaceAll("[\t\n\r\f]","");
        return s;
    }
}
