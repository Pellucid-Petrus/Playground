#!/usr/bin/python
# -*- coding: utf-8 -*-

###############################################################################
#   
#  Bonsai server
#  
#  Main file.
#
################################################################################
from flask import Flask, jsonify, render_template
import json
from dao import dao
import os
from jsonp import support_jsonp

# vars
app = Flask(__name__)
VERSION=0.1

# config
app.config.update(dict(
    DEBUG=True,
    DB='testdb',
    HOSTNAME='localhost',
    USER='testuser',
    PASSWORD='testpass'
))

# API
@app.route('/get/<app_id>/',  methods=['GET'])
@support_jsonp
def get_data(app_id):
    # show the user profile for that user
    app_config = dao.get_
    res = jsonify(app_config)
    print res.response
    return res

@app.route('/')
def welcome():
    return render_template('index.html', version=VERSION)

# main
if __name__ == '__main__':
    app.debug = True
    with dao(app.config) as dd:
      app.run()