package org.gnuton.jni;

/*
 * JNI Java side layer for QtCoreApplication.
 */
public class QtCoreApplicationWrapper {
    // Runs the QtApplication in a separate thread
	public native void startApplication();

    // Terminates the main QEventLoop of the Qt app
	public native void stopApplication();
}
