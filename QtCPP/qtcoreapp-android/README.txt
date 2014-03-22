How to compile the project
1. from project uncheck "shadow build"
2. Press the "play" button to build the code, this will create an android-build dir with some .so files
3. Open eclipse or Android Studio (> 0.5.2), File  > New > Project > from existing code. This will import the project. Please use Eclipse to edit the java files
4. Select MainActivity on the left panel and press the "play" button in the toolbar to build and run the app on the device/emulator


How to connect Java an C++?
We are gonna use JNI.
This is the way we can expose QObjects to JAVA
  (QObject header --> Java class -> C++ header -> C++ source code)

This is the procedure to create the needed files step by step:

  1. QObject header --> Java class
     Crate a java class to wrap your C++ Code. This is the class you have to call from Java to access to
     your C++ objects.
     This class can be generated automatically by the script "jni-tools/createJavaWrapper.py"

     The script:
     a. Creates java code for properties. Properties are defined in this way in c++ class header
        Q_PROPERTY(QString priority READ priority WRITE setPriority NOTIFY priorityChanged)
     b. Creates defines a java function for Q_INVOKABLE??? methods [TODO]

     Usage
      a. run
           jni-tools/create_java_wrapper.py qtmyobj.h
         it will generate the java file: qtmyobjWrapper.java
      b. move the generated java file into the right package (eg: directory src/org/gnuton/jni)
           mv qtmyobjWrapper.java android-native-build/src/org/gnuton/jni/

  2. Java class -> C++ header
     Now we need to generate the C++ wrapper for the QObject class.

     a. a bash script can do this for us;
       cd android-native-build/src/org/gnuton/jni
       ../../../../../jni-tools/generate_cpp_from_java_wrappers.sh
     This command outputs the c++ header file that we need to implement in C++ and that we can find in
        ../../..//org_gnuton_jni_QtMyObjTestWrapper.h

     b.




JNIEnv *env;
cls = (*env)->FindClass(env, "Sample2");
31. if(cls !=0)
32. { mid = (*env)->GetStaticMethodID(env, cls, "intMethod", "(I)I");
33. if(mid !=0)
34. { square = (*env)->CallStaticIntMethod(env, cls, mid, 5);
35. printf("Result of intMethod: %d\n", square);
36. }


Java types <-> C++ http://qt-project.org/doc/qt-5/qandroidjniobject.html#primitive-types
