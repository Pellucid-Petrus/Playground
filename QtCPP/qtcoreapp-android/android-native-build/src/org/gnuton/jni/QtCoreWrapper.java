package org.gnuton.jni;

import android.util.Log;

public class QtCoreWrapper {
	//public native int intMethod(int n);
	private static QtCoreWrapper uniqInstance;

	private static final String TAG = QtCoreWrapper.class.getName();

	public static synchronized QtCoreWrapper getInstance(String qtAppName)
	{		
		if (uniqInstance == null) {
			uniqInstance = new QtCoreWrapper(qtAppName);
		}
		return uniqInstance;
	}
	
	/**
	 * Create QtCoreWrapper class and loads the required libraries
	 * @param appName
	 */
	private QtCoreWrapper(String appName){
		// Load libararies - You may wanna add more libs here!!
		String libs[] = new String[]{"gnustl_shared", "Qt5Core", appName};
		
		for (String lib : libs){
			Log.d(TAG, "Loading "+ lib);
			try{
				System.loadLibrary(lib);
			}catch (UnsatisfiedLinkError e){
				// This is actually needed to load QtCore
				Log.d(TAG, "Hey we may have had a problem loading a lib!!");
			}
		}
	}
	
}