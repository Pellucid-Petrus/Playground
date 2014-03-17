#!/usr/bin/python
import sys
import re


########### VARS #############################################################################
JAVA_PACKAGE = "org.gnuton.jni"


########## DO NOT TOUCH ######################################################################
NOTIFY_JAVA_FUNC_CODE = "\t\t/** This method is called by C++ when the properti is changed\n" \
                        "\t\tAdd here the code which will manage the jav callbacks **/"

def convert_to_java_type(qtype):
  if not qtype or qtype == "void":
    return "void"
  if qtype == "QString":
    return "String"
  if qtype == "int" or qtype == "qint" or qtype == "qint32" or qtype == "qint16":
    return "int"

  print "Warning type (%s) not converted!" % qtype
  return qtype

# check number of args
if len(sys.argv) != 2:
  print "This script generates java code out of Q_PROPERTYs in Qt header files"
  print "syntax %s myqobject.h" % sys.argv[0]
  
  sys.exit(1)

# get cpp file
CPP_FILE=sys.argv[1]

# check file extension
if CPP_FILE[-1:].lower() != "h":
  print "please pass a C++ header as argument"
  sys.exit(1)


output = list()

# open the header file to extract properties
with open(CPP_FILE, "r") as in_file:
  
  for line in in_file.readlines():
      ### Q_PROPERTY(QString priority READ priority WRITE setPriority NOTIFY priorityChanged)
      rx = "Q_PROPERTY\((.*)\)"
      match = re.search(rx,line)

      if not match or match.lastindex != 1:
        continue
      propertyArray = re.split(r"[\s\t]*", match.group(1))
      print repr(propertyArray)
      
      qprop_type = convert_to_java_type(propertyArray.pop(0))
      qprop_name = propertyArray.pop(0)

      while len(propertyArray):
        prop_type = propertyArray.pop(0)
        prop_func = propertyArray.pop(0)
        if prop_type == "READ":
          output += "        public native %s %s();\n" % (qprop_type, prop_func)
        if prop_type == "WRITE":
          output += "        public native void %s(%s value);\n" % (prop_func, qprop_type)
        if prop_type == "NOTIFY":
          output += "        public static void %s(){\n%s\n\t};\n" % (prop_func, NOTIFY_JAVA_FUNC_CODE)
          pass
       
  in_file.close()
  
# write outputfile
OUTPUT_CLASS_NAME = CPP_FILE[:-2]
OUTPUT_FILE = "%sWrapper.java" % OUTPUT_CLASS_NAME
with open(OUTPUT_FILE, "w") as out_file:
  out_file.write("package %s;\n" % JAVA_PACKAGE)
  out_file.write("public class %sWrapper {\n" % OUTPUT_CLASS_NAME)

  for line in output:
  	out_file.write(line)

  out_file.write("}")

  out_file.close()
  print "**** %s wrote, check the file and if it's okay move it into the package %s" % (OUTPUT_FILE, JAVA_PACKAGE)
