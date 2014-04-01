#!/bin/bash
####################### PLEASE UPDATE THESE VARS ACCORDING TO YOUR OWN PATH ################################################################################
PKG=org.gnuton.jni.wrappers
SRC_DIR=../../../../ # org.gnuton.jni.wrappers 
############################################################################################################################################################
echo "Creating wrappers for $1"
rm *.class &>/dev/null
javac $1 QtObjectWrapper.java #*Wrapper.java

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


