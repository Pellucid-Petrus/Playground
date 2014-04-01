#include "org_gnuton_jni_wrappers_QtCoreApplicationWrapper.h"

#include "QtMainThread.h"

// This is the function which the JAVA will call to run our Qt app. So this is the actual entrypoint!
JNIEXPORT void JNICALL Java_org_gnuton_jni_wrappers_QtCoreApplicationWrapper_startApplication(JNIEnv *, jobject ){
    qDebug() << "Starting Qt app";
    QtMainThread::getInstance().start();
}

JNIEXPORT void JNICALL Java_org_gnuton_jni_wrappers_QtCoreApplicationWrapper_stopApplication(JNIEnv *, jobject ){
    qDebug() << "Stopped Qt app";
    QtMainThread::getInstance().exit();
}
