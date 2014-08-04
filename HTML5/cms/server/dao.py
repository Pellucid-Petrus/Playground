from db import db

import hashlib

class dao:
  DEBUG = True
  def __init__(self, config):
    print "Instantiating DAO..."
    self.db = db()
    self.db.DEBUG = self.DEBUG
    self.db.connect(config)

  def __enter__(self):
    return self
  
  def __exit__(self, type, value, traceback):
    self.db.disconnect()

  # Generic APIs
  def commit(self):
    self.db.commit();

  # User APIs
  def create_new_user(self, user, secret):
    secret= self.__hash_secret(secret) 
    q = 'INSERT INTO bonsai_app_config (user, secret, appname, config) VALUES ("%s", "%s", "", "{}");' % (user, secret)
    res = self.db.query(q)
  
  def delete_user(self, user, secret):
    if not user or not secret:
      raise Exception("delete_user requires user or secret")

    secret= self.__hash_secret(secret)
    q = "delete from bonsai_app_config where user='%s' and secret='%s'" % (user, secret);
    res = self.db.query(q)

  # App config APIs
  def get_app_config(self, user):
    if user == None:
      return None 
    query = 'select config from bonsai_app_config where user="%s";' % user
    res = self.db.query(query)
    return res[0][0] # (('{"foo":"bar"}',),)
 
  def update_user_data(self, user, config, secret, appname=None):
    """ user, config, secret are mandatory arguments
    """
    config = self.db.escape_str(config)
    secret= self.__hash_secret(secret)
    if not config or not user:
      return
    q= 'UPDATE bonsai_app_config SET '
    if secret != None:
      q +='secret="%s",' % secret
    if appname != None:
      q +='appname="%s",' % appname
      
    q += 'config="%s" where user="%s" and secret="%s";' % (config, user, secret);  
    res = self.db.query(q)

  def __hash_secret(self, secret):
    print hashlib.sha256(secret).hexdigest()

if __name__ == "__main__":
  config = dict(
    DB='testdb',
    HOSTNAME='localhost',
    USER='testuser',
    PASSWORD='testpass') 
  
  with dao(config) as dd:
    # insert okay
    dd.create_new_user("fabio", "thepassword");
    # fail to insert because same key is already in
    dd.create_new_user("fabio", "thepassword");
    
    # set app config
    dd.update_user_data("fabio", '{"foo":"bar"}', "thepassword")    
    # get app config
    print "APP CONFIG FOR FABIO:" + str(dd.get_app_config("fabio"));

    # delete user
    #dd.delete_user("fabio");

     
