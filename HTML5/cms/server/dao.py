from db import db

class dao:
  def __init__(self, config):
    print "Instantiating DAO..."
    self.d = db()
    self.d.connect(config)

  def __enter__(self):
    return self
  
  def __exit__(self, type, value, traceback):
    self.d.disconnect()


if __name__ == "__main__":
  config = {}
  config["host"] = "localhost"
  config["user"] = "testuser"
  config["password"] = "testuser"
  config["db"] = "testdb"
  
  with dao(config) as dd:
    pass
