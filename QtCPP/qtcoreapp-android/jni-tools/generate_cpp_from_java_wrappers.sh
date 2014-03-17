#!/bin/bash
ROOT_DIR=../../../../../ #android-native-build/src/org/gnuton/jni
SRC_DIR=../../../ # org/gnuton/jni
PKG=org.gnuton.jni

javac $1 #*Wrapper.java

# get list of classes
for file in $(ls *.class)
do
    java_class=${file%.class}
    echo "Processing:" $java_class
    cd $SRC_DIR
    javah $PKG.$java_class
    cd -
    rm $file
done

# show file created
echo "***** File created ****"
ls $SRC_DIR/*.h


