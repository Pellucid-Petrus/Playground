#!/usr/bin/python
# -*- coding: utf-8 -*-
import MySQLdb as mdb
import sys

class db:
  DEBUG = False

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
    self.con.commit()
    self.con.close()
    self.con = None
    self.cur = None

  def query(self, q):
    try:
      if self.DEBUG:
        print "Running query:" + q
      self.cur.execute(q)
      return self.cur.fetchall()
    except mdb.ProgrammingError as e:
      print "Warning: Programming error for '%s'. The problem: %s" % (q, str(e))
    except mdb.IntegrityError as x:
      print "Warning: Integrity error for '%s'. The problem: %s" % (q, str(x)) 

  def escape_str(self, s):
    return self.con.escape_string(s)

if __name__ == "__main__":
  print "testing"
  d = db()
  config = dict(
    DB='testdb',
    HOSTNAME='localhost',
    USER='testuser',
    PASSWORD='testpass')
 
  d.connect(config)
  #d.query("SELECT * FROM Writers")
  print "ESCAPED STRING" + d.escape_str('"ciao"');
  d.query('INSERT INTO bonsai_app_config (user, secret, appname, config) VALUES ("fabio", "0", "", "");');
  #d.disconnect() 
