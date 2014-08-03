USE testdb;
DROP TABLE IF EXISTS bonsai_app_config;
CREATE TABLE bonsai_app_config (
  id MEDIUMINT NOT NULL AUTO_INCREMENT,
  user    CHAR(24) NOT NULL,
  secret   CHAR(24) NOT NULL,
  appname TEXT NOT NULL,
  config  TEXT NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (user)
);
