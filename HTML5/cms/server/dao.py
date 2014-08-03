from db import db

class dao:
  def __init__(self, config):
    print "Instantiating DAO..."
    self.db = db()
    self.db.connect(config)

  def __enter__(self):
    return self
  
  def __exit__(self, type, value, traceback):
    self.db.disconnect()

  def create_new_user(self, user, secret):
    q = 'INSERT INTO bonsai_app_config (user, secret, appname, config) VALUES ("%s", "%s", "", "");' % (user, secret)
    print "running " + q
    res = self.db.query(q)
  
  def commit(self):
    self.db.commit();
    
  def delete_user(self, user):
    q = "delete from bonsai_app_config where user='%s'" % user;
    res = self.db.query(q)

  def commit_changes_to_db(self):
    self.db.commit()

  def save_app_config(self, user, appid, appname, config):
    query = 'INSERT INTO bonsai_app_config (user, appid, appname, config) VALUES ("gnuton", "0", "toniotest", "{}");'
    res = self.db.query(query)

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
    # delete user
    dd.delete_user("fabio");
     
