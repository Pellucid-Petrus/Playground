package org.gnuton.jni;
public class qtmyobjWrapper {

        public native String priority();
        public native void setPriority(String value);
        public static void priorityChanged(){
		/** This method is called by C++ when the properti is changed
		Add here the code which will manage the java callbacks **/
            QtLibsLoader.getInstance();
	    };
}