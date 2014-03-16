#-------------------------------------------------
#
# Project created by QtCreator 2014-03-15T22:19:58
#
#-------------------------------------------------

QT       += core

QT       -= gui

TARGET = qtcoreapp
CONFIG   += console
CONFIG   -= app_bundle

TEMPLATE = app


SOURCES += main.cpp

# This has been disabled because we don't wanna use shadow builds
# Copies android project to the build directory
#message(Copying native android code ($$PWD/android-native-build) to $$OUT_PWD)
#copydata.commands = $(COPY_DIR) $$PWD/android-native-build $$OUT_PWD
#first.depends = $(first) copydata
#export(first.depends)
#export(copydata.commands)
#QMAKE_EXTRA_TARGETS += first copydata

OTHER_FILES += \
    android-build/custom_rules.xml

