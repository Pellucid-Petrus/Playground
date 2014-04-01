What is this project about:
    Aim of this project is to run a QtCoreApplication with a native Android GUI.

How it works:

How to compile the project
    1. Load the Qt project with Qt Creator. On the left side, click "projects" and uncheck "shadow build"
    2. Press the "play" button to build the code, this will create an android-build dir with some .so files and it will deploy the app the device. The app won't actually work.
    3. Now let's edit the java files. Open Android Studio (> 0.5.2), File  > New > Project > from existing code. This will import the project.
    4. Select MainActivity on the left panel and press the "play" button in the toolbar to build and run the app on the device/emulator


How to connect Java to Qt?
    We are gonna use JNI APIs.
    Here is the schema which shows how to expose QObjects to Java.
       (QObject header --> Java class -> C++ header -> C++ source code)

    This is the procedure to create the needed files step by step:

      0. Before starting, please run setup.sh in the jnitools directory

      1. QObject header --> Java class
         Crate a java class to wrap your C++ Code. This is the class you have to call from Java to access to
         your C++ objects.
         This class can be generated automatically by the script "jni-tools/create_java_wrapper.py"

         The script:
         a. Creates java code for properties. Properties are defined in this way in c++ class header
            Q_PROPERTY(QString priority READ priority WRITE setPriority NOTIFY priorityChanged)
         b. Creates defines a java function for Q_INVOKABLE??? methods [TODO]

         Usage
          a. run
               create_java_wrapper.py qtmyobj.h
             it will generate the java file: qtmyobjWrapper.java
          b. move the generated java file into the right package (eg: directory src/org/gnuton/jni/wrappers)
               mv qtmyobjWrapper.java android-native-build/src/org/gnuton/jni/wrappers

      2. Java class -> C++ header
         Now we need to generate the C++ wrapper for the QObject class.

         a. a bash script can do this for us;
           cd android-native-grandle/app/src/main/java/org/gnuton/jni/wrappers
           java2cppWrappers.sh file.java
           Note: If it doesn't work please update the ROOT_DIR and SRC_DIR paths.

         This command outputs the c++ header file that we need to implement in C++ and that we can find in
            ../../..//org_gnuton_jni_QtMyObjTestWrapper.h

         b.Move the header in the directory containing your Qt code:
         c.create a cpp file for the generated header
         c.Ádd the h and cpp files to the .pro file of your project
           eg: Add org_gnuton_jni_wrappers_QtCoreApplicationWrapper.h and org_gnuton_jni_wrappers_QtCoreApplicationWrapper.cpp

> How to access to call C++ from Java:
    JNIEnv *env;
    cls = (*env)->FindClass(env, "Sample2");
    if(cls !=0){
      mid = (*env)->GetStaticMethodID(env, cls, "intMethod", "(I)I");

    if(mid !=0) {
      square = (*env)->CallStaticIntMethod(env, cls, mid, 5);
      printf("Result of intMethod: %d\n", square);
    }

> Convert Java types to C++ and vice versa
    http://qt-project.org/doc/qt-5/qandroidjniobject.html#primitive-types


> References:
    http://docs.oracle.com/javase/7/docs/technotes/guides/jni/spec/functions.html
    http://journals.ecs.soton.ac.uk/java/tutorial/native1.1/implementing/method.html

