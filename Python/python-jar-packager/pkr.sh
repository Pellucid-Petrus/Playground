#!/bin/bash

JYTHON_BIN=/usr/share/java/jython.jar
JYTHON_LIB=/usr/share/jython/Lib/
TEMP_DIR=/tmp/jython-pkg
MY_APP_DIRECTORY=$1
 
echo "*** Python to JAR ***"

function create_basic_jar() {
	# check if vars exists
	# TODO

	# create working dir
	echo "Making working dir $TEMP_DIR"
	rm -Rf $TEMP_DIR && echo REMOVED OLD DIR
	mkdir -p $TEMP_DIR
 
	# copy essential files
	echo "Creatig basic JAR..."
	cd $TEMP_DIR
	cp $JYTHON_BIN jythonlib.jar 
	cp -R $JYTHON_LIB .
	zip -r jythonlib.jar Lib
	rm -Rf Lib
}

function add_app_to_jar() {
	echo "App dir: $1"
	cd $TEMP_DIR
	mkdir Lib
	cp $1/* Lib
	cp jythonlib.jar myapp.jar
	zip -r myapp.jar Lib
	#jar ufm myapp.jar othermanifest.mf
}

# ** MAIN **

if [ $# != 1 ]; then
	echo "This script needs the path to the python app as argument"
	exit
fi

create_basic_jar

add_app_to_jar $MY_APP_DIRECTORY

