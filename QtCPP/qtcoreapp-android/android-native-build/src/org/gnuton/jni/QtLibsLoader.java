package org.gnuton.jni;

import android.app.Fragment;
import android.util.Log;

public class QtLibsLoader extends Fragment {
	private static final String TAG = QtLibsLoader.class.getName();

	private static String appName = "qtcoreapp"; // <-- THIS IS THE NAME OF THE LIB CONTAINING YOUR CODE!
	private static QtLibsLoader uniqInstance;
	private QtCoreApplicationWrapper coreApplicationWrapper;
	private boolean isAppStarted = false;
	
	/*
	public static synchronized QtLibsLoader getInstance()
	{		
		if (uniqInstance == null) {
			uniqInstance = new QtLibsLoader();
		}
		return uniqInstance;
	}*/
	
	/**
	 * Create QtCoreWrapper class and loads the required libraries
	 * @param appName
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
		
		coreApplicationWrapper = new QtCoreApplicationWrapper();
		
	}
	
	public synchronized void  startApplication(){
		if (this.isAppStarted)
			return;
		
		coreApplicationWrapper.startApplication();
		Log.d(TAG, "Qt EVENTLOOP HAS BEEN STARTED");
		
		this.isAppStarted = true;
	}
	
	public synchronized void  stopApplication(){
		if (!this.isAppStarted)
			return;
		
		// FIXME - THERE IS A MEMORY LEAK HERE MAN!!! The Qt app keeps on running in bg
		//Log.d(TAG, "Qt EVENTLOOP HAS BEEN STOPPED");
		//coreApplicationWrapper.stopApplication();
	}
}