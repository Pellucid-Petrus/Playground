package org.gnuton.jni.wrappers;

import org.gnuton.jni.QtLibsLoader;

public class qtmyobjWrapper implements QtObjectWrapper {
    public native String priority();
    public native void setPriority(String value);
    public static void priorityChanged(){
        /** This method is called by C++ when the properti is changed
         Add here the code which will manage the java callbacks **/
        QtLibsLoader.getInstance();
    };
}