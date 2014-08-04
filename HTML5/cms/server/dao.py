from db import db

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
    q = 'INSERT INTO bonsai_app_config (user, secret, appname, config) VALUES ("%s", "%s", "", "");' % (user, secret)
    res = self.db.query(q)
  
  def delete_user(self, user):
    q = "delete from bonsai_app_config where user='%s'" % user;
    res = self.db.query(q)

  # App config APIs
  def get_app_config(self, user):
    if user == None:
      return None
    query = 'select config from bonsai_app_config where user="%s";' % user
    res = self.db.query(query)
    return res[0][0] # (('{"foo":"bar"}',),)
 
  def update_user_data(self, user, secret, appname, config):
    config = self.db.escape_str(config)    
    q= 'UPDATE bonsai_app_config SET secret="%s", appname="%s", config="%s" where user="%s";' % (secret,appname, config, user);  
    res = self.db.query(q)

if __name__ == "__main__":
  config = dict(
    DB='testdb',
    HOSTNAME='localhost',
    USER='testuser',
    PASSWORD='testuser') 
  
  with dao(config) as dd:
    # insert okay
    dd.create_new_user("fabio", 0);
    # fail to insert because same key is already in
    dd.create_new_user("fabio", 0);
    
    # set app config
    dd.update_user_data("fabio", "theappid", "appname", '{"foo":"bar"}')    
    # get app config
    print "APP CONFIG FOR FABIO:" + str(dd.get_app_config("fabio"));

    # delete user
    #dd.delete_user("fabio");

     
