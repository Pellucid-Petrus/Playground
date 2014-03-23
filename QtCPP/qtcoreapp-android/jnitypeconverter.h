#ifndef JNITYPECONVERTER_H
#define JNITYPECONVERTER_H
#include <jni.h>

class JNITypeConverter
{
public:    
    // Converts JString to QString
    static QString toQString(JNIEnv* env, jstring str){
        const char *cString = env->GetStringUTFChars( str, 0 );
        return QString(cString);
    }

    // Converts QString to JString
    static jstring toJString(JNIEnv* env, const QString& str){
        QByteArray byteArray = str.toUtf8();

        return env->NewStringUTF(byteArray.constData());
    }
};

#endif // JNITYPECONVERTER_H
