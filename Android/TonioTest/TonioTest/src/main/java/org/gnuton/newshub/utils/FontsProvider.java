package org.gnuton.newshub.utils;

import android.graphics.Typeface;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by gnuton on 7/2/13.
 */
public class FontsProvider {
    static FontsProvider mInstance;
    private final Map<String, Typeface> map = new Hashtable<String, Typeface>();

    static public FontsProvider getInstace() {
        if (mInstance == null)
            mInstance = new FontsProvider();
        return mInstance;
    }

    private FontsProvider(){
    }

    public Typeface getTypeface(String fontsName) {
        if (!map.containsKey(fontsName))
            map.put(fontsName,  Typeface.createFromAsset(MyApp.getContext().getAssets(), "fonts/" + fontsName + ".ttf"));
        return map.get(fontsName);
    }

}
