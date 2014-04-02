#!/bin/bash

##### you may wanna edit these lines!! #####
QT_SDK_PATH=/home/gnuton/Desktop/Qt5Android/5.2.1
PRO_FILE=qtcoreapp.pro
export ANDROID_NDK_ROOT=/home/gnuton/necessitas/android-ndk/

###### Do not edit the lines below ####
export ANDROID_NDK_TOOLCHAIN_VERSION=4.6
QMAKE=$QT_SDK_PATH/android_armv7/bin/qmake

$QMAKE $PRO_FILE CONFIG+=debug
make install INSTALL_ROOT=android-build

