package org.gnuton.jni;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

public class QtLibsLoader extends Fragment{
	private static final String TAG = QtLibsLoader.class.getName();

	private static String appName = "qtcoreapp"; // <-- THIS IS THE NAME OF THE LIB CONTAINING YOUR CODE!
	private static QtLibsLoader uniqInstance;
	private QtCoreApplicationWrapper coreApplicationWrapper;
	private boolean isAppStarted = false;


	public static synchronized QtLibsLoader getInstance()
	{		
		if (uniqInstance == null) {
			uniqInstance = new QtLibsLoader();
		}
		return uniqInstance;
	}
	
	/**
	 * Create QtCoreWrapper class and loads the required libraries
	 */
	private QtLibsLoader(){
		
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

        // Starts the Qt application in a new thread in background
		coreApplicationWrapper = new QtCoreApplicationWrapper();
        coreApplicationWrapper.startApplication();
	}

    /***  Fragment lifecycle methods ***/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        // Do not destroy this fragment when orientaiton changes!!
        setRetainInstance(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if (!this.isAppStarted)
			return;

        // Stops the Qt application whe the Java application is closed
        coreApplicationWrapper.stopApplication();
    }
}