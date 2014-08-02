#!/usr/bin/python
# -*- coding: utf-8 -*-
import MySQLdb as mdb
import sys

class db:
  def __init__(self):
    print "init"
    self.con = None
    self.cur = None

  def connect(self, config):
    print "connecting"
    if (self.con):
      print "Already connected to DB"
      return
    try:
      self.con = mdb.connect(config["HOSTNAME"], config["USER"], config["PASSWORD"], config["DB"])
      self.cur = self.con.cursor()
    except Exception as e:
      print "Fatal error: " + str(e)
      sys.exit(-1) 
  
  def disconnect(self):
    print "disconnect"
    self.con.close()
    self.con = None
    self.cur = None

  def query(self, query):
    try:
      self.cur.execute(query)
      return self.cur.fetchall()
    except mdb.ProgrammingError as e:
      print "Warning: Failed to run the query %s. The problem: %s" % (query, str(e))

if __name__ == "__main__":
  print "testing"
  d = db()
  config = {}
  config["host"] = "localhost"
  config["user"] = "testuser"
  config["password"] = "testuser"
  config["db"] = "testdb"
 
  d.connect(config)
  d.query("SELECT * FROM Writers")
  d.disconnect() 
