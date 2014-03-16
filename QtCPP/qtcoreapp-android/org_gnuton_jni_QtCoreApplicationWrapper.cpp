#include "org_gnuton_jni_QtCoreApplicationWrapper.h"

#include <QCoreApplication>
#include <qdebug.h>
#include <qtmyobj.h>

JNIEXPORT void JNICALL Java_org_gnuton_jni_QtCoreApplicationWrapper_startApplication(JNIEnv *, jobject){
    int argc =0;
    char *argv[] = {};
    QCoreApplication a(argc, argv);
    qDebug() << "QCoreApplication running!";

    // Create an instance of my object
    QtMyObj myObj;

    a.exec();
}
