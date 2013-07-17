package org.gnuton.newshub.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gnuton on 6/21/13.
 */
public class FragmentUtils {
    private static Map mMap = new HashMap<String, Fragment>();

    public static Fragment getFragment(FragmentManager fm, String className, String tag){
        if (tag == null)
            tag = className;

        //TOCHECK What happens if android kills the fragment?
        Fragment f = (Fragment) mMap.get(tag);
        if (f != null)
            return f;

        try {
            //Java reflection is cool!!
            f = (Fragment) Class.forName(className).newInstance(); //Fragment.instantiate(context, MyFragment.class.getName(), myBundle)
            f.setRetainInstance(true);
            mMap.put(tag, f);
            return f;
        } catch (InstantiationException e) {
            e.printStackTrace();

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
