#include "org_gnuton_jni_qtmyobjWrapper.h"
#include "QtMainThread.h"

JNIEXPORT jstring JNICALL Java_org_gnuton_jni_qtmyobjWrapper_priority(JNIEnv *, jobject){
    QtMyObj* obj = QtMainThread::getInstance().instance_qtMyObj;
    Q_ASSERT(obj);
    return obj->priority();
}

JNIEXPORT void JNICALL Java_org_gnuton_jni_qtmyobjWrapper_setPriority(JNIEnv *, jobject, jstring str){
    QtMyObj* obj = QtMainThread::getInstance().instance_qtMyObj;
    Q_ASSERT(obj);
    return obj->setPriority(str);
}
