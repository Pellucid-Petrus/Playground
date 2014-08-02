###############################################################################
#   
#  Bonsai server
#  
#  Main file.
#
################################################################################
from flask import Flask, jsonify, render_template
import json

from jsonp import support_jsonp

# vars
app = Flask(__name__)
VERSION=0.1

# API
@app.route('/get/<app_id>/',  methods=['GET'])
@support_jsonp
def get_data(app_id):
    # show the user profile for that user
    res = jsonify({"foo":"bar"})
    print res.response
    return res

@app.route('/')
def welcome():
    return render_template('index.html', version=VERSION)

# main
if __name__ == '__main__':
    app.debug = True
    app.run()
