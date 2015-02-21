from db import db

import hashlib
import json


class Dao:
    """
    Interface to the DB
    """
    DEBUG = True

    def __init__(self, config):
        self.db = db()
        self.db.DEBUG = self.DEBUG
        self.__dbg_msg("Initializing DAO...")
        self.db.connect(config)

    def __enter__(self):
        return self

    def __exit__(self, type, value, traceback):
        self.db.disconnect()


    # Common Private methods
    def __dbg_msg(self, msg):
        if self.DEBUG:
            print msg

    def __hash_secret(self, secret):
        hash = hashlib.sha256(secret).hexdigest()
        return hash

    def __validate_json(self, json_str):
        """ Throw ValueError exception if the string is not JSON """
        json.loads(json_str)


    # Generic APIs
    def commit(self):
        self.db.commit();

    # User APIs
    def create_new_user(self, user, secret):
        secret= self.__hash_secret(secret)
        q = 'INSERT INTO bonsai_app_config (user, secret, appname, config) VALUES ("%s", "%s", "", "{}");' % (user, secret)
        res = self.db.query(q)
        return res

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
        if res != ():
            return res[0][0] # (('{"foo":"bar"}',),)
        else:
            return {}

    def update_user_data(self, user, config, secret, appname=None):
        """ user, config, secret are mandatory arguments
            NOTE: SECRET IS OPTIONAL FOR THE TIME BEING - NO SECURITY
        """
        if isinstance(config, dict):
            config = json.dumps(config)
        elif isinstance(config, str):
            self.__validate_json(config)

        print type(config)
        print config

        if not user or not config:
            print "NO USER or INVALID CONFIG"
            return False
        config = self.db.escape_str(config)
        secret = self.__hash_secret(secret)

        q = 'UPDATE bonsai_app_config SET '
        if secret != None:
            q += 'secret="%s",' % secret
        if appname != None:
            q += 'appname="%s",' % appname

        q += 'config="%s" where user="%s"' % (config, user);
        print q
        res = self.db.query(q)
        return True

if __name__ == "__main__":
    config = dict(
        DB='testdb',
        HOSTNAME='localhost',
        USER='testuser',
        PASSWORD='testpass')

    with Dao(config) as dd:
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