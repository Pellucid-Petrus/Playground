#include "org_gnuton_jni_qtmyobjWrapper.h"
#include "QtMainThread.h"
#include "jnitypeconverter.h"

JNIEXPORT jstring JNICALL Java_org_gnuton_jni_qtmyobjWrapper_priority(JNIEnv *env, jobject){
    QtMyObj* obj = QtMainThread::getInstance().instance_qtMyObj;
    Q_ASSERT(obj);
    jstring str = JNITypeConverter::toJString(env, obj->priority());
    return str;
}

JNIEXPORT void JNICALL Java_org_gnuton_jni_qtmyobjWrapper_setPriority(JNIEnv *env, jobject, jstring str){
    QtMyObj* obj = QtMainThread::getInstance().instance_qtMyObj;
    Q_ASSERT(obj);
    QString _str = JNITypeConverter::toQString(env, str);
    return obj->setPriority(_str);
}
