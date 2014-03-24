package org.gnuton.jni.wrappers;

/**
 * Created by gnuton
 */
public abstract class QtGenericObjectWrapper {

    // To make things simple let's make singleton our interface
    private static QtGenericObjectWrapper uniqInstance;

    public static synchronized QtGenericObjectWrapper getInstance(Class c)
    {
        if (uniqInstance == null) {
            try {
                uniqInstance = (QtGenericObjectWrapper) c.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return uniqInstance;
    }
}
