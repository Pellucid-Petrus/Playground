package org.gnuton.jni.wrappers;

/*
 * JNI Java side layer for QtCoreApplication.
 */
public class QtCoreApplicationWrapper extends QtGenericObjectWrapper {
    // Runs the QtApplication in a separate thread
	public native void startApplication();

    // Terminates the main QEventLoop of the Qt app
	public native void stopApplication();

    // Constructor not accessible to classes outside the JNI package
    protected  QtCoreApplicationWrapper(){};
}
